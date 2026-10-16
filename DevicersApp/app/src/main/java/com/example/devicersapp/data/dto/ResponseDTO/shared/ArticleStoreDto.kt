package com.example.devicersapp.data.dto.ResponseDTO.shared
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.firestore.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
/** Campos de tiendas de artículos en ambas fuentes; los IDs de los padres se reciben desde la ruta. */
data class ArticleStoreDto(
    val store_id: Int,
    val name: String?,
    val website_url: String?,
    val product_url: String?
)
/** Decodifica el documento sin consultas implícitas a otras colecciones. */
internal fun DocumentSnapshot.toArticleStoreDto(): ArticleStoreDto {
    return checkNotNull(data).toArticleStoreDto()
}
internal fun Map<String, Any?>.toArticleStoreDto(): ArticleStoreDto = ArticleStoreDto(
    store_id = integer("store_id"),
    name = getString("name"),
    website_url = getString("website_url"),
    product_url = getString("product_url")
)
/** Traduce el transporte al modelo común conservando campos y referencias. */
fun ArticleStoreDto.toArticleStoreInfo(): ArticleStoreInfo = ArticleStoreInfo(
    storeId = store_id,
    name = name,
    websiteUrl = website_url,
    productUrl = product_url
)
