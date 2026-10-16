package com.example.devicersapp.data.datasource.implementations.firestore
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.firestore.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.firestore.*
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Implementa las operaciones de likes de reseñas con las rutas y campos de Firestore v9. */
class ReviewLikeRemoteDataSourceFirestoreImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : ReviewLikeRemoteDataSource {
    override suspend fun getReviewLikes(reviewId: Int?): List<ReviewLikeInfo> {
        val reviews = if (reviewId != null) listOf(firestore.collection("reviews").document(reviewId.toString()))
            else firestore.collection("reviews").get(Source.SERVER).await().documents.map { it.reference }
        return reviews.flatMap { review -> review.collection("likes").get(Source.SERVER).await().documents.map {
            it.toReviewLikeFirestoreDto().toReviewLikeInfo(review.id.toInt())
        } }
    }
}
