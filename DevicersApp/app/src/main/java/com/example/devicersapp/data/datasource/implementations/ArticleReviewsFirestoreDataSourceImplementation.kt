package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.ArticleReviewsRemoteDataSource
import com.example.devicersapp.data.dto.ArticleDocumentDto
import com.example.devicersapp.data.dto.CommentDocumentDto
import com.example.devicersapp.data.dto.ReviewDocumentDto
import com.example.devicersapp.data.dto.UserSummaryDto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Source
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

/** Lee artículos y reseñas desde Firestore aprovechando las copias desnormalizadas de cada reseña. */
class ArticleReviewsFirestoreDataSourceImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : ArticleReviewsRemoteDataSource {
    private val reviews get() = firestore.collection("reviews")

    override suspend fun getActiveReviews(): List<ReviewDocumentDto> = fromServer {
        reviews.whereEqualTo("is_active", true).get(Source.SERVER).await()
            .documents.map { it.toReviewDocument() }
    }

    override suspend fun getActiveReviewsByArticle(articleId: Int): List<ReviewDocumentDto> = fromServer {
        // Filtra por artículo en el servidor y por estado aquí, para no exigir un índice compuesto.
        reviews.whereEqualTo("article_id", articleId).get(Source.SERVER).await()
            .documents.filter { it.getBoolean("is_active") == true }
            .map { it.toReviewDocument() }
    }

    override suspend fun getReviewById(reviewId: Int): ReviewDocumentDto = fromServer {
        val document = reviews.document(reviewId.toString()).get(Source.SERVER).await()
        if (!document.exists()) throw NoSuchElementException("No existe la reseña $reviewId.")
        document.toReviewDocument()
    }

    override suspend fun getArticleById(articleId: Int): ArticleDocumentDto = fromServer {
        val document = firestore.collection("articles").document(articleId.toString())
            .get(Source.SERVER).await()
        if (!document.exists()) throw NoSuchElementException("No existe el artículo $articleId.")
        document.data.orEmpty().toArticleDocument(prefix = "")
    }

    override suspend fun getReviewLikeCounts(reviewIds: Set<Int>): Map<Int, Int> = fromServer {
        countByField("review_likes", "review_id", reviewIds)
    }

    override suspend fun getCommentLikeCounts(commentIds: Set<Int>): Map<Int, Int> = fromServer {
        countByField("comment_likes", "comment_id", commentIds)
    }

    override suspend fun getFollowedUserIds(followerId: Int): Set<Int> = fromServer {
        firestore.collection("follows").whereEqualTo("follower_id", followerId).get(Source.SERVER).await()
            .documents.map { it.data.orEmpty().int("followed_id") }.toSet()
    }

    override suspend fun getUsers(userIds: Set<Int>): Map<Int, UserSummaryDto> = fromServer {
        coroutineScope {
            // Los documentos users usan el ID numérico como nombre; whereIn se consulta por lotes de 10.
            userIds.chunked(10).map { batch ->
                async {
                    firestore.collection("users").whereIn(FieldPath.documentId(), batch.map(Int::toString))
                        .get(Source.SERVER).await().documents
                }
            }.awaitAll().flatten().associate { document ->
                val id = document.id.toInt()
                id to UserSummaryDto(
                    id = id,
                    username = document.getString("username").orEmpty(),
                    profileImageUrl = document.getString("profile_image_url")?.trim()?.takeIf { it.isNotEmpty() }
                )
            }
        }
    }

    /** Agrupa por el campo indicado los documentos que lo referencian; whereIn no admite listas vacías. */
    private suspend fun countByField(collection: String, field: String, ids: Set<Int>): Map<Int, Int> =
        coroutineScope {
            ids.chunked(10).map { batch ->
                async {
                    firestore.collection(collection).whereIn(field, batch).get(Source.SERVER).await().documents
                }
            }.awaitAll().flatten().groupingBy { it.data.orEmpty().int(field) }.eachCount()
        }

    /** Traduce la falta de conexión a [IOException] para que las capas superiores no dependan de Firebase. */
    private suspend fun <T> fromServer(block: suspend () -> T): T =
        try {
            block()
        } catch (exception: FirebaseFirestoreException) {
            if (exception.code == FirebaseFirestoreException.Code.UNAVAILABLE) {
                throw IOException("No hay conexión con Firestore.", exception)
            }
            throw exception
        }

    private fun DocumentSnapshot.toReviewDocument(): ReviewDocumentDto {
        val fields = data.orEmpty()
        return ReviewDocumentDto(
            id = fields.int("id"),
            userId = fields.int("user_id"),
            rating = fields.int("rating"),
            title = fields["title"] as? String,
            body = fields["body"] as? String ?: "",
            isActive = fields["is_active"] as? Boolean ?: false,
            createdAt = fields.dateText("createdAt").orEmpty(),
            updatedAt = fields.dateText("updatedAt").orEmpty(),
            userUsername = fields["user_username"] as? String ?: "",
            // La reseña guarda el artículo con los mismos nombres de campo antecedidos por "article_".
            article = fields.toArticleDocument(prefix = "article_"),
            comments = (fields["comments"] as? List<*>).orEmpty().flatMap { it.flattenComments() }
        )
    }

    private fun Map<String, Any?>.toArticleDocument(prefix: String) = ArticleDocumentDto(
        id = int("${prefix}id"),
        name = checkNotNull(this["${prefix}name"] as? String) { "El artículo no tiene nombre." },
        model = this["${prefix}model"] as? String,
        description = this["${prefix}description"] as? String,
        imageUrl = this["${prefix}image_url"] as? String,
        brandId = int("${prefix}brand_id"),
        brandName = this["${prefix}brand_name"] as? String ?: "",
        categoryId = int("${prefix}category_id"),
        categoryName = this["${prefix}category_name"] as? String ?: "",
        releaseDate = dateText("${prefix}release_date"),
        specifications = (this["${prefix}specifications"] as? Map<*, *>)
            ?.entries?.associate { (key, value) -> key.toString() to value },
        isActive = this["${prefix}is_active"] as? Boolean ?: true,
        createdAt = dateText("${prefix}createdAt").orEmpty(),
        updatedAt = dateText("${prefix}updatedAt").orEmpty()
    )

    /** Recorre el árbol de comentarios copiado en la reseña y lo aplana conservando el padre de cada uno. */
    private fun Any?.flattenComments(): List<CommentDocumentDto> {
        val node = (this as? Map<*, *>)?.entries?.associate { (key, value) -> key.toString() to value }
            ?: return emptyList()
        val comment = CommentDocumentDto(
            id = node.int("id"),
            userId = node.int("user_id"),
            userUsername = node["user_username"] as? String ?: "",
            parentCommentId = (node["parent_comment_id"] as? Number)?.let { Math.toIntExact(it.toLong()) },
            body = node["body"] as? String ?: "",
            isActive = node["is_active"] as? Boolean ?: false,
            createdAt = node.dateText("createdAt").orEmpty()
        )
        return listOf(comment) + (node["replies"] as? List<*>).orEmpty().flatMap { it.flattenComments() }
    }

    /** Firestore entrega los enteros como Long; el modelo de la app conserva IDs Int. */
    private fun Map<String, Any?>.int(field: String): Int =
        Math.toIntExact(checkNotNull(this[field] as? Number) { "Falta el campo $field." }.toLong())

    private fun Map<String, Any?>.dateText(field: String): String? = when (val value = this[field]) {
        is Timestamp -> value.toDate().toInstant().toString()
        is String -> value
        else -> null
    }
}
