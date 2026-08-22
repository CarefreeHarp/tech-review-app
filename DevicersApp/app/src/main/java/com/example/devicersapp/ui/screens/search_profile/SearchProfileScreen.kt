package com.example.devicersapp.ui.screens.search_profile

import com.example.devicersapp.ui.theme.LocalDevicersColors

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.screens.search_profile.components.SearchProfileFilterPanel
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.ScreenTitleText
import com.example.devicersapp.ui.utils.navigation.SearchBar
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/**
 * Configura la búsqueda de perfiles y los filtros con los que se acota.
 *
 * @param onApplyFilters Acción solicitada al aplicar los filtros visibles.
 * @param modifier Modificador aplicado a la pantalla.
 */
@Composable
fun SearchProfileScreen(
    onApplyFilters: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // La pantalla es propietaria del estado y los componentes reciben valores y callbacks.
    var searchText by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var interests by remember { mutableStateOf("") }
    var minimumReviews by remember { mutableFloatStateOf(20f) }
    var relationship by remember { mutableStateOf("all") }
    var sortBy by remember { mutableStateOf("alphabetical") }

    SearchProfileScreenContent(
        searchText = searchText,
        onSearchTextChange = { searchText = it },
        username = username,
        onUsernameChange = { username = it },
        interests = interests,
        onInterestsChange = { interests = it },
        minimumReviews = minimumReviews,
        onMinimumReviewsChange = { minimumReviews = it },
        relationship = relationship,
        onRelationshipChange = { relationship = it },
        sortBy = sortBy,
        onSortChange = { sortBy = it },
        onClearFilters = {
            username = ""
            interests = ""
            minimumReviews = 20f
            relationship = "all"
            sortBy = "alphabetical"
        },
        onApplyFilters = onApplyFilters,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Reúne el título, la búsqueda general y la tarjeta de filtros de perfiles.
 *
 * @param searchText Texto actual de la búsqueda general.
 * @param onSearchTextChange Acción que solicita actualizar la búsqueda general.
 * @param username Nombre de usuario escrito para filtrar.
 * @param onUsernameChange Acción que solicita actualizar el nombre de usuario.
 * @param interests Intereses escritos para filtrar.
 * @param onInterestsChange Acción que solicita actualizar los intereses.
 * @param minimumReviews Cantidad mínima de reseñas publicadas.
 * @param onMinimumReviewsChange Acción que solicita cambiar la cantidad mínima de reseñas.
 * @param relationship Identificador de la relación activa con los perfiles buscados.
 * @param onRelationshipChange Acción que solicita cambiar la relación.
 * @param sortBy Identificador del orden activo.
 * @param onSortChange Acción que solicita cambiar el orden.
 * @param onClearFilters Acción que solicita restablecer los filtros.
 * @param onApplyFilters Acción que solicita aplicar los filtros.
 * @param modifier Modificador aplicado al contenido.
 */
@Composable
fun SearchProfileScreenContent(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    interests: String,
    onInterestsChange: (String) -> Unit,
    minimumReviews: Float,
    onMinimumReviewsChange: (Float) -> Unit,
    relationship: String,
    onRelationshipChange: (String) -> Unit,
    sortBy: String,
    onSortChange: (String) -> Unit,
    onClearFilters: () -> Unit,
    onApplyFilters: () -> Unit,
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
            text = searchText,
            onTextChange = onSearchTextChange
        )

        Spacer(modifier = Modifier.height(20.dp))

        SearchProfileFilterPanel(
            username = username,
            onUsernameChange = onUsernameChange,

            interests = interests,
            onInterestsChange = onInterestsChange,

            minimumReviews = minimumReviews,
            onMinimumReviewsChange = onMinimumReviewsChange,

            relationship = relationship,
            onRelationshipChange = onRelationshipChange,

            sortBy = sortBy,
            onSortChange = onSortChange,
            onClearFilters = onClearFilters,
            onApplyFilters = onApplyFilters
        )

        // Deja aire para que la barra flotante no tape el botón de aplicar.
        Spacer(modifier = Modifier.height(120.dp))
    }
}

/** Muestra una vista previa de la búsqueda de perfiles en el tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 1000)
fun SearchProfileScreenPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(selectedItem = "search", showBottomBar = true) { innerPadding ->
            SearchProfileScreen(
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
            )
        }
    }
}

/** Muestra una vista previa de la búsqueda de perfiles en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 1000)
fun SearchProfileScreenDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(selectedItem = "search", showBottomBar = true) { innerPadding ->
            SearchProfileScreen(
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
            )
        }
    }
}
