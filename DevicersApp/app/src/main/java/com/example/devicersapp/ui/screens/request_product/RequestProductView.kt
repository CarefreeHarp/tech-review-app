package com.example.devicersapp.ui.screens.request_product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.SearchControlText
import com.example.devicersapp.ui.utils.navigation.SearchBar
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.search.FilterLabel

/** Renderiza la solicitud de producto y conecta sus eventos con el estado del ViewModel. */
@Composable
fun RequestProductView(
    onSendRequest: () -> Unit = {},
    onUploadPhoto: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: RequestProductViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    RequestProductViewContent(
        state = uiState,
        onProductNameChange = viewModel::onProductNameChange,
        onCategoryChange = viewModel::onCategoryChange,
        onBrandChange = viewModel::onBrandChange,
        onReleaseDateChange = viewModel::onReleaseDateChange,
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
 * @param state Estado inmutable que describe el formulario y sus validaciones.
 * @param onProductNameChange Acción que solicita actualizar el nombre.
 * @param onCategoryChange Acción que solicita actualizar la categoría.
 * @param onBrandChange Acción que solicita actualizar la marca.
 * @param onReleaseDateChange Acción que solicita actualizar la fecha de lanzamiento.
 * @param onUploadPhoto Acción solicitada al adjuntar una fotografía.
 * @param onSendRequest Acción solicitada al enviar la solicitud.
 * @param modifier Modificador aplicado al contenido.
 */
@Composable
fun RequestProductViewContent(
    state: RequestProductState,
    onProductNameChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onBrandChange: (String) -> Unit,
    onReleaseDateChange: (String) -> Unit,
    onUploadPhoto: () -> Unit,
    onSendRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

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
            text = state.productName,
            onTextChange = onProductNameChange
        )

        Spacer(modifier = Modifier.height(18.dp))
        FilterLabel(R.string.request_product_category, color = colors.textPrimary)
        Spacer(modifier = Modifier.height(7.dp))
        SearchBar(
            placeholder = R.string.request_product_category_placeholder,
            backgroundColor = colors.surface,
            showSearchIcon = false,
            text = state.category,
            onTextChange = onCategoryChange
        )

        Spacer(modifier = Modifier.height(18.dp))
        FilterLabel(R.string.request_product_brand, color = colors.textPrimary)
        Spacer(modifier = Modifier.height(7.dp))
        SearchBar(
            placeholder = R.string.request_product_brand_placeholder,
            backgroundColor = colors.surface,
            showSearchIcon = false,
            text = state.brand,
            onTextChange = onBrandChange
        )

        Spacer(modifier = Modifier.height(18.dp))
        FilterLabel(R.string.launch_date, color = colors.textPrimary)
        Spacer(modifier = Modifier.height(7.dp))
        SearchBar(
            placeholder = R.string.launch_date_placeholder,
            backgroundColor = colors.surface,
            showSearchIcon = false,
            text = state.releaseDate,
            // El ViewModel filtra las letras, incluso cuando el contenido llega por pegado de texto.
            onTextChange = onReleaseDateChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        if (state.showReleaseDateError) {
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
                .clickable(enabled = state.canSendRequest, onClick = onSendRequest)
                .height(52.dp)
                .background(
                    color = if (state.canSendRequest) colors.primary else colors.border,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.request_product_send),
                color = if (state.canSendRequest) colors.textOnPrimary else colors.textSecondary,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // Deja aire para que la barra flotante no tape el botón de envío.
        Spacer(modifier = Modifier.height(120.dp))
    }
}

/** Muestra la solicitud de producto en tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun RequestProductViewPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(
            selectedItem = "create",
            showBottomBar = true,
            topBarNumber = 9
        ) { innerPadding ->
            RequestProductViewContent(
                state = RequestProductState(),
                onProductNameChange = {},
                onCategoryChange = {},
                onBrandChange = {},
                onReleaseDateChange = {},
                onUploadPhoto = {},
                onSendRequest = {},
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .fillMaxSize()
                    .background(LocalDevicersColors.current.background)
            )
        }
    }
}

/** Muestra la solicitud de producto en tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun RequestProductViewDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(
            selectedItem = "create",
            showBottomBar = true,
            topBarNumber = 9
        ) { innerPadding ->
            RequestProductViewContent(
                state = RequestProductState(),
                onProductNameChange = {},
                onCategoryChange = {},
                onBrandChange = {},
                onReleaseDateChange = {},
                onUploadPhoto = {},
                onSendRequest = {},
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .fillMaxSize()
                    .background(LocalDevicersColors.current.background)
            )
        }
    }
}
