package com.example.devicersapp.data.dto.ResponseDTO.firestore
import com.example.devicersapp.ui.models.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
/** Campos de artículos en Firestore v9; los IDs de los padres se reciben desde la ruta. */
data class ProductFirestoreDto(
    val id: Int,
    val category_id: Int,
    val category_name: String,
    val brand_name: String,
    val name: String,
    val model: String?,
    val description: String?,
    val image_url: String?,
    val release_date: String?,
    val specifications: String?,
    val stores_count: Int,
    val is_active: Boolean,
    val created_at: Timestamp?,
    val updated_at: Timestamp?
)
/** Decodifica el documento sin consultas implícitas a otras colecciones. */
internal fun DocumentSnapshot.toProductFirestoreDto(): ProductFirestoreDto {
    check(integer("id") == numericDocumentId())
    return checkNotNull(data).toProductFirestoreDto()
}
internal fun Map<String, Any?>.toProductFirestoreDto(): ProductFirestoreDto = ProductFirestoreDto(
    id = integer("id"),
    category_id = integer("category_id"),
    category_name = text("category_name"),
    brand_name = text("brand_name"),
    name = text("name"),
    model = getString("model"),
    description = getString("description"),
    image_url = getString("image_url"),
    release_date = getString("release_date"),
    specifications = getString("specifications"),
    stores_count = integer("stores_count"),
    is_active = flag("is_active"),
    created_at = getTimestamp("created_at"),
    updated_at = getTimestamp("updated_at")
)
/** Traduce el transporte al modelo común conservando campos y referencias. */
fun ProductFirestoreDto.toProductInfo(): ProductInfo = ProductInfo(
    id = id,
    categoryId = category_id,
    categoryName = category_name,
    brandName = brand_name,
    name = name,
    model = model,
    description = description,
    imageUrl = image_url,
    releaseDate = release_date,
    specifications = specifications,
    storesCount = stores_count,
    isActive = is_active,
    createdAt = checkNotNull(created_at.isoText()) { "Fecha created_at ausente" },
    updatedAt = checkNotNull(updated_at.isoText()) { "Fecha updated_at ausente" },
    brandId = null
)
