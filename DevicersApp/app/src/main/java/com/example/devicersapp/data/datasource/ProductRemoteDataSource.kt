package com.example.devicersapp.data.datasource
import com.example.devicersapp.ui.models.*
/** Operaciones de artículos; devuelve modelos comunes y propaga los errores de la fuente seleccionada. */
interface ProductRemoteDataSource {
    suspend fun getProducts(): List<ProductInfo>
    suspend fun getProductById(productId: Int): ProductInfo
    suspend fun getStores(productId: Int): List<ArticleStoreInfo>
}
