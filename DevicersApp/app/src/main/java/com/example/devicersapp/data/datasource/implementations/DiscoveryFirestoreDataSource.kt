package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.DiscoveryNotification
import com.example.devicersapp.data.datasource.DiscoveryRemoteDataSource
import com.example.devicersapp.ui.models.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import java.time.Instant
import javax.inject.Inject

/** Usa las mismas colecciones y los IDs numéricos de tasks/#5-6-7. No escribe datos. */
class DiscoveryFirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : DiscoveryRemoteDataSource {
    override suspend fun getProducts(): List<ProductInfo> = firestore.collection("articles")
        .whereEqualTo("is_active", true).get(Source.SERVER).await().documents.map { d ->
            ProductInfo(d.number("id"), d.number("category_id"), d.number("brand_id"),
                checkNotNull(d.getString("name")), d.getString("model"), d.getString("description"),
                d.getString("image_url"), d.dateText("release_date"),
                d.get("specifications")?.let { Gson().toJson(it) }, true,
                d.dateText("createdAt").orEmpty(), d.dateText("updatedAt").orEmpty())
        }

    override suspend fun getCategories(): List<CategoryInfo> = firestore.collection("categories")
        .get(Source.SERVER).await().documents.map { d ->
            CategoryInfo(d.number("id"), d.optionalNumber("parent_category_id"),
                checkNotNull(d.getString("name")), d.getString("description"))
        }

    override suspend fun getActiveReviews(): List<ReviewInfo> = firestore.collection("reviews")
        .whereEqualTo("is_active", true).get(Source.SERVER).await().documents.map { d ->
            ReviewInfo(d.number("id"), d.number("user_id"), d.number("article_id"), d.number("rating"),
                d.getString("title"), d.getString("body").orEmpty(), true,
                d.dateText("createdAt").orEmpty(), d.dateText("updatedAt").orEmpty())
        }

    override suspend fun getUsers(): List<UserInfo> = firestore.collection("users")
        .whereEqualTo("is_active", true).get(Source.SERVER).await().documents.map { d ->
            UserInfo(d.number("id"), "", d.getString("username").orEmpty(), null,
                d.getString("biography"), d.getString("profile_image_url")?.trim()?.takeIf { it.isNotEmpty() },
                null, true, d.dateText("createdAt").orEmpty(), d.dateText("updatedAt").orEmpty())
        }

    override suspend fun getFollowedIds(userId: Int): Set<Int> = firestore.collection("follows")
        .whereEqualTo("follower_id", userId).get(Source.SERVER).await().documents
        .map { it.number("followed_id") }.toSet()

    override suspend fun getNotifications(userId: Int): List<DiscoveryNotification> =
        firestore.collection("notifications").whereEqualTo("user_id", userId)
            .get(Source.SERVER).await().documents.mapNotNull { d ->
                // Los actores eliminados y tipos desconocidos no producen destinos ficticios.
                val actor = d.optionalNumber("actor_user_id") ?: return@mapNotNull null
                val time = d.dateText("created_at")?.let { runCatching { Instant.parse(it).toEpochMilli() }.getOrNull() }
                    ?: return@mapNotNull null
                DiscoveryNotification(d.id, d.number("user_id"), actor,
                    d.getString("type").orEmpty(), d.optionalNumber("review_id"), time)
            }

    private fun DocumentSnapshot.number(field: String): Int =
        Math.toIntExact(checkNotNull(getLong(field)) { "Falta $field en $id." })

    private fun DocumentSnapshot.optionalNumber(field: String): Int? = getLong(field)?.let(Math::toIntExact)

    private fun DocumentSnapshot.dateText(field: String): String? = when (val value = get(field)) {
        is Timestamp -> value.toDate().toInstant().toString()
        is String -> value
        else -> null
    }
}
