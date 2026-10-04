package com.example.devicersapp.data.local

import com.example.devicersapp.ui.models.FeedReviewContent

/** Reseñas de ejemplo con la misma forma que entrega el backend; solo se usan en las vistas previas. */
object LocalFeedReviewProvider {

    // Fecha fija para que las vistas previas muestren antigüedades estables.
    private const val SAMPLE_CREATED_AT_MILLIS = 1_790_000_000_000L

    val reviews = listOf(
        sampleReview(
            reviewId = 1, productId = 1, productName = "Auriculares", productBrand = "Sony",
            productCategory = "Audio", productImage = "device_01", productAverage = 4.5f,
            authorId = 2, authorUsername = "mariana.tech", authorImage = "profile_avatar_01",
            rating = 5, title = "Excelente cancelación de ruido",
            body = "Los uso todos los días en el transporte público y aíslan muy bien el ruido. La batería dura más de una semana.",
            likes = 2, comments = 2
        ),
        sampleReview(
            reviewId = 2, productId = 2, productName = "Teléfono", productBrand = "Samsung",
            productCategory = "Celulares", productImage = "device_00", productAverage = 4f,
            authorId = 3, authorUsername = "camila.audio", authorImage = "profile_avatar_02",
            rating = 4, title = null,
            body = "La cámara es muy buena de día, aunque de noche le cuesta un poco más enfocar.",
            likes = 1, comments = 0
        )
    )

    private fun sampleReview(
        reviewId: Int,
        productId: Int,
        productName: String,
        productBrand: String,
        productCategory: String,
        productImage: String,
        productAverage: Float,
        authorId: Int,
        authorUsername: String,
        authorImage: String,
        rating: Int,
        title: String?,
        body: String,
        likes: Int,
        comments: Int
    ) = FeedReviewContent(
        reviewId = reviewId,
        productId = productId,
        productName = productName,
        productBrand = productBrand,
        productCategory = productCategory,
        productImage = productImage,
        productAverage = productAverage,
        authorId = authorId,
        authorUsername = authorUsername,
        authorImage = authorImage,
        rating = rating,
        title = title,
        body = body,
        likes = likes,
        comments = comments,
        createdAtMillis = SAMPLE_CREATED_AT_MILLIS - reviewId * 3_600_000L
    )
}
