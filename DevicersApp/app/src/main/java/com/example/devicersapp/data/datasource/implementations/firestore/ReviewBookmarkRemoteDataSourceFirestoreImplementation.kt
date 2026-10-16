package com.example.devicersapp.data.datasource.implementations.firestore
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.firestore.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.firestore.*
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Implementa las operaciones de reseñas guardadas con las rutas y campos de Firestore v9. */
class ReviewBookmarkRemoteDataSourceFirestoreImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : ReviewBookmarkRemoteDataSource {
    override suspend fun getReviewBookmarks(userId: Int): List<ReviewBookmarkInfo> {
        require(userId > 0)
        return firestore.collection("users").document(userId.toString()).collection("review_bookmarks").get(Source.SERVER).await().documents
            .map { it.toReviewBookmarkFirestoreDto().toReviewBookmarkInfo(userId) }
    }
}
