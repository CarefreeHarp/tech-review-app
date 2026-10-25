import { initializeApp, cert } from "firebase-admin/app";
import { getFirestore, Timestamp } from "firebase-admin/firestore";
import serviceAccount from "./serviceAccountKey.json" with { type: "json" };
import { fakerES as faker } from "@faker-js/faker";

initializeApp({
  credential: cert(serviceAccount),
});

const db = getFirestore();

/** Construye un perfil ficticio; no crea una cuenta de Authentication. */
function buildUser(i) {
  const now = Timestamp.now();
  return {
    id: i,
    firebase_uid: null,
    email: `devicers.seed.${i}@example.test`,
    username: `${faker.internet.username()}_${i}`,
    biography: faker.person.bio(),
    profile_image_url: faker.image.personPortrait(),
    notifications_last_viewed_at: null,
    followers_count: 0,
    follows_count: 0,
    review_bookmarks_count: 0,
    is_active: true,
    created_at: now,
    updated_at: now,
  };
}

/** Construye una categoría raíz. */
function buildCategory(i, name) {
  return {
    id: i,
    name: name,
    parent_category_id: null,
    description: `Productos de ${name}.`,
  };
}

/** Construye un producto perteneciente a la categoría recibida. */
function buildProduct(category, i) {
  const now = Timestamp.now();
  const model = faker.string.alphanumeric(6).toUpperCase();
  // Las fotos corresponden a la categoría; Picsum puede devolver paisajes u otros temas.
  const photos = {
    Celulares: ["photo-1511707171634-5f897ff02aa9", "photo-1512499617640-c74ae3a79d37"],
    Audio: ["photo-1505740420928-5e560c06d30e", "photo-1484704849700-f032a568e944"],
    Computadores: ["photo-1496181133206-80ce9b88a853", "photo-1517336714731-489689fd1ca8"],
  };
  return {
    id: i,
    category_id: category.id,
    category_name: category.name,
    brand_name: faker.company.name(),
    name: `${category.name} ${model}`,
    model: model,
    description: faker.commerce.productDescription(),
    image_url: `https://images.unsplash.com/${faker.helpers.arrayElement(photos[category.name])}?auto=format&fit=crop&w=600&q=80`,
    release_date: faker.date.past().toISOString().slice(0, 10),
    specifications: "{}",
    stores_count: 0,
    is_active: true,
    created_at: now,
    updated_at: now,
  };
}

/** Construye una reseña con su autor y producto. */
function buildReview(user, product, i) {
  const now = Timestamp.now();
  return {
    id: i,
    user_id: user.id,
    article_id: product.id,
    rating: faker.number.int({ min: 1, max: 5 }),
    title: faker.lorem.sentence(),
    body: faker.lorem.paragraph(),
    likes_count: 0,
    comments_count: 0,
    is_active: true,
    created_at: now,
    updated_at: now,
  };
}

/** Construye un like asociado a su usuario y notificación. */
function buildReviewLike(user, notification = null) {
  return {
    user_id: user.id,
    notification_id: notification?.id ?? null,
    created_at: Timestamp.now(),
  };
}

/** Construye un comentario o una respuesta. */
function buildComment(user, i, parentId = null) {
  const now = Timestamp.now();
  return {
    id: i,
    user_id: user.id,
    user_username: user.username,
    user_profile_image_url: user.profile_image_url,
    parent_comment_id: parentId,
    body: faker.lorem.sentence(),
    likes_count: 0,
    is_active: true,
    created_at: now,
    updated_at: now,
  };
}

/** Construye un evento de actividad para el usuario destinatario. */
function buildNotification(user, actor, i, type, review = null, comment = null) {
  return {
    id: i,
    user_id: user.id,
    actor_user_id: actor.id,
    type: type,
    review_id: review?.id ?? null,
    comment_id: comment?.id ?? null,
    is_read: false,
    created_at: Timestamp.now(),
  };
}

/** Construye la relación almacenada en follows. */
function buildFollow(user, notification = null) {
  return {
    followed_id: user.id,
    notification_id: notification?.id ?? null,
    created_at: Timestamp.now(),
  };
}

/** Construye la relación almacenada en followers. */
function buildFollower(user) {
  return {
    follower_id: user.id,
    created_at: Timestamp.now(),
  };
}

/** Construye una referencia a una reseña guardada. */
function buildReviewBookmark(review) {
  return {
    review_id: review.id,
    created_at: Timestamp.now(),
  };
}

