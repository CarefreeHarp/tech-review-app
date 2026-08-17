package com.example.devicersapp.ui.screens.create_review

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.devicersapp.data.FakeDatabase
import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.screens.profile.ProfileScreen

private enum class CreateReviewFlowScreen {
    SELECT_PRODUCT,
    WRITE_REVIEW,
    PROFILE
}

/**
 * Controla el flujo temporal de creación de reseñas.
 *
 * Permite:
 * - seleccionar un producto;
 * - escribir una reseña;
 * - guardar la reseña en FakeDatabase;
 * - abrir temporalmente el perfil para comprobar la reseña publicada.
 *
 * De esta manera MainActivity solo necesita llamar CreateReviewFlow().
 */
@Composable
fun CreateReviewFlow(
    modifier: Modifier = Modifier
) {

    var currentScreen by remember {
        mutableStateOf(CreateReviewFlowScreen.SELECT_PRODUCT)
    }

    var selectedProduct by remember {
        mutableStateOf<ProductContent?>(null)
    }

    when (currentScreen) {

        CreateReviewFlowScreen.SELECT_PRODUCT -> {

            CreateReviewScreen(
                modifier = modifier,

                onProductClick = { product ->
                    selectedProduct = product
                    currentScreen = CreateReviewFlowScreen.WRITE_REVIEW
                },

                onProfileClick = {
                    currentScreen = CreateReviewFlowScreen.PROFILE
                }
            )
        }

        CreateReviewFlowScreen.WRITE_REVIEW -> {

            val product = selectedProduct

            if (product != null) {

                WriteReviewScreen(
                    product = product,
                    modifier = modifier,

                    onBackClick = {
                        selectedProduct = null
                        currentScreen = CreateReviewFlowScreen.SELECT_PRODUCT
                    },

                    onProfileClick = {
                        currentScreen = CreateReviewFlowScreen.PROFILE
                    },

                    onPublishClick = { rating, reviewText ->

                        FakeDatabase.addReview(
                            product = product,
                            rating = rating,
                            text = reviewText
                        )

                        selectedProduct = null

                        currentScreen =
                            CreateReviewFlowScreen.SELECT_PRODUCT
                    }
                )

            } else {

                currentScreen =
                    CreateReviewFlowScreen.SELECT_PRODUCT
            }
        }

        CreateReviewFlowScreen.PROFILE -> {

            ProfileScreen(
                modifier = modifier
            )
        }
    }
}