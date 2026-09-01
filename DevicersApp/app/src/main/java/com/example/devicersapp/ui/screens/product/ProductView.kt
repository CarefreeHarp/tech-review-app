package com.example.devicersapp.ui.screens.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalProductProvider
import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.models.RatingSummaryContent
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.screens.product.components.ProductImageCard
import com.example.devicersapp.ui.screens.product.components.RatingSummary
import com.example.devicersapp.ui.screens.product.components.ReviewCard
import com.example.devicersapp.ui.theme.CardMetadataText
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.SearchHeadingText
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/**
 * Configura el detalle del producto y observa su estado desde el ViewModel.
 */
@Composable
fun ProductView(
    productNameResId: Int,
    onViewMoreClick: (Int) -> Unit = {},
    onRateClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(productNameResId) {
        viewModel.loadProduct(productNameResId)
    }

    val product = uiState.product
    val ratingSummary = uiState.ratingSummary

    if (product != null && ratingSummary != null) {
        ProductViewContent(
            product = product,
            ratingSummary = ratingSummary,
            reviews = uiState.reviews,
            onRateClick = onRateClick,
            onViewMoreClick = onViewMoreClick,
            modifier = modifier
                .fillMaxSize()
                .background(LocalDevicersColors.current.background)
        )
    }
}

@Composable
fun ProductViewContent(
    product: ProductContent,
    ratingSummary: RatingSummaryContent,
    reviews: List<ReviewContent>,
    onRateClick: (Int) -> Unit,
    onViewMoreClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Column(
        modifier = modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            text = stringResource(product.nameResId),
            color = colors.textPrimary,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = stringResource(product.brandResId),
            color = colors.textSecondary,
            style = CardMetadataText
        )

        Spacer(modifier = Modifier.height(4.dp))

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 3.dp,
            color = colors.border
        )

        LazyColumn {
            item {
                Spacer(modifier = Modifier.height(18.dp))

                ProductImageCard(product)

                Spacer(modifier = Modifier.height(24.dp))

                RatingSummary(ratingSummary)

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        onRateClick(product.nameResId)
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
                        text = stringResource(R.string.product_rate),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = stringResource(R.string.product_top_reviews),
                    color = colors.textPrimary,
                    style = SearchHeadingText
                )

                Spacer(modifier = Modifier.height(14.dp))
            }

            itemsIndexed(reviews) { _, review ->
                ReviewCard(
                    review = review,
                    onViewMoreClick = {
                        onViewMoreClick(review.id)
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))
            }

            item {
                Spacer(modifier = Modifier.height(110.dp))
            }
        }
    }
}

@Composable
@Preview(showBackground = true, heightDp = 1100)
fun ProductViewPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(
            selectedItem = "search",
            showBottomBar = true,
            topBarNumber = 6
        ) { innerPadding ->
            ProductView(
                productNameResId = LocalProductProvider.product.nameResId,
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding()
                ),
                viewModel = ProductViewModel()
            )
        }
    }
}

@Composable
@Preview(showBackground = true, heightDp = 1100)
fun ProductViewDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(
            selectedItem = "search",
            showBottomBar = true,
            topBarNumber = 6
        ) { innerPadding ->
            ProductView(
                productNameResId = LocalProductProvider.product.nameResId,
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding()
                ),
                viewModel = ProductViewModel()
            )
        }
    }
}