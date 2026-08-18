package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductContent

/** Provee el producto local disponible para calificar en la pantalla de calificación. */
object LocalRateProductScreenProvider {

    val product = ProductContent(
        nameResId = R.string.rate_product_name,
        brandResId = R.string.rate_product_brand,
        imageResId = R.drawable.device_03,
        imageDescriptionResId = R.string.rate_product_image_description
    )
}
