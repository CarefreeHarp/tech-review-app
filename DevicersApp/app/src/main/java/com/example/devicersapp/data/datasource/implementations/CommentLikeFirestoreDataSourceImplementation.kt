package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.CommentLikeRemoteDataSource
import com.example.devicersapp.data.dto.CommentLikeDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CommentLikeFirestoreDataSourceImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : CommentLikeRemoteDataSource {

    override suspend fun getCommentLikes(): List<CommentLikeDto> {

        return firestore
            .collection("comment_likes")
            .get()
            .await()
            .documents
            .map { document ->

                CommentLikeDto(
                    id = document.getLong("id")?.toInt()
                        ?: document.id.toInt(),

                    user_id = document.getLong("user_id")?.toInt()
                        ?: throw Exception("Comment like sin user_id"),

                    comment_id = document.getLong("comment_id")?.toInt()
                        ?: throw Exception("Comment like sin comment_id")
                )
            }
    }
}