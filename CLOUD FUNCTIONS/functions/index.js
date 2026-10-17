const{
    onDocumentUpdated,
    onDocumentCreated,
    onDocumentWritten,
} = require("firebase-functions/v2/firestore");
const logger = require("firebase-functions/logger");
const admin = require("firebase-admin")

admin.initializeApp();

const db = admin.firestore();

/**
 * Envía una notificación push al autor de una reseña cuando otro usuario le da like.
 *
 * Se dispara al crearse reviews/{reviewId}/likes/{userId}. La app crea ese documento en la misma
 * transacción que incrementa likes_count y guarda el registro en notifications; esta función solo
 * entrega el aviso al dispositivo del autor mediante Firebase Cloud Messaging.
 */
exports.sendReviewLikeNotification = onDocumentCreated("reviews/{reviewId}/likes/{userId}", async (event) => {

    // 1. Obtiene los IDs desde la ruta del documento que activó la función.
    const reviewId = event.params.reviewId;
    const likerId = event.params.userId;

    // 2. Consulta la reseña para conocer a su autor, que es quien recibe la notificación.
    const reviewDoc = await db.collection("reviews").doc(reviewId).get();
    if (!reviewDoc.exists) {
        logger.warn("La reseña del like ya no existe", {reviewId, likerId});
        return;
    }
    const review = reviewDoc.data();
    const authorId = String(review.user_id);

    // 3. Igual que la app, no se notifica al autor por los likes que da a sus propias reseñas.
    if (authorId === likerId) {
        return;
    }

    // 4. Consulta al autor (destinatario), a quien dio el like y el artículo reseñado.
    const [authorDoc, likerDoc, articleDoc] = await Promise.all([
        db.collection("users").doc(authorId).get(),
        db.collection("users").doc(likerId).get(),
        db.collection("articles").doc(String(review.article_id)).get(),
    ]);
    // La app guarda fcm_token al registrarse, al iniciar sesión o cuando FCM renueva el token.
    const fcmToken = authorDoc.get("fcm_token");

    // 5. Sin token, el autor no tiene un dispositivo con sesión iniciada al que avisar.
    if (!fcmToken) {
        logger.info("El autor no tiene un token FCM registrado", {authorId, reviewId});
        return;
    }

    // 6. Construye el mensaje con el nombre real de quien dio like y el artículo reseñado.
    const likerName = likerDoc.get("username");
    const actor = likerName ? `@${likerName}` : "Alguien";
    const articleName = articleDoc.get("name");
    const message = {
        token: fcmToken,
        notification: {
            title: "Nuevo me gusta",
            body: articleName ?
                `${actor} le dio me gusta a tu reseña de ${articleName}` :
                `${actor} le dio me gusta a tu reseña`,
        },
    };

    try {
        // 7. Usa el SDK de Admin de Firebase para entregar la notificación al dispositivo del autor.
        await admin.messaging().send(message);
        logger.log("Notificación de like enviada", {reviewId, likerId, authorId});
    } catch (error) {
        logger.error("Error al enviar la notificación de like", error);
    }
});

// Firestore permite como máximo 500 escrituras por lote.
const BATCH_LIMIT = 500;

/**
 * Aplica una escritura a cada documento en lotes que respetan el límite de Firestore.
 *
 * @param {Array} documents Documentos obtenidos de una consulta.
 * @param {Function} write Recibe el lote y un documento, y agrega la escritura correspondiente.
 */
async function writeInBatches(documents, write) {
    for (let start = 0; start < documents.length; start += BATCH_LIMIT) {
        const batch = db.batch();
        documents.slice(start, start + BATCH_LIMIT).forEach((document) => write(batch, document));
        await batch.commit();
    }
}

/**
 * Mantiene actualizadas las copias del autor que guarda cada comentario.
 *
 * Los comentarios copian username y profile_image_url de su autor en user_username y
 * user_profile_image_url, y la pantalla de la reseña muestra esas copias; cuando el usuario cambia
 * su nombre o su foto desde Editar perfil, esta función las reemplaza en todos sus comentarios.
 */
