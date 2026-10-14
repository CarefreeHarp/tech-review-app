package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.ReviewLikeRemoteDataSource
import com.example.devicersapp.data.dto.ReviewLikeDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReviewLikeFirestoreDataSourceImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : ReviewLikeRemoteDataSource {

    override suspend fun getReviewLikes(): List<ReviewLikeDto> {

        return firestore
            .collection("review_likes")
            .get()
            .await()
            .documents
            .map { document ->

                ReviewLikeDto(
                    id = document.getLong("id")?.toInt()
                        ?: document.id.toInt(),

                    user_id = document.getLong("user_id")?.toInt()
                        ?: throw Exception("Like de reseña sin user_id"),

                    review_id = document.getLong("review_id")?.toInt()
                        ?: throw Exception("Like de reseña sin review_id")
                )
            }
    }
}