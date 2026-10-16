package com.example.devicersapp.data.dto.ResponseDTO.firestore
import com.example.devicersapp.ui.models.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
/** Campos de solicitudes de productos en Firestore v9; los IDs de los padres se reciben desde la ruta. */
data class ProductRequestFirestoreDto(
    val user_id: Int,
    val product_name: String,
    val image_url: String?,
    val category: String?,
    val brand: String?,
    val release_date: String?,
    val status: String,
    val createdAt: Timestamp?
)
/** Decodifica el documento sin consultas implícitas a otras colecciones. */
internal fun DocumentSnapshot.toProductRequestFirestoreDto(): ProductRequestFirestoreDto {
    return checkNotNull(data).toProductRequestFirestoreDto()
}
internal fun Map<String, Any?>.toProductRequestFirestoreDto(): ProductRequestFirestoreDto = ProductRequestFirestoreDto(
    user_id = integer("user_id"),
    product_name = text("product_name"),
    image_url = getString("image_url"),
    category = getString("category"),
    brand = getString("brand"),
    release_date = getString("release_date"),
    status = text("status"),
    createdAt = getTimestamp("createdAt")
)
/** Traduce el transporte al modelo común conservando campos y referencias. */
fun ProductRequestFirestoreDto.toProductRequestInfo(requestId: String): ProductRequestInfo = ProductRequestInfo(
    userId = user_id,
    productName = product_name,
    imageUrl = image_url,
    category = category,
    brand = brand,
    releaseDate = release_date,
    status = status,
    createdAt = checkNotNull(createdAt.isoText()) { "Fecha createdAt ausente" },
    id = requestId
)
