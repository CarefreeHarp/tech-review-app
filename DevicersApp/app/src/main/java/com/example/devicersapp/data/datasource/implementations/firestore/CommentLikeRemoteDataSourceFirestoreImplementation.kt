package com.example.devicersapp.data.datasource.implementations.firestore
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.firestore.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.firestore.*
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Implementa las operaciones de likes de comentarios con las rutas y campos de Firestore v9. */
class CommentLikeRemoteDataSourceFirestoreImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : CommentLikeRemoteDataSource {
    override suspend fun getCommentLikes(reviewId: Int?, commentId: Int?): List<CommentLikeInfo> {
        val reviews = if (reviewId != null) listOf(firestore.collection("reviews").document(reviewId.toString()))
            else firestore.collection("reviews").get(Source.SERVER).await().documents.map { it.reference }
        return reviews.flatMap { review ->
            val comments = if (commentId != null && reviewId != null)
                listOf(review.collection("comments").document(commentId.toString()))
            else review.collection("comments").get(Source.SERVER).await().documents.filter { commentId == null || it.id == commentId.toString() }.map { it.reference }
            comments.flatMap { comment -> comment.collection("likes").get(Source.SERVER).await().documents.map {
                it.toCommentLikeFirestoreDto().toCommentLikeInfo(review.id.toInt(), comment.id.toInt())
            } }
        }
    }
}
