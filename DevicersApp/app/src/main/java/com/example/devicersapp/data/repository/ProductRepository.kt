package com.example.devicersapp.data.repository
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*

/** Consulta el catálogo y las tiendas de artículos; no mezcla implementaciones ni datos de muestra. */
class ProductRepository(private val source: ProductRemoteDataSource) {
    suspend fun getProducts(includeInactive: Boolean = false): Result<List<ProductInfo>> = repositoryResult {
        source.getProducts().filter { includeInactive || it.isActive }
    }
    suspend fun getProductById(productId: Int): Result<ProductInfo> = repositoryResult { source.getProductById(productId) }
    suspend fun getStores(productId: Int): List<ArticleStoreInfo> = source.getStores(productId)
}
