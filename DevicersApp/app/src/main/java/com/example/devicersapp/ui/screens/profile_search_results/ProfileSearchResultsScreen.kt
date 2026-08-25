package com.example.devicersapp.ui.screens.profile_search_results

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalProfileProvider
import com.example.devicersapp.ui.models.ProfileSearchResultContent
import com.example.devicersapp.ui.screens.profile_search_results.components.ProfileResultCard
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.SearchControlText
import com.example.devicersapp.ui.utils.navigation.SearchBar
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/** Configura los resultados que devuelve una búsqueda de perfiles. */
@Composable
fun ProfileSearchResultsScreen(
    onProfileClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // La búsqueda llega desde la pantalla anterior y se puede seguir afinando aquí.
    var searchText by remember { mutableStateOf("") }
    // Estado elevado que determina qué tarjetas ya muestran el perfil como seguido.
    var followedProfileIds by remember { mutableStateOf(emptySet<String>()) }

    ProfileSearchResultsScreenContent(
        results = LocalProfileProvider.profiles,
        searchText = searchText,
        onSearchTextChange = { searchText = it },
        followedProfileIds = followedProfileIds,
        onFollow = { profileId ->
            followedProfileIds = followedProfileIds + profileId
        },
        onProfileClick = onProfileClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla la búsqueda, el conteo de coincidencias y la lista de perfiles encontrados.
 *
 * @param results Perfiles devueltos por la búsqueda.
 * @param searchText Texto actual de la búsqueda.
 * @param onSearchTextChange Acción que solicita actualizar la búsqueda.
 * @param followedProfileIds Identificadores de los perfiles que ya se siguen.
 * @param onFollow Acción solicitada al seguir un perfil.
 * @param modifier Modificador aplicado a la lista raíz.
 */
@Composable
fun ProfileSearchResultsScreenContent(
    results: List<ProfileSearchResultContent>,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    followedProfileIds: Set<String>,
    onFollow: (String) -> Unit,
    onProfileClick: (String) -> Unit,
    modifier: Modifier = Modifier
){
    val colors = LocalDevicersColors.current
    Column(modifier = modifier.padding(horizontal = 20.dp)) {
        Spacer(modifier = Modifier.height(28.dp))
        SearchBar(
            placeholder = R.string.profile_search_results_placeholder,
            backgroundColor = colors.surface,
            showSearchIcon = true,
            text = searchText,
            onTextChange = onSearchTextChange
        )
        Spacer(modifier = Modifier.height(18.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                Text(
                    text = stringResource(R.string.profile_search_results_count, results.size),
                    color = colors.textSecondary,
                    style = SearchControlText
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(results, key = { it.id }) { result ->
                ProfileResultCard(
                    result = result,
                    isFollowed = result.id in followedProfileIds,
                    onFollow = { onFollow(result.id) },
                    onProfileClick = {
                        onProfileClick(result.id)
                    }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            item {
                // Deja aire para que la barra flotante no tape el último perfil.
                Spacer(modifier = Modifier.height(110.dp))
            }
        }
    }
}

/** Muestra una vista previa de los resultados de perfiles en el tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 900)
fun ProfileSearchResultsScreenPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(
            selectedItem = "search",
            showBottomBar = true,
            topBarNumber = 7
        ) { innerPadding ->
            ProfileSearchResultsScreen(
                onProfileClick = {},
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding()
                )
            )
        }
    }
}

/** Muestra una vista previa de los resultados de perfiles en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 900)
fun ProfileSearchResultsScreenDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(
            selectedItem = "search",
            showBottomBar = true,
            topBarNumber = 7
        ) { innerPadding ->
            ProfileSearchResultsScreen(
                onProfileClick = {},
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding()
                )
            )
        }
    }
}
