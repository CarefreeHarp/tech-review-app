package com.example.devicersapp.data.dto.ResponseDTO.shared
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.firestore.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
/** Campos de categorías en ambas fuentes; los IDs de los padres se reciben desde la ruta. */
data class CategoryDto(
    val id: Int,
    val parent_category_id: Int?,
    val name: String,
    val description: String?
)
/** Decodifica el documento sin consultas implícitas a otras colecciones. */
internal fun DocumentSnapshot.toCategoryDto(): CategoryDto {
    check(integer("id") == numericDocumentId())
    return checkNotNull(data).toCategoryDto()
}
internal fun Map<String, Any?>.toCategoryDto(): CategoryDto = CategoryDto(
    id = integer("id"),
    parent_category_id = optionalInteger("parent_category_id"),
    name = text("name"),
    description = getString("description")
)
/** Traduce el transporte al modelo común conservando campos y referencias. */
fun CategoryDto.toCategoryInfo(): CategoryInfo = CategoryInfo(
    id = id,
    parentCategoryId = parent_category_id,
    name = name,
    description = description
)
