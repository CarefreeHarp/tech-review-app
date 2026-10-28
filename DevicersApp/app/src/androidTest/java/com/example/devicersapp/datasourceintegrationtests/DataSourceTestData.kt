package com.example.devicersapp.datasourceintegrationtests

import com.google.firebase.Timestamp

// Datos compartidos por las pruebas de DataSource: los campos siguen el esquema de Firestore.
// Los IDs empiezan en 1000 y coinciden con los nombres de los documentos creados en setUp.

/** Genera el perfil [i], con ID 1000 + i, UID reconocible y contadores iniciales en cero. */
fun generateUser(i: Int): Map<String, Any?> = mapOf(
    "id" to 1000 + i,
    "firebase_uid" to "uid_$i",
    "email" to "user$i@example.com",
    "username" to "username_$i",
    "biography" to "Bio de usuario $i",
    "profile_image_url" to "https://picsum.photos/200/200?random=$i",
    "notifications_last_viewed_at" to null,
    "followers_count" to 0,
    "follows_count" to 0,
    "review_bookmarks_count" to 0,
    "is_active" to true,
    "created_at" to Timestamp.now(),
    "updated_at" to Timestamp.now()
)

/** Genera el artículo [i], activo y sin tiendas asociadas inicialmente. */
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

/** Genera la reseña [i] vinculada a [userId] y [productId], sin likes ni comentarios. */
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