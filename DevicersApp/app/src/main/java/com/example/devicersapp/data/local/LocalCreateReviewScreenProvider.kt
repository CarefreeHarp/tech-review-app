package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductCategoryContent
import com.example.devicersapp.ui.models.ProductSearchContent

/** Provee las categorías y productos locales disponibles para iniciar una reseña. */
object LocalCreateReviewScreenProvider {

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
            imageResId = R.drawable.devicers_headphones_black,
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
            imageResId = R.drawable.electronic_phone,
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
            imageResId = R.drawable.electronic_desktop,
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
            imageResId = R.drawable.auriculares_logo,
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
            imageResId = R.drawable.electronic_phone,
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
            imageResId = R.drawable.electronic_desktop,
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
            imageResId = R.drawable.auriculares_logo,
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
            imageResId = R.drawable.electronic_phone,
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
            imageResId = R.drawable.electronic_desktop,
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
            imageResId = R.drawable.auriculares_logo,
            rating = 5
        )
    )
}
