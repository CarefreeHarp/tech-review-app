package com.example.devicersapp.ui.screens.rateProduct

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.screens.rateProduct.components.RateProductHeader
import com.example.devicersapp.ui.screens.rateProduct.components.RateableProductCard
import com.example.devicersapp.ui.screens.rateProduct.components.RatingSelector
import com.example.devicersapp.ui.screens.rateProduct.components.ReviewForm
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/**
 * Configura el estado de la reseña y delega la interfaz de calificación al contenido.
 *
 * @param modifier Modificador aplicado al contenedor raíz.
 */
@Composable
fun RateProductScreen(
    modifier: Modifier = Modifier
) {

    var rating by rememberSaveable {
        mutableIntStateOf(0)
    }

    var title by rememberSaveable {
        mutableStateOf("")
    }

    var experience by rememberSaveable {
        mutableStateOf("")
    }

    var advantage by rememberSaveable {
        mutableStateOf("")
    }

    var disadvantage by rememberSaveable {
        mutableStateOf("")
    }

    val product = ProductContent(
        name = stringResource(R.string.rate_product_name),
        brand = stringResource(R.string.rate_product_brand),
        imageResId = R.drawable.auriculares_logo,
        imageDescription = stringResource(R.string.rate_product_image_description),
        showImage = true
    )

    RateProductScreenContent(
        product = product,

        rating = rating,
        onRatingChange = {
            rating = it
        },

        title = title,
        onTitleChange = {
            title = it
        },

        experience = experience,
        onExperienceChange = {
            experience = it
        },

        advantage = advantage,
        onAdvantageChange = {
            advantage = it
        },

        disadvantage = disadvantage,
        onDisadvantageChange = {
            disadvantage = it
        },

        onBackClick = {
            // TODO: Implementar el regreso a la pantalla de producto.
        },
        onChangeProduct = {
            // TODO: Implementar la selección de otro producto.
        },
        onPublishClick = {
            // TODO: Implementar la publicación de la calificación.
        },

        modifier = modifier.fillMaxSize()
    )
}

/**
 * Ensambla los componentes presentacionales de la pantalla de calificación.
 * El estado y las acciones se reciben desde el composable raíz para conservar el state hoisting.
 *
 * @param product Producto que se calificará.
 * @param rating Calificación seleccionada.
 * @param onRatingChange Acción al seleccionar una calificación.
 * @param title Título escrito para la reseña.
 * @param onTitleChange Acción al cambiar el título.
 * @param experience Experiencia escrita por la persona usuaria.
 * @param onExperienceChange Acción al cambiar la experiencia.
 * @param advantage Ventaja escrita para el producto.
 * @param onAdvantageChange Acción al cambiar la ventaja.
 * @param disadvantage Desventaja escrita para el producto.
 * @param onDisadvantageChange Acción al cambiar la desventaja.
 * @param onBackClick Acción de regreso.
 * @param onChangeProduct Acción para cambiar de producto.
 * @param onPublishClick Acción para publicar la calificación.
 * @param modifier Modificador aplicado a la lista raíz.
 */
@Composable
fun RateProductScreenContent(
    product: ProductContent,

    rating: Int,
    onRatingChange: (Int) -> Unit,

    title: String,
    onTitleChange: (String) -> Unit,

    experience: String,
    onExperienceChange: (String) -> Unit,

    advantage: String,
    onAdvantageChange: (String) -> Unit,

    disadvantage: String,
    onDisadvantageChange: (String) -> Unit,

    onBackClick: () -> Unit,
    onChangeProduct: () -> Unit,
    onPublishClick: () -> Unit,

    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
    ) {

        item {
            Spacer(modifier = Modifier.height(12.dp))

            RateProductHeader(
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            RateableProductCard(
                product = product,
                onChangeProduct = onChangeProduct
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            RatingSelector(
                rating = rating,
                onRatingChange = onRatingChange
            )

            Spacer(modifier = Modifier.height(28.dp))
        }

        item {
            ReviewForm(
                title = title,
                onTitleChange = onTitleChange,

                experience = experience,
                onExperienceChange = onExperienceChange,

                advantage = advantage,
                onAdvantageChange = onAdvantageChange,

                disadvantage = disadvantage,
                onDisadvantageChange = onDisadvantageChange,

                onPublishClick = onPublishClick
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}



@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun RateProductScreenWithScaffoldPreview() {
    DevicersAppTheme {

        DevicersScaffold(
            selectedItem = "add"
        ) { innerPadding ->

            RateProductScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}
