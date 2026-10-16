package com.example.devicersapp.data.dto.ResponseDTO.shared
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.firestore.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
/** Campos de tiendas en ambas fuentes; los IDs de los padres se reciben desde la ruta. */
data class StoreDto(
    val id: Int,
    val name: String,
    val website_url: String?
)
/** Decodifica el documento sin consultas implícitas a otras colecciones. */
internal fun DocumentSnapshot.toStoreDto(): StoreDto {
    check(integer("id") == numericDocumentId())
    return checkNotNull(data).toStoreDto()
}
internal fun Map<String, Any?>.toStoreDto(): StoreDto = StoreDto(
    id = integer("id"),
    name = text("name"),
    website_url = getString("website_url")
)
/** Traduce el transporte al modelo común conservando campos y referencias. */
fun StoreDto.toStoreInfo(): StoreInfo = StoreInfo(
    id = id,
    name = name,
    websiteUrl = website_url
)
