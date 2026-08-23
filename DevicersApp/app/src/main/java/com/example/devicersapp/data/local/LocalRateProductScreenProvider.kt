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

    fun getProductById(productId: String): ProductContent {
        val selectedProduct = LocalCreateReviewScreenProvider.products.find {
            it.id == productId
        } ?: return product

        return ProductContent(
            nameResId = selectedProduct.nameResId,
            brandResId = selectedProduct.brandResId,
            imageResId = selectedProduct.imageResId,
            imageDescriptionResId = selectedProduct.imageDescriptionResId
        )
    }
    fun getProductIdByNameResId(productNameResId: Int): String? { //convierte nameRedIs en productId
        return LocalCreateReviewScreenProvider.products.find {
            it.nameResId == productNameResId
        }?.id
    }
    fun getProductByNameResId(productNameResId: Int): ProductContent {

        val createReviewProduct =
            LocalCreateReviewScreenProvider.products.find {
                it.nameResId == productNameResId
            }

        if (createReviewProduct != null) {
            return ProductContent(
                nameResId = createReviewProduct.nameResId,
                brandResId = createReviewProduct.brandResId,
                imageResId = createReviewProduct.imageResId,
                imageDescriptionResId = createReviewProduct.imageDescriptionResId
            )
        }

        val profileProduct =
            LocalProfileProvider.ratedProducts.find {
                it.nameResId == productNameResId
            }

        if (profileProduct != null) {
            return ProductContent(
                nameResId = profileProduct.nameResId,
                brandResId = R.string.review_product_brand,
                imageResId = profileProduct.imageResId,
                imageDescriptionResId = profileProduct.imageDescriptionResId
            )
        }

        val savedProduct =
            LocalProfileProvider.savedReviews.find {
                it.productNameResId == productNameResId
            }

        if (savedProduct != null) {
            return ProductContent(
                nameResId = savedProduct.productNameResId,
                brandResId = R.string.review_product_brand,
                imageResId = savedProduct.productImageResId,
                imageDescriptionResId = savedProduct.imageDescriptionResId
            )
        }

        return product
    }
}