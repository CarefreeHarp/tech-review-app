package com.example.devicersapp.ui.screens.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.models.RatingDistribution
import com.example.devicersapp.ui.models.RatingSummaryContent
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.utils.navigation.BottomNavigationBar
import androidx.compose.foundation.layout.statusBarsPadding
import com.example.devicersapp.ui.screens.product.components.ProductImageCard
import com.example.devicersapp.ui.screens.product.components.RatingSummary
import com.example.devicersapp.ui.screens.product.components.ReviewCard
import com.example.devicersapp.ui.utils.navigation.AppTopBar

@Composable
fun ProductScreen(
    modifier: Modifier = Modifier
) {
    ProductScreenContent(
        modifier = modifier
            .fillMaxSize()
            .background(
                colorResource(R.color.background_light)
            )
            .statusBarsPadding()
    )
}

@Composable
fun ProductScreenContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {

        // Contenido desplazable
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp)
        ) {

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            // Header: volver + logo + opciones
            AppTopBar()

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            // Nombre del producto
            Text(
                text = stringResource(R.string.product_title),
                color = colorResource(R.color.text_primary_light),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            // Marca
            Text(
                text = stringResource(R.string.product_brand_label),
                color = colorResource(R.color.text_secondary_light),
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            ProductImageCard(
                ProductContent(
                    name = stringResource(R.string.product_title),
                    brand = stringResource(R.string.product_brand_label),
                    imageResId = R.drawable.devicers_headphones_black,
                    imageDescription = stringResource(R.string.review_product_image)
                )
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            RatingSummary(
                RatingSummaryContent(
                    title = stringResource(R.string.product_average_rating),
                    average = stringResource(R.string.product_average_value),
                    rating = 5,
                    reviewCount = stringResource(R.string.product_review_count),
                    distribution = listOf(
                        RatingDistribution(stringResource(R.string.rating_five), 0.88f, stringResource(R.string.product_rating_five_percentage)),
                        RatingDistribution(stringResource(R.string.rating_four), 0.52f, stringResource(R.string.product_rating_four_percentage)),
                        RatingDistribution(stringResource(R.string.rating_three), 0.17f, stringResource(R.string.product_rating_three_percentage)),
                        RatingDistribution(stringResource(R.string.rating_two), 0.07f, stringResource(R.string.product_rating_two_percentage)),
                        RatingDistribution(stringResource(R.string.rating_one), 0.05f, stringResource(R.string.product_rating_one_percentage))
                    )
                )
            )

            Spacer(modifier = Modifier.height(38.dp))

            // Botón para calificar
            Button(
                onClick = {
                    // Más adelante conectaremos esta acción
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.primary_yellow),
                    contentColor = colorResource(R.color.text_primary_light)
                )
            ) {
                Text(
                    text = stringResource(R.string.product_rate),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(25.dp)
            )

            // Reseñas
            Spacer(modifier = Modifier.height(10.dp))

            ReviewCard(
                ReviewContent(
                    avatarResId = R.drawable.profile_avatar_01,
                    author = stringResource(R.string.review_card_username),
                    rating = 5,
                    text = stringResource(R.string.review_card_text),
                    likes = 23
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Espacio temporal para la futura tarjeta de reseña
            Spacer(
                modifier = Modifier.height(100.dp)
            )
        }

        // Barra inferior existente del proyecto
       // BottomNavigationBar(
       //     modifier = Modifier.fillMaxWidth(),
       //     selectedItem = "search"
       // )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductScreenPreview() {
    ProductScreen()
}
