package com.example.devicersapp.ui.screens.activity

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.ui.models.ActivityContent
import com.example.devicersapp.ui.models.ActivityGroupContent
import com.example.devicersapp.ui.models.ActivityType
import com.example.devicersapp.ui.screens.activity.components.ActivityCard
import com.example.devicersapp.ui.screens.activity.components.ActivitySection
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.data.local.LocalActivityProvider

/** Configura la pantalla de actividad y observa su estado desde el ViewModel. */
@Composable
fun ActivityView(
    onReviewClick: (Int) -> Unit = {},
    onProfileClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ActivityViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    ActivityViewContent(
        state = uiState,
        onFollow = viewModel::followActivity,
        onActivityClick = { activity ->
            when (activity.type) {
                ActivityType.LIKE,
                ActivityType.COMMENT ->
                    onReviewClick(requireNotNull(activity.targetReviewId))

                ActivityType.FOLLOW ->
                    onProfileClick(requireNotNull(activity.targetProfileId))
            }
        },
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla la lista agrupada de eventos de actividad.
 *
 * @param state Estado inmutable con los grupos y seguimientos visibles.
 * @param onFollow Acción solicitada al seleccionar seguir.
 * @param onActivityClick Acción solicitada al abrir el destino de una notificación.
 * @param modifier Modificador aplicado al contenido.
 */
@Composable
fun ActivityViewContent(
    state: ActivityState,
    onFollow: (String) -> Unit,
    onActivityClick: (ActivityContent) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current
    LazyColumn(
        modifier = modifier.padding(horizontal = 20.dp),
        contentPadding = PaddingValues(bottom = 110.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        state.activityGroups.forEachIndexed { groupIndex, group ->
            item(key = "${group.id}_header") {
                ActivitySection(titleResId = group.titleResId)
            }
            items(
                items = group.notifications,
                key = { activity -> activity.id }
            ) { activity ->
                // Cada actividad es un ítem independiente para que LazyColumn la componga bajo demanda.
                ActivityCard(
                    activity = activity,
                    isFollowed = activity.id in state.followedActivityIds,
                    onFollow = { onFollow(activity.id) },
                    onClick = { onActivityClick(activity) },
                    isHighlighted = groupIndex == 0
                )
            }
        }
    }
}

/** Muestra una vista previa de la pantalla de actividad en el tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 900)
fun ActivityScreenPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(
            selectedItem = "activity",
            showBottomBar = true,
            topBarNumber = 10
        ) { innerPadding ->
            ActivityViewContent(
                state = ActivityState(
                    activityGroups = LocalActivityProvider.activityGroups(
                        currentTimeMillis = System.currentTimeMillis()
                    )
                ),
                onFollow = {},
                onActivityClick = {},
                modifier = Modifier
                    .padding(
                        top = innerPadding.calculateTopPadding()
                    )
                    .fillMaxSize()
                    .background(LocalDevicersColors.current.background)
            )
        }
    }
}

/** Muestra una vista previa de la pantalla de actividad en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 900)
fun ActivityScreenDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(
            selectedItem = "activity",
            showBottomBar = true,
            topBarNumber = 10
        ) { innerPadding ->
            ActivityViewContent(
                state = ActivityState(
                    activityGroups = LocalActivityProvider.activityGroups(
                        currentTimeMillis = System.currentTimeMillis()
                    )
                ),
                onFollow = {},
                onActivityClick = {},
                modifier = Modifier
                    .padding(
                        top = innerPadding.calculateTopPadding()
                    )
                    .fillMaxSize()
                    .background(LocalDevicersColors.current.background)
            )
        }
    }
}
