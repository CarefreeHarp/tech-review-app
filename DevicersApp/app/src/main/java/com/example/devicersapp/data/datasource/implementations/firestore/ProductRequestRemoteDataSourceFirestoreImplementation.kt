package com.example.devicersapp.data.datasource.implementations.firestore
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.firestore.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.firestore.*
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Implementa las operaciones de solicitudes de productos con las rutas y campos de Firestore v9. */
class ProductRequestRemoteDataSourceFirestoreImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProductRequestRemoteDataSource {
    private val requests get() = firestore.collection("product_requests")
    override suspend fun getRequests(userId: Int): List<ProductRequestInfo> {
        require(userId > 0)
        return requests.whereEqualTo("user_id", userId).get(Source.SERVER).await().documents
            .map { it.toProductRequestFirestoreDto().toProductRequestInfo(it.id) }
    }
    override suspend fun submit(requestId: String, userId: Int, suggestion: ProductSuggestion): ProductRequestInfo {
        require(requestId.isNotBlank() && '/' !in requestId && userId > 0 && suggestion.productName.isNotBlank())
        val reference = requests.document(requestId)
        firestore.runTransaction { transaction ->
            check(transaction.get(firestore.collection("users").document(userId.toString())).exists())
            check(!transaction.get(reference).exists()) { "Ya existe una solicitud con ese ID." }
            transaction.set(reference, suggestion.toProductRequestFirestoreWriteDto(userId).fields())
        }.await()
        return reference.get(Source.SERVER).await().also { snapshot ->
            if (!snapshot.exists()) throw NoSuchElementException("No existe ${snapshot.reference.path}")
        }.toProductRequestFirestoreDto().toProductRequestInfo(requestId)
    }
}
