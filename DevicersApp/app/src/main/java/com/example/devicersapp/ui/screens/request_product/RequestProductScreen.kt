package com.example.devicersapp.ui.screens.request_product

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.screens.request_product.components.ProductPhotoUploader
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.SearchControlText
import com.example.devicersapp.ui.utils.navigation.SearchBar
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.search.FilterLabel
import com.example.devicersapp.ui.utils.search.formatLaunchDate
import com.example.devicersapp.ui.utils.search.isValidLaunchDate

/**
 * Configura el formulario con el que se pide agregar un producto al catálogo.
 *
 * @param onSendRequest Acción solicitada al enviar la solicitud.
 * @param onUploadPhoto Acción solicitada al adjuntar una fotografía.
 * @param modifier Modificador aplicado a la pantalla.
 */
@Composable
fun RequestProductScreen(
    onSendRequest: () -> Unit = {},
    onUploadPhoto: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // El formulario conserva sus valores ante recomposiciones y cambios de configuración.
    var productName by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    var brand by rememberSaveable { mutableStateOf("") }
    var releaseDate by rememberSaveable { mutableStateOf("") }

    RequestProductScreenContent(
        productName = productName,
        onProductNameChange = { productName = it },
        category = category,
        onCategoryChange = { category = it },
        brand = brand,
        onBrandChange = { brand = it },
        releaseDate = releaseDate,
        onReleaseDateChange = { releaseDate = it },
        onUploadPhoto = onUploadPhoto,
        onSendRequest = onSendRequest,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla los campos, el área de fotografía y la acción de envío de la solicitud.
 *
 * @param productName Nombre escrito para el producto solicitado.
 * @param onProductNameChange Acción que solicita actualizar el nombre.
 * @param category Categoría escrita para el producto.
 * @param onCategoryChange Acción que solicita actualizar la categoría.
 * @param brand Marca escrita para el producto.
 * @param onBrandChange Acción que solicita actualizar la marca.
 * @param releaseDate Fecha de lanzamiento escrita para el producto.
 * @param onReleaseDateChange Acción que solicita actualizar la fecha de lanzamiento.
 * @param onUploadPhoto Acción solicitada al adjuntar una fotografía.
 * @param onSendRequest Acción solicitada al enviar la solicitud.
 * @param modifier Modificador aplicado al contenido.
 */
@Composable
fun RequestProductScreenContent(
    productName: String,
    onProductNameChange: (String) -> Unit,
    category: String,
    onCategoryChange: (String) -> Unit,
    brand: String,
    onBrandChange: (String) -> Unit,
    releaseDate: String,
    onReleaseDateChange: (String) -> Unit,
    onUploadPhoto: () -> Unit,
    onSendRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    // La solicitud necesita al menos el nombre, y la fecha solo se acepta si existe en el calendario.
    val isReleaseDateValid = releaseDate.isBlank() || isValidLaunchDate(releaseDate)
    val showReleaseDateError = releaseDate.isNotBlank() &&
        releaseDate.filter(Char::isDigit).length == 8 &&
        !isReleaseDateValid
    val canSendRequest = productName.isNotBlank() && isReleaseDateValid

    // El contenido no es una colección repetida, así que basta con un contenedor desplazable.
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.request_product_subtitle).trim(),
            modifier = Modifier.fillMaxWidth(),
            color = colors.textSecondary,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(22.dp))
        FilterLabel(R.string.request_product_name, color = colors.textPrimary)
        Spacer(modifier = Modifier.height(7.dp))
        SearchBar(
            placeholder = R.string.request_product_name_placeholder,
            backgroundColor = colors.surface,
            showSearchIcon = false,
            text = productName,
            onTextChange = onProductNameChange
        )

        Spacer(modifier = Modifier.height(18.dp))
        FilterLabel(R.string.request_product_category, color = colors.textPrimary)
        Spacer(modifier = Modifier.height(7.dp))
        SearchBar(
            placeholder = R.string.request_product_category_placeholder,
            backgroundColor = colors.surface,
            showSearchIcon = false,
            text = category,
            onTextChange = onCategoryChange
        )

        Spacer(modifier = Modifier.height(18.dp))
        FilterLabel(R.string.request_product_brand, color = colors.textPrimary)
        Spacer(modifier = Modifier.height(7.dp))
        SearchBar(
            placeholder = R.string.request_product_brand_placeholder,
            backgroundColor = colors.surface,
            showSearchIcon = false,
            text = brand,
            onTextChange = onBrandChange
        )

        Spacer(modifier = Modifier.height(18.dp))
        FilterLabel(R.string.launch_date, color = colors.textPrimary)
        Spacer(modifier = Modifier.height(7.dp))
        SearchBar(
            placeholder = R.string.launch_date_placeholder,
            backgroundColor = colors.surface,
            showSearchIcon = false,
            text = releaseDate,
            // El filtrado evita letras, incluso cuando el contenido llega por pegado de texto.
            onTextChange = { onReleaseDateChange(formatLaunchDate(it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        if (showReleaseDateError) {
            Text(
                text = stringResource(R.string.launch_date_invalid),
                modifier = Modifier.padding(top = 4.dp),
                color = colors.error,
                style = SearchControlText
            )
        }

        Spacer(modifier = Modifier.height(18.dp))
        FilterLabel(R.string.request_product_photo, color = colors.textPrimary)
        Spacer(modifier = Modifier.height(7.dp))
        ProductPhotoUploader(onUploadClick = onUploadPhoto)

        Spacer(modifier = Modifier.height(26.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                // Una solicitud sin nombre o con una fecha imposible no se puede enviar.
                .clickable(enabled = canSendRequest, onClick = onSendRequest)
                .height(52.dp)
                .background(
                    color = if (canSendRequest) colors.primary else colors.border,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.request_product_send),
                color = if (canSendRequest) colors.textOnPrimary else colors.textSecondary,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // Deja aire para que la barra flotante no tape el botón de envío.
        Spacer(modifier = Modifier.height(120.dp))
    }
}

/** Muestra una vista previa de la solicitud de producto en el tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun RequestProductScreenPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(
            selectedItem = "create",
            showBottomBar = true,
            topBarNumber = 9
        ) { innerPadding ->
            RequestProductScreen(
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
            )
        }
    }
}

/** Muestra una vista previa de la solicitud de producto en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun RequestProductScreenDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(
            selectedItem = "create",
            showBottomBar = true,
            topBarNumber = 9
        ) { innerPadding ->
            RequestProductScreen(
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
            )
        }
    }
}
