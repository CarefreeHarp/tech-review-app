package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.models.ReplyContent
import com.example.devicersapp.ui.models.ReviewContent

/** Centraliza las reseñas locales, incluidos los comentarios que pertenecen a cada una. */
object LocalReviewProvider {

    // Este hilo se inserta dentro de las reseñas de ejemplo; no existe un provider independiente.
    private val defaultComments = listOf(
        ReplyContent("reply_one", R.string.review_reply_time, R.string.review_reply_text_one, likes = 24),
        ReplyContent("reply_two", R.string.review_reply_time, R.string.review_reply_text_two, depth = 1),
        ReplyContent("reply_three", R.string.review_reply_time_three, R.string.review_reply_text_three, depth = 2),
        ReplyContent("reply_four", R.string.review_reply_time_four, R.string.review_reply_text_four, depth = 1),
        ReplyContent("reply_eleven", R.string.review_reply_time, R.string.review_reply_text_eleven, likes = 6, depth = 1),
        ReplyContent("reply_five", R.string.review_reply_time_five, R.string.review_reply_text_five, likes = 8),
        ReplyContent("reply_six", R.string.review_reply_time_six, R.string.review_reply_text_six, depth = 1),
        ReplyContent("reply_seven", R.string.review_reply_time_seven, R.string.review_reply_text_seven, depth = 1),
        ReplyContent("reply_eight", R.string.review_reply_time, R.string.review_reply_text_eight, likes = 3),
        ReplyContent("reply_nine", R.string.review_reply_time_nine, R.string.review_reply_text_nine, depth = 1),
        ReplyContent("reply_ten", R.string.review_reply_time_ten, R.string.review_reply_text_ten, depth = 2)
    )

    /** Todas las reseñas publicadas que el feed puede mostrar. */
    private val feedReviews = listOf(
        feedReview("mariana", R.string.feed_product_phone, R.drawable.device_00, R.string.feed_metadata_phone, R.string.feed_review_phone, 125, R.string.feed_time_two_days, 5, R.string.feed_average_phone, defaultComments),
        feedReview("camila", R.string.feed_product_audio, R.drawable.device_01, R.string.feed_metadata_audio, R.string.feed_review_audio, 86, R.string.feed_time_one_day, 4, R.string.feed_average_audio, defaultComments.take(5)),
        feedReview("audio_fan", R.string.feed_product_computer, R.drawable.device_02, R.string.feed_metadata_computer, R.string.feed_review_computer, 41, R.string.feed_time_five_hours, 3, R.string.feed_average_computer, defaultComments.take(3)),
        feedReview("nora_mobile", R.string.feed_product_four, R.drawable.device_03, R.string.feed_metadata_four, R.string.feed_review_four, 67, R.string.feed_time_one_day, 5, R.string.feed_average_four, defaultComments.take(6)),
        feedReview("diego_gadgets", R.string.feed_product_five, R.drawable.device_04, R.string.feed_metadata_five, R.string.feed_review_five, 93, R.string.feed_time_five_hours, 4, R.string.feed_average_five, defaultComments.takeLast(5)),
        feedReview("luna_digital", R.string.feed_product_six, R.drawable.device_05, R.string.feed_metadata_six, R.string.feed_review_six, 52, R.string.feed_time_two_days, 4, R.string.feed_average_six, defaultComments.take(4)),
        feedReview("mariana", R.string.feed_product_seven, R.drawable.device_06, R.string.feed_metadata_seven, R.string.feed_review_seven, 110, R.string.feed_time_one_day, 5, R.string.feed_average_seven, defaultComments),
        feedReview("camila", R.string.feed_product_eight, R.drawable.device_07, R.string.feed_metadata_eight, R.string.feed_review_eight, 38, R.string.feed_time_five_hours, 4, R.string.feed_average_eight, defaultComments.take(2)),
        feedReview("audio_fan", R.string.feed_product_nine, R.drawable.device_08, R.string.feed_metadata_nine, R.string.feed_review_nine, 74, R.string.feed_time_two_days, 5, R.string.feed_average_nine, defaultComments.take(5)),
        feedReview("nora_mobile", R.string.feed_product_ten, R.drawable.device_09, R.string.feed_metadata_ten, R.string.feed_review_ten, 59, R.string.feed_time_one_day, 4, R.string.feed_average_ten, defaultComments.take(4)),
        feedReview("diego_gadgets", R.string.feed_product_eleven, R.drawable.device_00, R.string.feed_metadata_eleven, R.string.feed_review_eleven, 46, R.string.feed_time_five_hours, 5, R.string.feed_average_eleven, defaultComments.take(3)),
        feedReview("luna_digital", R.string.feed_product_twelve, R.drawable.device_01, R.string.feed_metadata_twelve, R.string.feed_review_twelve, 82, R.string.feed_time_two_days, 4, R.string.feed_average_twelve, defaultComments.take(6)),
        feedReview("mariana", R.string.feed_product_thirteen, R.drawable.device_02, R.string.feed_metadata_thirteen, R.string.feed_review_thirteen, 31, R.string.feed_time_one_day, 4, R.string.feed_average_thirteen, defaultComments.take(2)),
        feedReview("own_profile", R.string.feed_product_phone, R.drawable.device_00, R.string.feed_metadata_phone, R.string.review_detail_text, 128, R.string.review_time, 5, R.string.review_product_average, defaultComments.take(4), id = R.string.profile_product_first),
        feedReview("own_profile", R.string.feed_product_audio, R.drawable.device_01, R.string.feed_metadata_audio, R.string.review_card_text, 72, R.string.feed_time_one_day, 4, R.string.feed_average_audio, defaultComments.take(3), id = R.string.profile_product_second)
    )

