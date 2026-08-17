package com.example.devicersapp.data.local

import androidx.compose.runtime.mutableStateListOf
import com.example.devicersapp.ui.models.ProductContent

/**
 * Representa una reseña creada por el usuario.
 *
 * Guarda únicamente la información necesaria para
 * mostrar posteriormente la reseña en el perfil.
 */
data class StoredUserReview(
    val productName: String,
    val rating: Int,
    val text: String
)

/**
 * Proveedor local de las reseñas creadas por el usuario.
 *
 * Mantiene temporalmente las reseñas en memoria mientras
 * la aplicación está en ejecución.
 */
object LocalUserReviewsProvider {

    val reviews = mutableStateListOf<StoredUserReview>()

    /**
     * Agrega una nueva reseña a la lista local.
     */
    fun addReview(
        product: ProductContent,
        rating: Int,
        text: String
    ) {
        reviews.add(
            StoredUserReview(
                productName = product.name,
                rating = rating,
                text = text
            )
        )
    }
}