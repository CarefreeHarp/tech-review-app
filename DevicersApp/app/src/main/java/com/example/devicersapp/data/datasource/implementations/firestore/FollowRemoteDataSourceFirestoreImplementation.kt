package com.example.devicersapp.data.datasource.implementations.firestore
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.firestore.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.firestore.*
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Implementa las operaciones de seguimientos con las rutas y campos de Firestore v9. */
class FollowRemoteDataSourceFirestoreImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : FollowRemoteDataSource {
    override suspend fun getFollowers(userId: Int): List<FollowInfo> {
        require(userId > 0)
        return firestore.collection("users").document(userId.toString()).collection("followers").get(Source.SERVER).await().documents
            .map { it.toFollowerFirestoreDto().toFollowInfo(userId) }
    }
    override suspend fun getFollowing(userId: Int): List<FollowInfo> {
        require(userId > 0)
        return firestore.collection("users").document(userId.toString()).collection("follows").get(Source.SERVER).await().documents
            .map { it.toFollowFirestoreDto().toFollowInfo(userId) }
    }
}
