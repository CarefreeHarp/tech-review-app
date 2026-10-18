package com.example.devicersapp.data.datasource
import com.example.devicersapp.ui.models.*
import kotlinx.coroutines.flow.Flow
/** Operaciones de artículos; devuelve modelos comunes y propaga los errores de la fuente seleccionada. */
interface ProductRemoteDataSource {
    suspend fun getProducts(): List<ProductInfo>
    /** Catálogo en vivo: a diferencia de [getProducts], emite una lista nueva cada vez que cambian los artículos. */
    fun listenProducts(): Flow<List<ProductInfo>>
    suspend fun getProductById(productId: Int): ProductInfo
    suspend fun getStores(productId: Int): List<ArticleStoreInfo>
}
