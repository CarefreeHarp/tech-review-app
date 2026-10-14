package com.example.devicersapp.data.datasource.implementations

import android.util.Log
import com.example.devicersapp.data.datasource.ReviewBookmarkRemoteDataSource
import com.example.devicersapp.data.dto.ReviewBookmarkDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReviewBookmarkFirestoreDataSourceImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : ReviewBookmarkRemoteDataSource {

    override suspend fun getReviewBookmarks(): List<ReviewBookmarkDto> {

        println("AAAAAA FIRESTORE BOOKMARKS")

        return firestore
            .collection("review_bookmarks")
            .get(Source.SERVER)
            .await()
            .documents
            .map { document ->

                val userId = document.getLong("review_user_id")?.toInt()
                    ?: throw IllegalStateException(
                        "Bookmark sin review_user_id"
                    )

                val reviewId = document.getLong("review_id")?.toInt()
                    ?: throw IllegalStateException(
                        "Bookmark sin review_id"
                    )

                ReviewBookmarkDto(
                    user_id = userId,
                    review_id = reviewId
                )
            }
    }
}