    /** Reseñas usadas en el detalle del producto de referencia. */
    private val referenceProductReviews = listOf(
        productReview("mariana", R.string.review_card_username, 5, R.string.review_card_text, 12, R.string.feed_time_five_hours, defaultComments),
        productReview("camila", R.string.product_review_author_two, 4, R.string.product_review_text_two, 8, R.string.feed_time_one_day, defaultComments.take(5)),
        productReview("audio_fan", R.string.product_review_author_three, 4, R.string.product_review_text_three, 5, R.string.feed_time_one_day, defaultComments.take(3)),
        productReview("nora_mobile", R.string.product_review_author_four, 5, R.string.product_review_text_four, 15, R.string.feed_time_two_days, defaultComments.take(6)),
        productReview("diego_gadgets", R.string.product_review_author_five, 4, R.string.product_review_text_five, 9, R.string.feed_time_two_days, defaultComments.takeLast(5)),
        productReview("luna_digital", R.string.product_review_author_six, 5, R.string.product_review_text_six, 11, R.string.feed_time_two_days, defaultComments.take(3))
    )

    /** Reseñas adicionales que completan los historiales de los perfiles mostrados en la aplicación. */
    private val profileReviews = listOf(
        feedReview("mariana", R.string.feed_product_four, R.drawable.device_03, R.string.feed_metadata_four, R.string.feed_review_four, 79, R.string.feed_time_five_hours, 5, R.string.feed_average_four, defaultComments.take(4), id = 1001),
        feedReview("mariana", R.string.feed_product_five, R.drawable.device_04, R.string.feed_metadata_five, R.string.feed_review_five, 96, R.string.feed_time_one_day, 4, R.string.feed_average_five, defaultComments.take(3), id = 1002),
        feedReview("mariana", R.string.feed_product_six, R.drawable.device_05, R.string.feed_metadata_six, R.string.feed_review_six, 64, R.string.feed_time_two_days, 5, R.string.feed_average_six, defaultComments.take(5), id = 1003),
        feedReview("mariana", R.string.feed_product_eight, R.drawable.device_07, R.string.feed_metadata_eight, R.string.feed_review_eight, 88, R.string.feed_time_one_day, 4, R.string.feed_average_eight, defaultComments.take(2), id = 1004),
        feedReview("own_profile", R.string.feed_product_seven, R.drawable.device_06, R.string.feed_metadata_seven, R.string.feed_review_seven, 104, R.string.feed_time_five_hours, 5, R.string.feed_average_seven, defaultComments.take(4), id = 2001),
        feedReview("own_profile", R.string.feed_product_nine, R.drawable.device_08, R.string.feed_metadata_nine, R.string.feed_review_nine, 57, R.string.feed_time_one_day, 4, R.string.feed_average_nine, defaultComments.take(3), id = 2002),
        feedReview("own_profile", R.string.feed_product_ten, R.drawable.device_09, R.string.feed_metadata_ten, R.string.feed_review_ten, 73, R.string.feed_time_two_days, 5, R.string.feed_average_ten, defaultComments.take(5), id = 2003),
        feedReview("own_profile", R.string.feed_product_twelve, R.drawable.device_01, R.string.feed_metadata_twelve, R.string.feed_review_twelve, 91, R.string.feed_time_one_day, 5, R.string.feed_average_twelve, defaultComments.take(2), id = 2004)
    )

