package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.ReviewRemoteDataSource
import com.example.devicersapp.data.dto.CreateReviewRequestDto
import com.example.devicersapp.data.dto.ProductDto
import com.example.devicersapp.data.dto.ReviewDto
import com.example.devicersapp.data.dto.UpdateReviewRequestDto
import com.example.devicersapp.data.dto.UserDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReviewFirestoreDataSourceImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : ReviewRemoteDataSource {

    private val reviews = firestore.collection("reviews")

    override suspend fun getReviews(): List<ReviewDto> {
        println("AAAAAA FIRESTORE REVIEWS")

        return reviews
            .get(Source.SERVER)
            .await()
            .documents
            .filter {
                it.getBoolean("is_active") == true
            }
            .map {
                it.toReviewDto()
            }
    }

    override suspend fun getReviewById(reviewId: Int): ReviewDto {
        val document = reviews
            .document(reviewId.toString())
            .get(Source.SERVER)
            .await()

        if (!document.exists()) {
            throw NoSuchElementException(
                "No existe la review $reviewId"
            )
        }

        return document.toReviewDto()
    }

    override suspend fun getReviewsByUser(userId: Int): List<ReviewDto> {
        return reviews
            .whereEqualTo("user_id", userId)
            .get(Source.SERVER)
            .await()
            .documents
            .filter {
                it.getBoolean("is_active") == true
            }
            .map {
                it.toReviewDto()
            }
    }

    override suspend fun getReviewsByProduct(productId: Int): List<ReviewDto> {
        return reviews
            .whereEqualTo("article_id", productId)
            .get(Source.SERVER)
            .await()
            .documents
            .filter {
                it.getBoolean("is_active") == true
            }
            .map {
                it.toReviewDto()
            }
    }

    override suspend fun createReview(
        request: CreateReviewRequestDto
    ): ReviewDto {

        require(request.rating in 1..5) {
            "La calificación debe estar entre 1 y 5"
        }

        require(request.body.isNotBlank()) {
            "La reseña no puede estar vacía"
        }

        val user = firestore
            .collection("users")
            .document(request.userId.toString())
            .get(Source.SERVER)
            .await()

        require(user.exists()) {
            "No se encontró el usuario"
        }

        val article = firestore
            .collection("articles")
            .document(request.articleId.toString())
            .get(Source.SERVER)
            .await()

        require(article.exists()) {
            "No se encontró el artículo"
        }

        val maxExistingId = reviews
            .get(Source.SERVER)
            .await()
            .documents
            .mapNotNull { it.id.toIntOrNull() }
            .maxOrNull() ?: 0

        val counterRef = firestore
            .collection("counters")
            .document("reviews")

        val newReviewId = firestore.runTransaction { transaction ->

            val counter = transaction.get(counterRef)

            val nextId = maxOf(
                maxExistingId + 1,
                counter.getLong("next_id")?.toInt() ?: 1
            )

            val now = com.google.firebase.Timestamp.now()

            val data = mapOf(
                "id" to nextId,
                "user_id" to request.userId,
                "article_id" to request.articleId,
                "rating" to request.rating,
                "title" to request.title,
                "body" to request.body,
                "is_active" to true,
                "createdAt" to now,
                "updatedAt" to now,

                "user_username" to user.getString("username"),

                "article_name" to article.getString("name"),
                "article_model" to article.getString("model"),
                "article_description" to article.getString("description"),
                "article_image_url" to article.getString("image_url"),
                "article_brand_id" to article.getLong("brand_id"),
                "article_brand_name" to article.getString("brand_name"),
                "article_category_id" to article.getLong("category_id"),
                "article_category_name" to article.getString("category_name"),
                "article_specifications" to article.get("specifications"),
                "article_release_date" to article.get("release_date")
            )

            transaction.set(
                reviews.document(nextId.toString()),
                data
            )

            transaction.set(
                counterRef,
                mapOf("next_id" to nextId + 1)
            )

            nextId
        }.await()

        return getReviewById(newReviewId)
    }

    override suspend fun updateReview(
        reviewId: Int,
        request: UpdateReviewRequestDto
    ): ReviewDto {

        val updates = mutableMapOf<String, Any>()

        request.rating?.let {
            updates["rating"] = it
        }

        request.title?.let {
            updates["title"] = it
        }

        request.body?.let {
            updates["body"] = it
        }

        request.is_active?.let {
            updates["is_active"] = it
        }

        request.user_id?.let {
            updates["user_id"] = it
        }

        request.article_id?.let {
            updates["article_id"] = it
        }

        updates["updatedAt"] =
            com.google.firebase.Timestamp.now()

        reviews
            .document(reviewId.toString())
            .update(updates)
            .await()

        return getReviewById(reviewId)
    }

    override suspend fun deleteReview(
        reviewId: Int
    ) {

        reviews
            .document(reviewId.toString())
            .update(
                "is_active",
                false
            )
            .await()
    }


    private suspend fun DocumentSnapshot.toReviewDto(): ReviewDto {

        val userId =
            getLong("user_id")?.toInt()
                ?: throw IllegalStateException(
                    "Review sin user_id"
                )

        val articleId =
            getLong("article_id")?.toInt()
                ?: throw IllegalStateException(
                    "Review sin article_id"
                )

        val userDocument =
            firestore
                .collection("users")
                .document(userId.toString())
                .get(Source.SERVER)
                .await()


        val articleDocument =
            firestore
                .collection("articles")
                .document(articleId.toString())
                .get(Source.SERVER)
                .await()


        return ReviewDto(
            id = getLong("id")?.toInt()
                ?: id.toInt(),

            user_id = userId,

            article_id = articleId,

            rating = getLong("rating")?.toInt()
                ?: 0,

            title = getString("title"),

            body = getString("body")
                ?: "",

            is_active = getBoolean("is_active")
                ?: false,

            createdAt =
                getTimestamp("createdAt")
                    ?.toDate()
                    ?.toInstant()
                    ?.toString()
                    ?: "",

            updatedAt =
                getTimestamp("updatedAt")
                    ?.toDate()
                    ?.toInstant()
                    ?.toString()
                    ?: "",

            user = userDocument.toUserDto(),

            article = articleDocument.toProductDto()
        )
    }


    private fun DocumentSnapshot.toUserDto(): UserDto {

        return UserDto(
            id = id.toInt(),
            email = getString("email").orEmpty(),
            username = getString("username").orEmpty(),
            firebase_uid = getString("firebase_uid"),
            biography = getString("biography"),
            profile_image_url =
                getString("profile_image_url"),
            notifications_last_viewed_at =
                getString("notifications_last_viewed_at"),
            is_active =
                getBoolean("is_active") ?: true,
            createdAt =
                getTimestamp("createdAt")
                    ?.toDate()
                    ?.toInstant()
                    ?.toString()
                    ?: "",
            updatedAt =
                getTimestamp("updatedAt")
                    ?.toDate()
                    ?.toInstant()
                    ?.toString()
                    ?: ""
        )
    }


    private fun DocumentSnapshot.toProductDto(): ProductDto {

        return ProductDto(
            id = id.toInt(),

            category_id =
                getLong("category_id")?.toInt()
                    ?: 0,

            brand_id =
                getLong("brand_id")?.toInt()
                    ?: 0,

            name =
                getString("name")
                    ?: "",

            model =
                getString("model"),

            description =
                getString("description"),

            image_url =
                getString("image_url"),

            release_date =
                getString("release_date"),

            specifications = null,

            is_active =
                getBoolean("is_active")
                    ?: true,

            createdAt =
                getTimestamp("createdAt")
                    ?.toDate()
                    ?.toInstant()
                    ?.toString()
                    ?: "",

            updatedAt =
                getTimestamp("updatedAt")
                    ?.toDate()
                    ?.toInstant()
                    ?.toString()
                    ?: ""
        )
    }
}