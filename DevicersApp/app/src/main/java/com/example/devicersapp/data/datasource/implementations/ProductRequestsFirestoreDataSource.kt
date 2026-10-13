package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.ProductRequestsRemoteDataSource
import com.example.devicersapp.data.datasource.ProductSuggestion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductRequestsFirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ProductRequestsRemoteDataSource {
    override suspend fun submit(requestId: String, userId: Int, firebaseUid: String, suggestion: ProductSuggestion) {
        check(auth.currentUser?.uid == firebaseUid) { "La sesión cambió." }
        val ref = firestore.collection("product_requests").document(requestId)
        // El mismo intento conserva ID al reintentar: no duplica una escritura cuyo resultado se perdió.
        firestore.runTransaction { transaction ->
            val profile = transaction.get(firestore.collection("users").document(userId.toString()))
            check(profile.getString("firebase_uid") == firebaseUid && profile.getBoolean("is_active") == true)
            val existing = transaction.get(ref)
            if (existing.exists()) {
                check(existing.getString("firebase_uid") == firebaseUid)
            } else {
                transaction.set(ref, mapOf(
                    "user_id" to userId, "firebase_uid" to firebaseUid,
                    "product_name" to suggestion.productName, "category" to suggestion.category,
                    "brand" to suggestion.brand, "release_date" to suggestion.releaseDate,
                    "status" to "pending", "createdAt" to FieldValue.serverTimestamp()
                ))
            }
        }.await()
    }
}
