package com.example.devicersapp.data.local

import android.net.Uri
import com.example.devicersapp.data.datasource.OwnProfileRemoteDataSource
import com.example.devicersapp.data.datasource.ProfileRemoteDataSource
import com.example.devicersapp.data.datasource.UserProfileRemoteDataSource
import com.example.devicersapp.data.repository.EditProfileRepository
import com.example.devicersapp.data.repository.OwnProfileRepository
import com.example.devicersapp.data.repository.ProfileRepository
import com.example.devicersapp.data.repository.SessionRepository
import com.example.devicersapp.ui.models.ReviewInfo
import com.example.devicersapp.ui.models.UserInfo
import kotlinx.coroutines.runBlocking

/** Proporciona datos locales exclusivamente a las vistas previas, sin inicializar Firebase. */
class ProfilePreviewProvider(username: String, biography: String, email: String) {
    private val user = UserInfo(1, email, username, "preview", biography, null, null, true, "", "")
    private val profiles = object : UserProfileRemoteDataSource {
        override suspend fun getById(userId: Int) = user
        override suspend fun findByFirebaseUid(firebaseUid: String) = user
        override suspend fun createIfMissing(firebaseUid: String, email: String, username: String, profileImageUrl: String?) = user
        override suspend fun updateProfile(userId: Int, firebaseUid: String, username: String?, profileImageUrl: String?, biography: String?) = user
    }
    val session = SessionRepository(profiles).also {
        runBlocking { it.loadProfile("preview", email, username, null) }
    }
    val ownProfile = OwnProfileRepository(session, object : OwnProfileRemoteDataSource {
        override suspend fun getReviewsByUser(userId: Int) = emptyList<ReviewInfo>()
        override suspend fun getFollowCounts(userId: Int) = 0 to 0
        override suspend fun deleteReview(reviewId: Int, userId: Int) = Unit
    })
    val publicProfile = ProfileRepository(object : ProfileRemoteDataSource {
        override suspend fun getUser(userId: Int) = user
        override suspend fun getReviews(userId: Int) = emptyList<ReviewInfo>()
        override suspend fun getFollowCounts(userId: Int) = 0 to 0
        override suspend fun isFollowing(followerId: Int, followedId: Int) = false
    })
    val editor = object : EditProfileRepository {
        override suspend fun getProfile() = user
        override suspend fun saveProfile(username: String, biography: String, photo: Uri?) = Unit
    }
}
