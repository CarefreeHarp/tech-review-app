package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.OwnProfileRemoteDataSource
import com.example.devicersapp.data.datasource.ProfileRemoteDataSource
import com.example.devicersapp.data.datasource.UserProfileRemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Reutiliza las consultas Firestore de identidad, reseñas y métricas para cualquier perfil. */
class ProfileFirestoreDataSource @Inject constructor(
    private val profiles: UserProfileRemoteDataSource,
    private val profileContent: OwnProfileRemoteDataSource,
    private val firestore: FirebaseFirestore
) : ProfileRemoteDataSource {
    override suspend fun getUser(userId: Int) = profiles.getById(userId)
    override suspend fun getReviews(userId: Int) = profileContent.getReviewsByUser(userId)
    override suspend fun getFollowCounts(userId: Int) = profileContent.getFollowCounts(userId)

    override suspend fun isFollowing(followerId: Int, followedId: Int): Boolean =
        // Un filtro en servidor evita exigir un índice compuesto para esta relación.
        firestore.collection("follows").whereEqualTo("follower_id", followerId)
            .get(Source.SERVER).await().documents.any { it.getLong("followed_id") == followedId.toLong() }
}