/** Construye los contadores que usará la app después del seed. */
function buildCounters(ids) {
  return {
    next_user_id: ids.user,
    next_category_id: ids.category,
    next_article_id: ids.product,
    next_review_id: ids.review,
    next_comment_id: ids.comment,
    next_notification_id: ids.notification,
  };
}

// Cada función usa un solo batch, pensado para un máximo de 500 escrituras.

/** Inserta perfiles ficticios y devuelve los usuarios creados. */
async function seedUsers(count = 10) {
  const batch = db.batch();
  const users = [];

  for (let i = 1; i <= count; i++) {
    const user = buildUser(i);
    const userRef = db.collection("users").doc(String(user.id));
    batch.set(userRef, user);
    users.push(user);
  }

  await batch.commit();
  return users;
}

/** Inserta las categorías del catálogo de prueba. */
async function seedCategories() {
  const batch = db.batch();
  const categories = [];
  const names = ["Celulares", "Audio", "Computadores"];

  for (let i = 0; i < names.length; i++) {
    const category = buildCategory(i + 1, names[i]);
    const categoryRef = db.collection("categories").doc(String(category.id));
    batch.set(categoryRef, category);
    categories.push(category);
  }

  await batch.commit();
  return categories;
}

/** Inserta varios productos por categoría. */
async function seedProducts(categories, productsPerCategory = 5) {
  const batch = db.batch();
  const products = [];

  for (const category of categories) {
    for (let i = 1; i <= productsPerCategory; i++) {
      const product = buildProduct(category, products.length + 1);
      const productRef = db.collection("articles").doc(String(product.id));
      batch.set(productRef, product);
      products.push(product);
    }
  }

  await batch.commit();
  return products;
}

/** Inserta varias reseñas por usuario, asociadas a productos del catálogo. */
async function seedReviews(users, products, reviewsPerUser = 5) {
  const batch = db.batch();
  const reviews = [];

  for (const user of users) {
    for (let i = 1; i <= reviewsPerUser; i++) {
      const product = faker.helpers.arrayElement(products);
      const review = buildReview(user, product, reviews.length + 1);
      const reviewRef = db.collection("reviews").doc(String(review.id));
      batch.set(reviewRef, review);
      reviews.push(review);
    }
  }

  await batch.commit();
  return reviews;
}

/** Inserta likes de otros usuarios y actualiza la cantidad de cada reseña. */
async function seedReviewLikes(users, reviews, likesPerReview = 3) {
  const batch = db.batch();
  const likes = [];

  for (const review of reviews) {
    const others = users.filter((user) => user.id !== review.user_id);
    const selected = faker.helpers.arrayElements(others, Math.min(likesPerReview, others.length));
    const reviewRef = db.collection("reviews").doc(String(review.id));

    for (const user of selected) {
      const like = buildReviewLike(user);
      batch.set(reviewRef.collection("likes").doc(String(user.id)), like);
      likes.push({ review, user, like });
    }
    batch.update(reviewRef, { likes_count: selected.length });
  }

  await batch.commit();
  return likes;
}

/** Inserta comentarios y respuestas en cada reseña. */
async function seedComments(users, reviews, commentsPerReview = 2) {
  const batch = db.batch();
  const comments = [];

  for (const review of reviews) {
    const reviewRef = db.collection("reviews").doc(String(review.id));
    let parentId = null;

    for (let i = 1; i <= commentsPerReview; i++) {
      const user = faker.helpers.arrayElement(users);
      const comment = buildComment(user, comments.length + 1, parentId);
      batch.set(reviewRef.collection("comments").doc(String(comment.id)), comment);
      comments.push({ review, user, comment });
      parentId = comment.id;
    }
    batch.update(reviewRef, { comments_count: commentsPerReview });
  }

  await batch.commit();
  return comments;
}

/** Inserta seguimientos sin relaciones repetidas ni seguimientos a uno mismo. */
async function seedFollows(users, followsPerUser = 2) {
  const batch = db.batch();
  const follows = [];
  const count = Math.min(followsPerUser, Math.max(0, users.length - 1));

  for (let i = 0; i < users.length; i++) {
    const user = users[i];
    const userRef = db.collection("users").doc(String(user.id));

    for (let offset = 1; offset <= count; offset++) {
      const followed = users[(i + offset) % users.length];
      const follow = buildFollow(followed);
      batch.set(userRef.collection("follows").doc(String(followed.id)), follow);
      follows.push({ user, followed, follow });
    }
    batch.update(userRef, { follows_count: count });
  }

  await batch.commit();
  return follows;
}

