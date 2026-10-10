package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.OwnProfileRemoteDataSource
import com.example.devicersapp.ui.models.ProductInfo
import com.example.devicersapp.ui.models.ReviewInfo
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Lee el perfil propio aprovechando los campos planos de las reseñas en Firestore. */
class OwnProfileFirestoreDataSourceImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : OwnProfileRemoteDataSource {
    override suspend fun getReviewsByUser(userId: Int): List<ReviewInfo> {
        require(userId > 0)
        // Filtra por autor en el servidor; el estado y orden se resuelven sin índice compuesto.
        return firestore.collection("reviews").whereEqualTo("user_id", userId)
            .get(Source.SERVER).await().documents
            .filter { it.getBoolean("is_active") == true }
            .map { document ->
                check(document.number("user_id") == userId)
                ReviewInfo(
                    id = document.number("id"), userId = userId,
                    articleId = document.number("article_id"), rating = document.number("rating"),
                    title = document.getString("title"), body = document.getString("body").orEmpty(),
                    isActive = true, createdAt = document.dateText("createdAt"),
                    updatedAt = document.dateText("updatedAt"),
                    article = ProductInfo(
                        id = document.number("article_id"),
                        categoryId = document.number("article_category_id"),
                        brandId = document.number("article_brand_id"),
                        name = checkNotNull(document.getString("article_name")),
                        model = document.getString("article_model"),
                        description = document.getString("article_description"),
                        imageUrl = document.getString("article_image_url"),
                        releaseDate = document.dateText("article_release_date"),
                        specifications = null, isActive = document.getBoolean("article_is_active") ?: true,
                        createdAt = document.dateText("article_createdAt"),
                        updatedAt = document.dateText("article_updatedAt")
                    )
                )
            }.sortedByDescending { it.createdAt }
    }

    override suspend fun getFollowCounts(userId: Int): Pair<Int, Int> = coroutineScope {
        require(userId > 0)
        val follows = firestore.collection("follows")
        val followers = async {
            follows.whereEqualTo("followed_id", userId).count().get(AggregateSource.SERVER).await().count
        }
        val following = async {
            follows.whereEqualTo("follower_id", userId).count().get(AggregateSource.SERVER).await().count
        }
        Math.toIntExact(followers.await()) to Math.toIntExact(following.await())
    }

    override suspend fun deleteReview(reviewId: Int, userId: Int) {
        require(reviewId > 0 && userId > 0)
        val reference = firestore.collection("reviews").document(reviewId.toString())
        firestore.runTransaction { transaction ->
            val review = transaction.get(reference)
            check(review.exists() && review.number("user_id") == userId) {
                "La reseña no pertenece al usuario actual."
            }
            // Conserva las referencias existentes y excluye la reseña de las consultas activas.
            transaction.update(reference, mapOf("is_active" to false, "updatedAt" to FieldValue.serverTimestamp()))
        }.await()
    }

    private fun DocumentSnapshot.number(field: String): Int =
        Math.toIntExact(checkNotNull(getLong(field)) { "Falta el campo $field." })

    private fun DocumentSnapshot.dateText(field: String): String = when (val value = get(field)) {
        is com.google.firebase.Timestamp -> value.toDate().toInstant().toString()
        is String -> value
        else -> ""
    }
}
