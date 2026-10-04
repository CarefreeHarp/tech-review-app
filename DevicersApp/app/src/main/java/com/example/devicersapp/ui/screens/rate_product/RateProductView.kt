package com.example.devicersapp.ui.screens.rate_product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.devicersapp.ui.screens.rate_product.components.RateableProductCard
import com.example.devicersapp.ui.screens.rate_product.components.RatingSelector
import com.example.devicersapp.ui.screens.rate_product.components.ReviewForm
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Renderiza la calificación y solicita la carga del producto que llega por argumento.
 */
@Composable
fun RateProductView(
    productId: Int,
    onPublishClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: RateProductViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    LaunchedEffect(uiState.publishedSuccessfully) {
        if (uiState.publishedSuccessfully) {
            onPublishClick()
        }
    }

    RateProductViewContent(
        state = uiState,
        onRatingChange = viewModel::onRatingChange,
        onTitleChange = viewModel::onTitleChange,
        onExperienceChange = viewModel::onExperienceChange,
        onAdvantageChange = viewModel::onAdvantageChange,
        onDisadvantageChange = viewModel::onDisadvantageChange,
        onChangeProduct = viewModel::onChangeProduct,
        onPublishClick = {
            viewModel.publishReview()
        },
        onRetry = {
            viewModel.loadProduct(productId)
        },
        modifier = modifier
            .fillMaxSize()
            .background(
                LocalDevicersColors.current.background
            )
    )
}

/**
 * Ensambla los componentes presentacionales de la pantalla de calificación.
 */
@Composable
fun RateProductViewContent(
    state: RateProductState,
    onRatingChange: (Int) -> Unit,
    onTitleChange: (String) -> Unit,
    onExperienceChange: (String) -> Unit,
    onAdvantageChange: (String) -> Unit,
    onDisadvantageChange: (String) -> Unit,
    onChangeProduct: () -> Unit,
    onPublishClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    if (state.isLoading) {

        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }

    if (state.errorMessage != null) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "No se pudo cargar el producto",
                color = colors.textPrimary,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = state.errorMessage,
                color = colors.textSecondary,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Button(
                onClick = onRetry
            ) {
                Text("Reintentar")
            }
        }

        return
    }

    val product = state.product ?: return

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {

        item {
            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }

        item {

            RateableProductCard(
                product = product,
                onChangeProduct = onChangeProduct
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }

        item {

            RatingSelector(
                rating = state.rating,
                onRatingChange = onRatingChange
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )
        }

        item {

            if (state.publishError != null) {

                Text(
                    text = state.publishError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }

            if (state.isPublishing) {

                CircularProgressIndicator()

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }

            ReviewForm(
                title = state.title,
                onTitleChange = onTitleChange,

                experience = state.experience,
                onExperienceChange = onExperienceChange,

                advantage = state.advantage,
                onAdvantageChange = onAdvantageChange,

                disadvantage = state.disadvantage,
                onDisadvantageChange = onDisadvantageChange,

                onPublishClick = onPublishClick
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )
        }
    }
}