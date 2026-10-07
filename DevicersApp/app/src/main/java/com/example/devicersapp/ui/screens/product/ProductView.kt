package com.example.devicersapp.ui.screens.product

import com.example.devicersapp.ui.screens.product.components.ProductSkeleton

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.screens.product.components.ProductImageCard
import com.example.devicersapp.ui.screens.product.components.RatingSummary
import com.example.devicersapp.ui.screens.product.components.ReviewCard
import com.example.devicersapp.ui.theme.CardMetadataText
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.SearchHeadingText

/**
 * Configura el detalle del producto y observa su estado desde el ViewModel.
 */
@Composable
fun ProductView(
    productId: Int,
    onViewMoreClick: (Int) -> Unit = {},
    onRateClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    androidx.compose.runtime.DisposableEffect(lifecycleOwner, productId) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) viewModel.loadProduct(productId)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        viewModel.loadProduct(productId)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    ProductViewContent(
        state = uiState,
        onRateClick = onRateClick,
        onViewMoreClick = onViewMoreClick,
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
 * Muestra los datos del producto y sus reseñas.
 */
@Composable
fun ProductViewContent(
    state: ProductState,
    onRateClick: (Int) -> Unit,
    onViewMoreClick: (Int) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    if (state.isLoading) {

        ProductSkeleton(modifier)

        return
    }

    if (state.errorMessage != null || state.errorMessageResId != null) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = stringResource(R.string.remote_product_load_error),
                color = colors.textPrimary,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = state.errorMessageResId?.let { stringResource(it) } ?: state.errorMessage.orEmpty(),
                color = colors.textSecondary,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Button(
                onClick = onRetry
            ) {
                Text(
                    text = stringResource(R.string.home_feed_retry)
                )
            }
        }

        return
    }

    val product = state.product ?: return

    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
    ) {

        Text(
            text = product.name,
            color = colors.textPrimary,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        if (!product.model.isNullOrBlank()) {
            Text(
                text = product.model,
                color = colors.textSecondary,
                style = CardMetadataText
            )
        }

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 3.dp,
            color = colors.border
        )

        LazyColumn {

            item {

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                ProductImageCard(
                    product = product
                )

                if (!product.description.isNullOrBlank()) {

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    Text(
                        text = product.description,
                        color = colors.textSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                RatingSummary(
                    reviews = state.reviews
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Button(
                    onClick = {
                        onRateClick(product.id)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.textOnPrimary
                    )
                ) {

                    Text(
                        text = stringResource(
                            R.string.product_rate
                        ),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                Text(
                    text = stringResource(
                        R.string.product_top_reviews
                    ),
                    color = colors.textPrimary,
                    style = SearchHeadingText
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )
            }

            items(
                items = state.reviews,
                key = { review ->
                    review.id
                }
            ) { review ->

                ReviewCard(
                    review = review,
                    onViewMoreClick = {
                        onViewMoreClick(review.id)
                    }
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )
            }

            item {

                Spacer(
                    modifier = Modifier.height(110.dp)
                )
            }
        }
    }
}