    /** Fuente única de todas las reseñas existentes; el feed las presenta completas. */
    val reviews = feedReviews + profileReviews + referenceProductReviews

    /** Reseñas del producto de referencia, derivadas de la fuente única de reseñas. */
    val productReviews = reviews.filter { review ->
        review.productNameResId == LocalProductProvider.product.nameResId
    }

    /** Busca una reseña visible desde cualquier origen local. */
    fun findById(reviewId: Int): ReviewContent? =
        reviews.find { it.id == reviewId }

    /** Busca el producto asociado con una reseña local. */
    fun findProductByReviewId(reviewId: Int): ProductContent? =
        reviews.find { it.id == reviewId }?.toProduct()

    /** Devuelve solamente las reseñas que fueron creadas por el perfil indicado. */
    fun reviewsForProfile(profileId: String): List<ReviewContent> =
        reviews.filter { review -> review.authorId == profileId }

    /** Devuelve solamente las reseñas existentes que pertenecen al producto indicado. */
    fun reviewsForProduct(productNameResId: Int): List<ReviewContent> =
        reviews.filter { review -> review.productNameResId == productNameResId }

    /** Construye una reseña del feed con la relación a su perfil y sus comentarios. */
    private fun feedReview(authorId: String, productNameResId: Int, productImageResId: Int, productMetadataResId: Int, textResId: Int, likes: Int, timeAgoResId: Int, rating: Int, productAverageResId: Int, comments: List<ReplyContent>, id: Int = productNameResId) = ReviewContent(
        authorId = authorId, productNameResId = productNameResId, productImageResId = productImageResId,
        productMetadataResId = productMetadataResId, rating = rating, textResId = textResId,
        likes = likes, comments = comments, timeAgoResId = timeAgoResId,
        productAverageResId = productAverageResId,
        id = id
    )

    /** Construye una reseña vinculada al producto de referencia. */
    private fun productReview(authorId: String, id: Int, rating: Int, textResId: Int, likes: Int, timeAgoResId: Int, comments: List<ReplyContent>) = ReviewContent(
        authorId = authorId, productNameResId = LocalProductProvider.product.nameResId,
        productImageResId = LocalProductProvider.product.imageResId,
        productMetadataResId = LocalProductProvider.product.brandResId, rating = rating,
        textResId = textResId, likes = likes, comments = comments, timeAgoResId = timeAgoResId,
        productAverageResId = R.string.product_average_value, id = id
    )

    /** Convierte la relación de producto de la reseña al contenido requerido por la pantalla. */
    private fun ReviewContent.toProduct() = ProductContent(
        nameResId = productNameResId, brandResId = productMetadataResId,
        imageResId = productImageResId, imageDescriptionResId = R.string.review_product_image
    )

}
