package com.example.devicersapp.ui.screens.search_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.screens.search_profile.components.SearchProfileFilterPanel
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.ScreenTitleText
import com.example.devicersapp.ui.utils.navigation.SearchBar
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.search.SearchEntityToggle

/** Renderiza la búsqueda de perfiles y conecta sus eventos con el estado del ViewModel. */
@Composable
fun SearchProfileView(
    onApplyFilters: () -> Unit = {},
    onUsersClick: () -> Unit = {},
    onProductsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: SearchProfileViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    SearchProfileViewContent(
        state = uiState,
        onSearchTextChange = viewModel::onSearchTextChange,
        onUsernameChange = viewModel::onUsernameChange,
        onInterestsChange = viewModel::onInterestsChange,
        onMinimumReviewsChange = viewModel::onMinimumReviewsChange,
        onRelationshipChange = viewModel::onRelationshipChange,
        onSortChange = viewModel::onSortChange,
        onClearFilters = viewModel::onClearFilters,
        onApplyFilters = onApplyFilters,
        onUsersClick = onUsersClick,
        onProductsClick = onProductsClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Reúne el título, la búsqueda general y la tarjeta de filtros de perfiles.
 *
 * @param state Estado inmutable que describe la consulta y los filtros activos.
 * @param onSearchTextChange Acción que solicita actualizar la búsqueda general.
 * @param onUsernameChange Acción que solicita actualizar el nombre de usuario.
 * @param onInterestsChange Acción que solicita actualizar los intereses.
 * @param onMinimumReviewsChange Acción que solicita cambiar la cantidad mínima de reseñas.
 * @param onRelationshipChange Acción que solicita cambiar la relación.
 * @param onSortChange Acción que solicita cambiar el orden.
 * @param onClearFilters Acción que solicita restablecer los filtros.
 * @param onApplyFilters Acción que solicita aplicar los filtros.
 * @param onUsersClick Acción que solicita mostrar la búsqueda de usuarios.
 * @param onProductsClick Acción que solicita mostrar la búsqueda de productos.
 * @param modifier Modificador aplicado al contenido.
 */
@Composable
fun SearchProfileViewContent(
    state: SearchProfileState,
    onSearchTextChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onInterestsChange: (String) -> Unit,
    onMinimumReviewsChange: (Float) -> Unit,
    onRelationshipChange: (String) -> Unit,
    onSortChange: (String) -> Unit,
    onClearFilters: () -> Unit,
    onApplyFilters: () -> Unit,
    onUsersClick: () -> Unit,
    onProductsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    // El contenido no es una colección repetida, así que basta con un contenedor desplazable.
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.search_profile_title),
            modifier = Modifier.fillMaxWidth(),
            color = colors.textPrimary,
            style = ScreenTitleText
        )

        Spacer(modifier = Modifier.height(20.dp))

        SearchBar(
            placeholder = R.string.search_profile_placeholder,
            backgroundColor = colors.surface,
            showSearchIcon = true,
            text = state.searchText,
            onTextChange = onSearchTextChange
        )

        Spacer(modifier = Modifier.height(16.dp))
        SearchEntityToggle(
            isUsersSelected = true,
            onUsersClick = onUsersClick,
            onProductsClick = onProductsClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        SearchProfileFilterPanel(
            username = state.username,
            onUsernameChange = onUsernameChange,

            interests = state.interests,
            onInterestsChange = onInterestsChange,

            minimumReviews = state.minimumReviews,
            onMinimumReviewsChange = onMinimumReviewsChange,

            relationship = state.relationship,
            onRelationshipChange = onRelationshipChange,

            sortBy = state.sortBy,
            onSortChange = onSortChange,
            onClearFilters = onClearFilters,
            onApplyFilters = onApplyFilters
        )

        // Deja aire para que la barra flotante no tape el botón de aplicar.
        Spacer(modifier = Modifier.height(120.dp))
    }
}

/** Muestra la búsqueda de perfiles en tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 1000)
fun SearchProfileViewPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(selectedItem = "search", showBottomBar = true) { innerPadding ->
            SearchProfileViewContent(
                state = SearchProfileState(),
                onSearchTextChange = {},
                onUsernameChange = {},
                onInterestsChange = {},
                onMinimumReviewsChange = {},
                onRelationshipChange = {},
                onSortChange = {},
                onClearFilters = {},
                onApplyFilters = {},
                onUsersClick = {},
                onProductsClick = {},
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .fillMaxSize()
                    .background(LocalDevicersColors.current.background)
            )
        }
    }
}

/** Muestra la búsqueda de perfiles en tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 1000)
fun SearchProfileViewDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(selectedItem = "search", showBottomBar = true) { innerPadding ->
            SearchProfileViewContent(
                state = SearchProfileState(),
                onSearchTextChange = {},
                onUsernameChange = {},
                onInterestsChange = {},
                onMinimumReviewsChange = {},
                onRelationshipChange = {},
                onSortChange = {},
                onClearFilters = {},
                onApplyFilters = {},
                onUsersClick = {},
                onProductsClick = {},
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .fillMaxSize()
                    .background(LocalDevicersColors.current.background)
            )
        }
    }
}
