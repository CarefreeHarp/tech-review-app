package com.example.devicersapp.data.datasource.implementations.firestore
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.firestore.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.firestore.*
import com.google.firebase.firestore.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Implementa las operaciones de artículos con las rutas y campos de Firestore v9. */
class ProductRemoteDataSourceFirestoreImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProductRemoteDataSource {
    private val articles get() = firestore.collection("articles")
    override suspend fun getProducts(): List<ProductInfo> =
        articles.get(Source.SERVER).await().documents.map { it.toProductFirestoreDto().toProductInfo() }

    /**
     * Convierte el listener de la colección de artículos en un Flow: emite el catálogo al suscribirse
     * y otra vez cada vez que Firestore detecta un artículo agregado, modificado o eliminado.
     */
    override fun listenProducts(): Flow<List<ProductInfo>> = callbackFlow {
        val listener = articles.addSnapshotListener { snapshot, error ->
            // Un error del listener cierra el flujo; el ViewModel lo recibe en su catch.
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                // Un documento corrupto también cierra el flujo en vez de lanzar la excepción en el hilo principal.
                runCatching { snapshot.documents.map { it.toProductFirestoreDto().toProductInfo() } }
                    .onSuccess { products -> trySend(products) }
                    .onFailure { close(it) }
            }
        }
        // Cuando nadie recolecta el flujo (pantalla destruida o búsqueda reemplazada), se retira el listener.
        awaitClose { listener.remove() }
    }
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
