package com.example.devicersapp.data.dto.RequestDTO.firestore
import com.example.devicersapp.ui.models.ProductSuggestion
import com.google.firebase.firestore.FieldValue
/** Solicitud documental; createdAt conserva el nombre definido en el esquema v9. */
data class ProductRequestFirestoreWriteDto(val user_id: Int, val product_name: String, val image_url: String?,
    val category: String, val brand: String, val release_date: String) {
    fun fields(): Map<String, Any?> = mapOf(
        "user_id" to user_id, "product_name" to product_name,
        "image_url" to image_url, "category" to category, "brand" to brand,
        "release_date" to release_date, "status" to "pending", "createdAt" to FieldValue.serverTimestamp()
    )
}
/** Copia los campos de la sugerencia sin incorporar identificadores de Auth. */
fun ProductSuggestion.toProductRequestFirestoreWriteDto(userId: Int) =
    ProductRequestFirestoreWriteDto(userId, productName, imageUrl, category, brand, releaseDate)
