package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductSearchContent

/** Provee los productos de ejemplo que devuelve una búsqueda del catálogo. */
object LocalFoundProductsScreenProvider {

    val results = listOf(
        ProductSearchContent(
            id = "headphones",
            categoryId = "audio",
            searchTerms = listOf("auriculares", "audio", "sony"),
            nameResId = R.string.found_product_first_name,
            brandResId = R.string.found_product_first_brand,
            categoryResId = R.string.audio,
            imageDescriptionResId = R.string.found_product_image,
            imageResId = R.drawable.device_01,
            rating = 5,
            averageResId = R.string.found_product_first_average
        ),
        ProductSearchContent(
            id = "airpods_pro",
            categoryId = "audio",
            searchTerms = listOf("airpods", "audio", "apple"),
            nameResId = R.string.found_product_second_name,
            brandResId = R.string.found_product_second_brand,
            categoryResId = R.string.audio,
            imageDescriptionResId = R.string.found_product_image,
            imageResId = R.drawable.device_05,
            rating = 5,
            averageResId = R.string.found_product_second_average
        ),
        ProductSearchContent(
            id = "wh_1000xm5",
            categoryId = "audio",
            searchTerms = listOf("wh-1000xm5", "audio", "sony"),
            nameResId = R.string.found_product_third_name,
            brandResId = R.string.found_product_third_brand,
            categoryResId = R.string.audio,
            imageDescriptionResId = R.string.found_product_image,
            imageResId = R.drawable.device_06,
            rating = 5,
            averageResId = R.string.found_product_third_average
        )
    )
}
