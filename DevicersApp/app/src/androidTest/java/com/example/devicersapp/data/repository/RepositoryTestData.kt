package com.example.devicersapp.data.repository

import com.google.firebase.Timestamp

// Datos de prueba compartidos por las clases *RepositoryIntegrationTest. Siguen el mismo esquema que
// SEED_DATA/seed.js: campos snake_case y un campo id igual al ID numérico del documento.
// Los IDs empiezan en 1000 para no chocar con los datos cargados con faker.

/** Genera el usuario de prueba [i]: ID 1000 + i, nombre "username_i", activo y sin relaciones. */
fun generateUser(i: Int): Map<String, Any?> = mapOf(
    "id" to 1000 + i,
    "firebase_uid" to "uid_$i",
    "email" to "user$i@example.com",
    "username" to "username_$i",
    "biography" to "Bio de usuario $i",
    "profile_image_url" to "https://picsum.photos/200/200?random=$i",
    "notifications_last_viewed_at" to null,
    // Las Cloud Functions recalculan estos contadores con los documentos reales; sin relaciones valen 0.
    "followers_count" to 0,
    "follows_count" to 0,
    "review_bookmarks_count" to 0,
    "is_active" to true,
    "created_at" to Timestamp.now(),
    "updated_at" to Timestamp.now()
)

/** Genera el artículo de prueba [i]: ID 1000 + i, nombre "Producto i" y activo. */
fun generateProduct(i: Int): Map<String, Any?> = mapOf(
    "id" to 1000 + i,
    "category_id" to 1,
    "category_name" to "Celulares",
    "brand_name" to "Marca $i",
    "name" to "Producto $i",
    "model" to "MODELO-$i",
    "description" to "Descripción del producto $i",
    "image_url" to "https://picsum.photos/600/400?random=$i",
    "release_date" to "2025-01-01",
    "specifications" to "{}",
    "stores_count" to 0,
    "is_active" to true,
    "created_at" to Timestamp.now(),
    "updated_at" to Timestamp.now()
)

/** Genera la reseña de prueba [i] (ID 1000 + i) que el usuario [userId] escribió sobre el artículo [productId]. */
fun generateReview(i: Int, userId: Int, productId: Int): Map<String, Any?> = mapOf(
    "id" to 1000 + i,
    "user_id" to userId,
    "article_id" to productId,
    "rating" to 4,
    "title" to "Título de la reseña $i",
    "body" to "Cuerpo de la reseña $i",
    "likes_count" to 0,
    "comments_count" to 0,
    "is_active" to true,
    "created_at" to Timestamp.now(),
    "updated_at" to Timestamp.now()
)

/**
 * Genera la notificación de prueba [i] (ID 1000 + i): [actorUserId] realizó una acción de tipo [type]
 * que recibe [userId]. [reviewId] es la reseña relacionada, o null si la acción no apunta a una reseña.
 */
fun generateNotification(i: Int, userId: Int, actorUserId: Int, type: String, reviewId: Int?): Map<String, Any?> = mapOf(
    "id" to 1000 + i,
    "user_id" to userId,
    "actor_user_id" to actorUserId,
    "type" to type,
    "review_id" to reviewId,
    "comment_id" to null,
    "is_read" to false,
    "created_at" to Timestamp.now()
)
