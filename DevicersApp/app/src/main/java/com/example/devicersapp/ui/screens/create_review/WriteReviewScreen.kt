package com.example.devicersapp.ui.screens.create_review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.screens.create_review.components.RatingSelector
import com.example.devicersapp.ui.screens.create_review.components.ReviewTextField
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.utils.navigation.BottomNavigationBar

/**
 * Pantalla donde el usuario escribe y publica una reseña.
 *
 * Administra el estado de la calificación y del texto
 * y los eleva a los componentes hijos.
 */
@Composable
fun WriteReviewScreen(
    product: ProductContent,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onPublishClick: (Int, String) -> Unit = { _, _ -> }
) {
    var rating by remember {
        mutableIntStateOf(0)
    }

    var reviewText by remember {
        mutableStateOf("")
    }

    WriteReviewScreenContent(
        product = product,
        rating = rating,
        onRatingChange = {
            rating = it
        },
        reviewText = reviewText,
        onReviewTextChange = {
            reviewText = it
        },
        onBackClick = onBackClick,
        onProfileClick = onProfileClick,
        onPublishClick = {
            onPublishClick(
                rating,
                reviewText
            )
        },
        modifier = modifier
    )
}

/**
 * Contenido visual del formulario de reseña.
 */
@Composable
fun WriteReviewScreenContent(
    product: ProductContent,
    rating: Int,
    onRatingChange: (Int) -> Unit,
    reviewText: String,
    onReviewTextChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onPublishClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val canPublish =
        rating > 0 &&
                reviewText.isNotBlank()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,

        topBar = {
            WriteReviewHeader(
                onBackClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            )
        },

        bottomBar = {
            BottomNavigationBar(
                selectedItem = "add",
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                onProfileClick = onProfileClick
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            ProductSelectedCard(
                product = product
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "¿Qué calificación le das?",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            RatingSelector(
                rating = rating,
                onRatingChange = onRatingChange
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "Cuéntanos tu experiencia",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            ReviewTextField(
                value = reviewText,
                onValueChange = onReviewTextChange
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Button(
                onClick = onPublishClick,
                enabled = canPublish,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(
                    text = "Publicar reseña",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }
    }
}

@Composable
private fun WriteReviewHeader(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "‹",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.clickable {
                onBackClick()
            }
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "Escribir reseña",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Spacer(
            modifier = Modifier.width(20.dp)
        )
    }
}

@Composable
private fun ProductSelectedCard(
    product: ProductContent
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(
                product.imageResId
            ),
            contentDescription = product.imageDescription,
            modifier = Modifier
                .size(72.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(8.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column {

            Text(
                text = product.name,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = product.brand,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}

private val PreviewProduct = ProductContent(
    name = "Auriculares",
    brand = "Marca · Audio",
    imageResId = R.drawable.devicers_headphones_black,
    imageDescription = "Auriculares"
)

@Composable
@Preview(showBackground = true)
fun WriteReviewLightPreview() {
    DevicersAppTheme(
        darkTheme = false
    ) {
        WriteReviewScreen(
            product = PreviewProduct
        )
    }
}

@Composable
@Preview(showBackground = true)
fun WriteReviewDarkPreview() {
    DevicersAppTheme(
        darkTheme = true
    ) {
        WriteReviewScreen(
            product = PreviewProduct
        )
    }
}