/** Inserta el lado inverso de los seguimientos anteriores. */
async function seedFollowers(users, follows) {
  const batch = db.batch();
  const followers = [];

  for (const { user, followed, follow } of follows) {
    const follower = buildFollower(user);
    follower.created_at = follow.created_at;
    const followerRef = db.collection("users").doc(String(followed.id)).collection("followers").doc(String(user.id));
    batch.set(followerRef, follower);
    followers.push({ user: followed, follower });
  }
  for (const user of users) {
    const count = follows.filter((relation) => relation.followed.id === user.id).length;
    batch.update(db.collection("users").doc(String(user.id)), { followers_count: count });
  }

  await batch.commit();
  return followers;
}

/** Inserta reseñas guardadas por cada usuario. */
async function seedReviewBookmarks(users, reviews, bookmarksPerUser = 2) {
  const batch = db.batch();
  const bookmarks = [];

  for (const user of users) {
    const selected = faker.helpers.arrayElements(reviews, Math.min(bookmarksPerUser, reviews.length));
    const userRef = db.collection("users").doc(String(user.id));

    for (const review of selected) {
      const bookmark = buildReviewBookmark(review);
      batch.set(userRef.collection("review_bookmarks").doc(String(review.id)), bookmark);
      bookmarks.push({ user, bookmark });
    }
    batch.update(userRef, { review_bookmarks_count: selected.length });
  }

  await batch.commit();
  return bookmarks;
}

/** Inserta avisos para las interacciones creadas, sin notificar acciones propias. */
async function seedNotifications(likes = [], comments = [], follows = []) {
  const batch = db.batch();
  const notifications = [];

  for (const { review, user } of likes) {
    const notification = buildNotification({ id: review.user_id }, user, notifications.length + 1, "review_like", review);
    batch.set(db.collection("notifications").doc(String(notification.id)), notification);
    batch.update(db.doc(`reviews/${review.id}/likes/${user.id}`), { notification_id: notification.id });
    notifications.push(notification);
  }
  for (const { review, user, comment } of comments) {
    if (review.user_id === user.id) continue;
    const notification = buildNotification({ id: review.user_id }, user, notifications.length + 1, "comment", review, comment);
    batch.set(db.collection("notifications").doc(String(notification.id)), notification);
    notifications.push(notification);
  }
  for (const { user, followed } of follows) {
    const notification = buildNotification(followed, user, notifications.length + 1, "follow");
    batch.set(db.collection("notifications").doc(String(notification.id)), notification);
    batch.update(db.doc(`users/${user.id}/follows/${followed.id}`), { notification_id: notification.id });
    notifications.push(notification);
  }

  await batch.commit();
  return notifications;
}

/** Inserta los contadores posteriores a la creación de los datos de prueba. */
async function seedCounters(users, categories, products, reviews, comments = [], notifications = []) {
  const batch = db.batch();
  const counters = buildCounters({
    user: users.length + 1,
    category: categories.length + 1,
    product: products.length + 1,
    review: reviews.length + 1,
    comment: comments.length + 1,
    notification: notifications.length + 1,
  });

  batch.set(db.collection("counters").doc("next_id"), counters, { merge: true });
  await batch.commit();
  return counters;
}

/** Carga los datos de prueba en el orden requerido por sus relaciones. */
async function main() {
  try {
    console.log("Creando usuarios...");
    const users = await seedUsers();

    console.log("Creando categorías...");
    const categories = await seedCategories();

    console.log("Creando productos...");
    const products = await seedProducts(categories);

    console.log("Creando reseñas...");
    const reviews = await seedReviews(users, products);

    console.log("Creando likes de reseñas...");
    const likes = await seedReviewLikes(users, reviews);

    console.log("Creando comentarios...");
    const comments = await seedComments(users, reviews);

    console.log("Creando seguimientos...");
    const follows = await seedFollows(users);
    await seedFollowers(users, follows);

    console.log("Creando reseñas guardadas...");
    await seedReviewBookmarks(users, reviews);

    console.log("Creando notificaciones...");
    const notifications = await seedNotifications(likes, comments, follows);

    console.log("Actualizando contadores...");
    await seedCounters(users, categories, products, reviews, comments, notifications);

    console.log("Carga de datos completada.");
    process.exit(0);
  } catch (error) {
    console.error("Error al cargar los datos:", error.message);
    process.exit(1);
  }
}

main();
