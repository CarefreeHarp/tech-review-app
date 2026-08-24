package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductCategoryContent
import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.models.ProductSearchContent
import com.example.devicersapp.ui.models.RatingDistribution
import com.example.devicersapp.ui.models.RatingSummaryContent

/** Centraliza los productos locales y los datos derivados que muestran sus pantallas. */
object LocalProductProvider {

    val categories = listOf(
        ProductCategoryContent(id = "all", labelResId = R.string.all),
        ProductCategoryContent(id = "cellphones", labelResId = R.string.cellphones),
        ProductCategoryContent(id = "audio", labelResId = R.string.audio),
        ProductCategoryContent(id = "computers", labelResId = R.string.computers)
    )

    val products = listOf(
        ProductSearchContent(
            id = "headphones",
            categoryId = "audio",
            searchTerms = listOf("auriculares", "marca", "audio"),
            nameResId = R.string.rate_product_name,
            brandResId = R.string.rate_product_brand,
            categoryResId = R.string.audio,
            imageDescriptionResId = R.string.rate_product_image_description,
            imageResId = R.drawable.device_00,
            rating = 5
        ),
        ProductSearchContent(
            id = "phone",
            categoryId = "cellphones",
            searchTerms = listOf("teléfono", "telefono", "marca", "celulares"),
            nameResId = R.string.feed_product_phone,
            brandResId = R.string.create_review_phone_brand,
            categoryResId = R.string.cellphones,
            imageDescriptionResId = R.string.create_review_phone_image_description,
            imageResId = R.drawable.device_02,
            rating = 5
        ),
        ProductSearchContent(
            id = "laptop-pro",
            categoryId = "computers",
            searchTerms = listOf("laptop", "computador", "nova"),
            nameResId = R.string.create_review_product_three_name,
            brandResId = R.string.create_review_product_three_brand,
            categoryResId = R.string.computers,
            imageDescriptionResId = R.string.create_review_product_three_image,
            imageResId = R.drawable.device_01,
            rating = 5
        ),
        ProductSearchContent(
            id = "wireless-headphones",
            categoryId = "audio",
            searchTerms = listOf("audífonos", "audifonos", "soundwave"),
            nameResId = R.string.create_review_product_four_name,
            brandResId = R.string.create_review_product_four_brand,
            categoryResId = R.string.audio,
            imageDescriptionResId = R.string.create_review_product_four_image,
            imageResId = R.drawable.device_08,
            rating = 4
        ),
        ProductSearchContent(
            id = "cellphone-plus",
            categoryId = "cellphones",
            searchTerms = listOf("celular", "teléfono", "telefono", "pixelar"),
            nameResId = R.string.create_review_product_five_name,
            brandResId = R.string.create_review_product_five_brand,
            categoryResId = R.string.cellphones,
            imageDescriptionResId = R.string.create_review_product_five_image,
            imageResId = R.drawable.device_03,
            rating = 5
        ),
        ProductSearchContent(
            id = "monitor-ultra",
            categoryId = "computers",
            searchTerms = listOf("monitor", "pantalla", "vision"),
            nameResId = R.string.create_review_product_six_name,
            brandResId = R.string.create_review_product_six_brand,
            categoryResId = R.string.computers,
            imageDescriptionResId = R.string.create_review_product_six_image,
            imageResId = R.drawable.device_04,
            rating = 4
        ),
        ProductSearchContent(
            id = "speaker-mini",
            categoryId = "audio",
            searchTerms = listOf("parlante", "bocina", "boom"),
            nameResId = R.string.create_review_product_seven_name,
            brandResId = R.string.create_review_product_seven_brand,
            categoryResId = R.string.audio,
            imageDescriptionResId = R.string.create_review_product_seven_image,
            imageResId = R.drawable.device_05,
            rating = 4
        ),
        ProductSearchContent(
            id = "tablet-air",
            categoryId = "cellphones",
            searchTerms = listOf("tablet", "orbital"),
            nameResId = R.string.create_review_product_eight_name,
            brandResId = R.string.create_review_product_eight_brand,
            categoryResId = R.string.cellphones,
            imageDescriptionResId = R.string.create_review_product_eight_image,
            imageResId = R.drawable.device_06,
            rating = 5
        ),
        ProductSearchContent(
            id = "mechanical-keyboard",
            categoryId = "computers",
            searchTerms = listOf("teclado", "keypro", "mecánico", "mecanico"),
            nameResId = R.string.create_review_product_nine_name,
            brandResId = R.string.create_review_product_nine_brand,
            categoryResId = R.string.computers,
            imageDescriptionResId = R.string.create_review_product_nine_image,
            imageResId = R.drawable.device_07,
            rating = 4
        ),
        ProductSearchContent(
            id = "usb-microphone",
            categoryId = "audio",
            searchTerms = listOf("micrófono", "microfono", "vocal", "usb"),
            nameResId = R.string.create_review_product_ten_name,
            brandResId = R.string.create_review_product_ten_brand,
            categoryResId = R.string.audio,
            imageDescriptionResId = R.string.create_review_product_ten_image,
            imageResId = R.drawable.device_09,
            rating = 5
        )
    )

    /** Producto de referencia usado únicamente cuando una vista previa no recibe una selección. */
    val product = ProductContent(
        nameResId = R.string.product_title,
        brandResId = R.string.product_brand_label,
        imageResId = R.drawable.device_00,
        imageDescriptionResId = R.string.rate_product_image_description
    )

    val ratingSummary = RatingSummaryContent(
        averageResId = R.string.product_average_value,
        rating = 5,
        reviewCountResId = R.string.product_review_count,
        distribution = listOf(
            RatingDistribution(5, 0.68f, R.string.product_rating_five_percentage),
            RatingDistribution(4, 0.22f, R.string.product_rating_four_percentage),
            RatingDistribution(3, 0.07f, R.string.product_rating_three_percentage),
            RatingDistribution(2, 0.02f, R.string.product_rating_two_percentage),
            RatingDistribution(1, 0.01f, R.string.product_rating_one_percentage)
        )
    )

    /** Obtiene el contenido de detalle a partir del identificador recibido en la navegación. */
    fun getProductByNameResId(productNameResId: Int): ProductContent {
        products.find { it.nameResId == productNameResId }?.let { selectedProduct ->
            return ProductContent(
                nameResId = selectedProduct.nameResId,
                brandResId = selectedProduct.brandResId,
                imageResId = selectedProduct.imageResId,
                imageDescriptionResId = selectedProduct.imageDescriptionResId
            )
        }

        LocalReviewProvider.reviews.find { it.productNameResId == productNameResId }?.let { review ->
            return ProductContent(
                nameResId = review.productNameResId,
                brandResId = review.productMetadataResId,
                imageResId = review.productImageResId,
                imageDescriptionResId = R.string.review_product_image
            )
        }

        return product
    }
}
