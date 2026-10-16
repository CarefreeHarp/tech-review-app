package com.example.devicersapp.data.datasource.implementations.firestore
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.firestore.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.firestore.*
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Implementa las operaciones de tiendas con las rutas y campos de Firestore v9. */
class StoreRemoteDataSourceFirestoreImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : StoreRemoteDataSource {
    override suspend fun getStores(): List<StoreInfo> =
        firestore.collection("stores").get(Source.SERVER).await().documents.map { it.toStoreDto().toStoreInfo() }
}