exports.syncUserInfoInComments = onDocumentUpdated("users/{userId}", async (event) => {

    // 1. Obtiene el ID desde la ruta; en los comentarios user_id se guarda como número.
    const userId = Number(event.params.userId);

    // 2. Obtiene la data antes y después de la modificación.
    const beforeData = event.data.before.data();
    const afterData = event.data.after.data();

    // 3. Solo continúa si cambió alguno de los datos copiados en los comentarios.
    if (beforeData.username === afterData.username &&
        beforeData.profile_image_url === afterData.profile_image_url) {
        return;
    }

    // 4. Trae los comentarios del usuario en todas las reseñas (consulta de grupo de colecciones).
    const comments = await db.collectionGroup("comments").where("user_id", "==", userId).get();

    // 5. Reemplaza las copias en lotes atómicos.
    await writeInBatches(comments.docs, (batch, comment) => {
        batch.update(comment.ref, {
            user_username: afterData.username,
            user_profile_image_url: afterData.profile_image_url ?? null,
        });
    });
    logger.log("Copias del usuario actualizadas en sus comentarios", {userId, comments: comments.size});
});

/**
 * Retira las referencias a una reseña cuando su autor la elimina.
 *
 * La app elimina reseñas de forma lógica (is_active = false): la reseña, sus likes y sus comentarios
 * se conservan, pero se eliminan los guardados de los usuarios y las notificaciones que apuntan a
 * ella, para que Guardados y Actividad no intenten abrir una reseña retirada.
 */
exports.cleanUpDeletedReview = onDocumentUpdated("reviews/{reviewId}", async (event) => {

    // 1. Obtiene el ID desde la ruta; las referencias guardan review_id como número.
    const reviewId = Number(event.params.reviewId);

    // 2. Solo continúa cuando la reseña pasa de activa a retirada.
    const wasActive = event.data.before.get("is_active");
    const isActive = event.data.after.get("is_active");
    if (wasActive !== true || isActive !== false) {
        return;
    }

    // 3. Busca los guardados de todos los usuarios y las notificaciones que apuntan a la reseña.
    const [bookmarks, notifications] = await Promise.all([
        db.collectionGroup("review_bookmarks").where("review_id", "==", reviewId).get(),
        db.collection("notifications").where("review_id", "==", reviewId).get(),
    ]);

    // 4. Elimina ambas referencias; countUserReviewBookmarks recalcula después los contadores de guardados.
    await writeInBatches([...bookmarks.docs, ...notifications.docs], (batch, reference) => {
        batch.delete(reference.ref);
    });
    logger.log("Referencias de la reseña eliminada retiradas",
        {reviewId, bookmarks: bookmarks.size, notifications: notifications.size});
});

/**
 * Recalcula el contador de un documento padre con la cantidad real de documentos de su subcolección.
 *
 * Se recuenta en vez de sumar o restar uno: la app ya incrementa likes_count en su propia
 * transacción, así que el recuento no duplica ese cambio y corrige cualquier desfase, venga de la
 * app, de la consola o de otra función.
 *
 * @param {object} event Evento de escritura de un documento de la subcolección.
 * @param {string} counterField Campo del documento padre que guarda la cantidad.
 */
async function recountSubcollection(event, counterField) {
    // Una edición no cambia la cantidad de documentos de la subcolección.
    if (event.data.before.exists && event.data.after.exists) {
        return;
    }
    const subcollection = (event.data.after.exists ? event.data.after : event.data.before).ref.parent;
    const parentRef = subcollection.parent;

    // La lectura del padre y el conteo ocurren en la misma transacción que la corrección.
    const correctedTotal = await db.runTransaction(async (transaction) => {
        const parent = await transaction.get(parentRef);
        // Si el documento padre ya no existe, no hay contador que corregir.
        if (!parent.exists) {
            return null;
        }
        const total = (await transaction.get(subcollection.count())).data().count;
        if (parent.get(counterField) === total) {
            return null;
        }
        transaction.update(parentRef, {[counterField]: total});
        return total;
    });
    if (correctedTotal !== null) {
        logger.log("Contador corregido", {path: parentRef.path, field: counterField, total: correctedTotal});
    }
}

