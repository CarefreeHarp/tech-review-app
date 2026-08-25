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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalActivityProvider
import com.example.devicersapp.ui.models.ActivityContent
import com.example.devicersapp.ui.models.ActivityGroupContent
import com.example.devicersapp.ui.models.ActivityType
import com.example.devicersapp.ui.screens.activity.components.ActivityCard
import com.example.devicersapp.ui.screens.activity.components.ActivitySection
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/** Configura la pantalla de actividad, donde se reúne todo lo que ocurre alrededor de las reseñas. */
@Composable
fun ActivityScreen(
    onReviewClick: (Int) -> Unit = {},
    onProfileClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Estado elevado que determina qué tarjetas deben mostrar la acción "Siguiendo".
    var followedActivityIds by remember { mutableStateOf(emptySet<String>()) }
    // La referencia temporal se conserva para que los textos relativos no cambien en cada recomposición.
    val activityGroups = remember {
        LocalActivityProvider.activityGroups(currentTimeMillis = System.currentTimeMillis())
    }

    ActivityScreenContent(
        activityGroups = activityGroups,
        followedActivityIds = followedActivityIds,
        onFollow = { activityId -> followedActivityIds = followedActivityIds + activityId },
        onActivityClick = { activity ->
            when (activity.type) {
                ActivityType.LIKE, ActivityType.COMMENT -> onReviewClick(requireNotNull(activity.targetReviewId))
                ActivityType.FOLLOW -> onProfileClick(requireNotNull(activity.targetProfileId))
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
 * @param activityGroups Grupos locales de eventos que se mostrarán.
 * @param followedActivityIds Identificadores de eventos cuyos autores ya se siguen.
 * @param onFollow Acción solicitada al seleccionar seguir.
 * @param onActivityClick Acción solicitada al abrir el destino de una notificación.
 * @param modifier Modificador aplicado al contenido.
 */
@Composable
fun ActivityScreenContent(
    activityGroups: List<ActivityGroupContent>,
    followedActivityIds: Set<String>,
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
        activityGroups.forEachIndexed { groupIndex, group ->
            item(key = "${group.id}_header") {
                ActivitySection(titleResId = group.titleResId)
            }
            items(
                items = group.activities,
                key = { activity -> activity.id }
            ) { activity ->
                // Cada actividad es un ítem independiente para que LazyColumn la componga bajo demanda.
                ActivityCard(
                    activity = activity,
                    isFollowed = activity.id in followedActivityIds,
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
        DevicersScaffold(selectedItem = "activity", showBottomBar = true, topBarNumber = 10) { innerPadding ->
            ActivityScreen(modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
        }
    }
}

/** Muestra una vista previa de la pantalla de actividad en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 900)
fun ActivityScreenDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(selectedItem = "activity", showBottomBar = true, topBarNumber = 10) { innerPadding ->
            ActivityScreen(modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
        }
    }
}
