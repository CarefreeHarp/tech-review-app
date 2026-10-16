package com.example.devicersapp.data.datasource.implementations.firestore
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.firestore.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.firestore.*
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Implementa las operaciones de artículos con las rutas y campos de Firestore v9. */
class ProductRemoteDataSourceFirestoreImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProductRemoteDataSource {
    private val articles get() = firestore.collection("articles")
    override suspend fun getProducts(): List<ProductInfo> =
        articles.get(Source.SERVER).await().documents.map { it.toProductFirestoreDto().toProductInfo() }
    override suspend fun getProductById(productId: Int): ProductInfo {
        require(productId > 0)
        return articles.document(productId.toString()).get(Source.SERVER).await().also { snapshot ->
            if (!snapshot.exists()) throw NoSuchElementException("No existe ${snapshot.reference.path}")
        }.toProductFirestoreDto().toProductInfo()
            .copy(stores = getStores(productId))
    }
    override suspend fun getStores(productId: Int): List<ArticleStoreInfo> {
        require(productId > 0)
        return articles.document(productId.toString()).collection("stores").get(Source.SERVER).await().documents
            .map { it.toArticleStoreDto().toArticleStoreInfo() }
    }
}
