package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.CommentRemoteDataSource
import com.example.devicersapp.data.dto.CommentDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CommentFirestoreDataSourceImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : CommentRemoteDataSource {

    override suspend fun getComments(): List<CommentDto> {

        return firestore
            .collection("comments")
            .get()
            .await()
            .documents
            .map { document ->

                CommentDto(
                    id = document.getLong("id")?.toInt()
                        ?: document.id.toInt(),

                    user_id = document.getLong("user_id")?.toInt()
                        ?: throw Exception("Comentario sin user_id"),

                    review_id = document.getLong("review_id")?.toInt()
                        ?: throw Exception("Comentario sin review_id"),

                    parent_comment_id = document.getLong("parent_comment_id")?.toInt(),

                    body = document.getString("body")
                        ?: "",

                    is_active = document.getBoolean("is_active")
                        ?: true,

                    createdAt = document.getTimestamp("createdAt")
                        ?.toDate()
                        ?.toInstant()
                        ?.toString()
                        ?: "",

                    updatedAt = document.getTimestamp("updatedAt")
                        ?.toDate()
                        ?.toInstant()
                        ?.toString()
                        ?: ""
                )
            }
    }
}