// Un trigger por cada subcolección que tiene un contador en su documento padre.
exports.countReviewLikes = onDocumentWritten("reviews/{reviewId}/likes/{userId}",
    (event) => recountSubcollection(event, "likes_count"));
exports.countReviewComments = onDocumentWritten("reviews/{reviewId}/comments/{commentId}",
    (event) => recountSubcollection(event, "comments_count"));
exports.countCommentLikes = onDocumentWritten("reviews/{reviewId}/comments/{commentId}/likes/{userId}",
    (event) => recountSubcollection(event, "likes_count"));
exports.countUserFollowers = onDocumentWritten("users/{userId}/followers/{followerId}",
    (event) => recountSubcollection(event, "followers_count"));
exports.countUserFollows = onDocumentWritten("users/{userId}/follows/{followedId}",
    (event) => recountSubcollection(event, "follows_count"));
exports.countUserReviewBookmarks = onDocumentWritten("users/{userId}/review_bookmarks/{reviewId}",
    (event) => recountSubcollection(event, "review_bookmarks_count"));
exports.countArticleStores = onDocumentWritten("articles/{articleId}/stores/{storeId}",
    (event) => recountSubcollection(event, "stores_count"));

/**
 * Iguala el lado opuesto de un seguimiento, que se guarda dos veces: users/{A}/follows/{B} y
 * users/{B}/followers/{A}. Si ambos lados ya coinciden no escribe nada, así que funciona tanto si
 * la app escribe un lado como si escribe los dos.
 *
 * @param {object} event Evento de escritura de uno de los lados.
 * @param {object} mirrorRef Documento del lado opuesto.
 * @param {object} mirrorFields Campos propios del lado opuesto.
 */
async function mirrorFollowRelation(event, mirrorRef, mirrorFields) {
    // Una edición no crea ni elimina la relación.
    if (event.data.before.exists && event.data.after.exists) {
        return;
    }
    await db.runTransaction(async (transaction) => {
        const mirror = await transaction.get(mirrorRef);
        if (event.data.after.exists && !mirror.exists) {
            // Ambos lados conservan la misma fecha de creación.
            transaction.set(mirrorRef, {
                ...mirrorFields,
                created_at: event.data.after.get("created_at") ?? admin.firestore.FieldValue.serverTimestamp(),
            });
        } else if (!event.data.after.exists && mirror.exists) {
            transaction.delete(mirrorRef);
        }
    });
}

exports.syncFollowsToFollowers = onDocumentWritten("users/{userId}/follows/{followedId}", (event) => {
    const {userId, followedId} = event.params;
    const mirrorRef = db.collection("users").doc(followedId).collection("followers").doc(userId);
    return mirrorFollowRelation(event, mirrorRef, {follower_id: Number(userId)});
});

exports.syncFollowersToFollows = onDocumentWritten("users/{userId}/followers/{followerId}", (event) => {
    const {userId, followerId} = event.params;
    const mirrorRef = db.collection("users").doc(followerId).collection("follows").doc(userId);
    return mirrorFollowRelation(event, mirrorRef, {followed_id: Number(userId)});
});

/**
 * Mantiene el nombre de la categoría que cada artículo copia en category_name.
 * La app no edita categorías; los cambios llegan desde la consola de Firebase.
 */
exports.syncCategoryNameInArticles = onDocumentUpdated("categories/{categoryId}", async (event) => {

    // 1. Obtiene el ID desde la ruta; en los artículos category_id se guarda como número.
    const categoryId = Number(event.params.categoryId);

    // 2. Solo continúa si cambió el nombre de la categoría.
    const beforeName = event.data.before.get("name");
    const afterName = event.data.after.get("name");
    if (beforeName === afterName) {
        return;
    }

    // 3. Trae los artículos de la categoría y reemplaza la copia del nombre en lotes.
    const articles = await db.collection("articles").where("category_id", "==", categoryId).get();
    await writeInBatches(articles.docs, (batch, article) => {
        batch.update(article.ref, {category_name: afterName});
    });
    logger.log("Nombre de la categoría actualizado en sus artículos", {categoryId, articles: articles.size});
});
