package com.example.devicersapp.ui.screens.profile_search_results

import com.example.devicersapp.ui.screens.profile_search_results.components.ProfileSearchResultsSkeleton

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProfileSearchResultContent
import com.example.devicersapp.ui.screens.profile_search_results.components.ProfileResultCard
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.SearchControlText
import com.example.devicersapp.ui.utils.navigation.SearchBar
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.navigation.TopBarStyle

/** Renderiza los resultados de perfiles y delega sus eventos al ViewModel. */
@Composable
fun ProfileSearchResultsView(
    onProfileClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileSearchResultsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.loadResults() }

    ProfileSearchResultsViewContent(
        state = uiState,
        onRetry = viewModel::loadResults,
        onSearchTextChange = viewModel::onSearchTextChange,
        onFollow = viewModel::onFollowClick,
        onProfileClick = onProfileClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/** Ensambla la búsqueda, el conteo de coincidencias y la lista de perfiles encontrados. */
@Composable
fun ProfileSearchResultsViewContent(
    state: ProfileSearchResultsState,
    onRetry: () -> Unit = {},
    onSearchTextChange: (String) -> Unit,
    onFollow: (String) -> Unit,
    onProfileClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current
    val results = state.results

    Column(modifier = modifier.padding(horizontal = 20.dp)) {
        state.error?.let { Text(stringResource(it)); androidx.compose.material3.TextButton(onClick = onRetry) { Text(stringResource(R.string.discovery_retry)) } }
        state.followError?.let { Text(stringResource(it), color = colors.textSecondary) }
        Spacer(modifier = Modifier.height(28.dp))
        SearchBar(
            placeholder = R.string.profile_search_results_placeholder,
            backgroundColor = colors.surface,
            showSearchIcon = true,
            text = state.searchText,
            onTextChange = onSearchTextChange
        )
        Spacer(modifier = Modifier.height(18.dp))
        if (state.loading) {
            ProfileSearchResultsSkeleton(modifier = Modifier.weight(1f))
            return@Column
        }
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
                    isFollowed = result.id.toIntOrNull() in state.followedUserIds,
                    followEnabled = result.id.toIntOrNull() !in state.followsInProgress && !state.loading,
                    onFollow = { onFollow(result.id) },
                    onProfileClick = { onProfileClick(result.id) }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
            item {
                Spacer(modifier = Modifier.height(110.dp))
            }
        }
    }
}

/** Muestra los resultados de perfiles en tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 900)
fun ProfileSearchResultsViewPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(selectedItem = "search", showBottomBar = true, topBarStyle = TopBarStyle.ProfileSearchResults) {
                innerPadding ->
            ProfileSearchResultsViewContent(
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                state = ProfileSearchResultsState(results = emptyList()),
                onSearchTextChange = {},
                onFollow = {},
                onProfileClick = {}
            )
        }
    }
}

/** Muestra los resultados de perfiles en tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 900)
fun ProfileSearchResultsViewDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(selectedItem = "search", showBottomBar = true, topBarStyle = TopBarStyle.ProfileSearchResults) {
                innerPadding ->
            ProfileSearchResultsViewContent(
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                state = ProfileSearchResultsState(results = emptyList()),
                onSearchTextChange = {},
                onFollow = {},
                onProfileClick = {}
            )
        }
    }
}
