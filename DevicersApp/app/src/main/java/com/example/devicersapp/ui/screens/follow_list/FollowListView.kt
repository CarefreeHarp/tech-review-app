package com.example.devicersapp.ui.screens.follow_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.FollowListSection
import com.example.devicersapp.ui.screens.follow_list.components.FollowListUserItem
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.profile.ProfileLoadStatus
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.tabs.SectionTabsRow
import com.example.devicersapp.ui.utils.navigation.TopBarStyle

/** Observa la lista y comunica al ViewModel los argumentos del perfil de navegación. */
@Composable
fun FollowListView(
    profileId: Int,
    initialSection: FollowListSection,
    viewModel: FollowListViewModel,
    modifier: Modifier = Modifier,
    onProfileClick: (String) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.loadProfile(profileId, initialSection) }
    FollowListViewContent(state = state, onSectionChange = viewModel::selectSection, onRetry = viewModel::reload,
        onProfileClick = onProfileClick,
        modifier = modifier.fillMaxSize().background(LocalDevicersColors.current.background))
}

/** Mantiene visibles las pestañas y muestra la consulta activa como una colección perezosa. */
@Composable
fun FollowListViewContent(
    state: FollowListState,
    onSectionChange: (FollowListSection) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    onProfileClick: (String) -> Unit = {}
) {
    val colors = LocalDevicersColors.current
    Column(modifier = modifier.padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)) {
        SectionTabsRow(startLabelResId = R.string.profile_followers, endLabelResId = R.string.profile_following,
            isStartSelected = state.section == FollowListSection.FOLLOWERS,
            selectedColor = colors.primaryText,
            onStartClick = { onSectionChange(FollowListSection.FOLLOWERS) },
            onEndClick = { onSectionChange(FollowListSection.FOLLOWING) })
        when {
            state.loading -> Box(Modifier.weight(1f).fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = colors.primary)
            }
            state.errorMessageResId != null -> ProfileLoadStatus(state.errorMessageResId, onRetry, Modifier.weight(1f))
            state.users.isEmpty() -> Text(stringResource(R.string.follow_list_empty),
                style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)
            else -> LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.users, key = { it.id }) { user -> FollowListUserItem(user = user, onProfileClick = onProfileClick) }
            }
        }
    }
}

/** Previsualiza la lista sin crear repositorios ni consultar servicios remotos. */
@Preview(showBackground = true, heightDp = 900)
@Composable
fun FollowListViewPreview() {
    DevicersAppTheme {
        DevicersScaffold(topBarStyle = TopBarStyle.FollowList) { padding ->
            FollowListViewContent(state = FollowListState(loading = false), onSectionChange = {},
                onRetry = {}, modifier = Modifier.padding(padding))
        }
    }
}
