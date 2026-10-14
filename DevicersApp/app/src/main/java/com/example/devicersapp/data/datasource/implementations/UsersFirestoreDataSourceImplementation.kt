package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.UsersRemoteDataSource
import com.example.devicersapp.data.dto.UserDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UsersFirestoreDataSourceImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : UsersRemoteDataSource {

    override suspend fun getUsers(
        excludeUserId: Int?
    ): List<UserDto> {

        var query: com.google.firebase.firestore.Query = firestore.collection("users")

        if (excludeUserId != null) {
            query = query.whereNotEqualTo("id", excludeUserId)
        }

        return query
            .get()
            .await()
            .documents
            .map { document ->

                UserDto(
                    id = document.getLong("id")!!.toInt(),

                    email = document.getString("email") ?: "",

                    username = document.getString("username") ?: "",

                    firebase_uid = document.getString("firebase_uid"),

                    biography = document.getString("biography"),

                    profile_image_url = document.getString("profile_image_url"),

                    notifications_last_viewed_at =
                        document.getString("notifications_last_viewed_at"),

                    is_active = document.getBoolean("is_active") ?: true,

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


    override suspend fun getUserById(userId: Int): UserDto {

        val document = firestore
            .collection("users")
            .whereEqualTo("id", userId)
            .get()
            .await()
            .documents
            .firstOrNull()
            ?: throw Exception("Usuario no encontrado")

        return UserDto(
            id = document.getLong("id")!!.toInt(),

            email = document.getString("email") ?: "",

            username = document.getString("username") ?: "",

            firebase_uid = document.getString("firebase_uid"),

            biography = document.getString("biography"),

            profile_image_url = document.getString("profile_image_url"),

            notifications_last_viewed_at =
                document.getString("notifications_last_viewed_at"),

            is_active = document.getBoolean("is_active") ?: true,

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