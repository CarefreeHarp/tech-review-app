package com.example.devicersapp.data.datasource.implementations.firestore
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.firestore.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.firestore.*
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Implementa las operaciones de categorías con las rutas y campos de Firestore v9. */
class CategoryRemoteDataSourceFirestoreImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : CategoryRemoteDataSource {
    override suspend fun getCategories(): List<CategoryInfo> =
        firestore.collection("categories").get(Source.SERVER).await().documents.map { it.toCategoryDto().toCategoryInfo() }
}
