package com.example.devicersapp.ui.screens.activity

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalActivityScreenProvider
import com.example.devicersapp.ui.models.ActivityGroupContent
import com.example.devicersapp.ui.screens.activity.components.ActivityScrollIndicator
import com.example.devicersapp.ui.screens.activity.components.ActivitySection
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.SearchScreenTitleText
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/** Configura la pantalla de actividad, donde se reúne todo lo que ocurre alrededor de las reseñas. */
@Composable
fun ActivityScreen(modifier: Modifier = Modifier) {
    // Estado elevado que determina qué tarjetas deben mostrar la acción "Siguiendo".
    var followedActivityIds by remember { mutableStateOf(emptySet<String>()) }
    // La referencia temporal se conserva para que los textos relativos no cambien en cada recomposición.
    val activityGroups = remember {
        LocalActivityScreenProvider.activityGroups(currentTimeMillis = System.currentTimeMillis())
    }

    ActivityScreenContent(
        activityGroups = activityGroups,
        followedActivityIds = followedActivityIds,
        onFollow = { activityId -> followedActivityIds = followedActivityIds + activityId },
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla el encabezado y la lista agrupada de eventos de actividad.
 *
 * @param activityGroups Grupos locales de eventos que se mostrarán.
 * @param followedActivityIds Identificadores de eventos cuyos autores ya se siguen.
 * @param onFollow Acción solicitada al seleccionar seguir.
 * @param modifier Modificador aplicado al contenido.
 */
@Composable
fun ActivityScreenContent(
    activityGroups: List<ActivityGroupContent>,
    followedActivityIds: Set<String>,
    onFollow: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current
    val listState = rememberLazyListState()

    Column(modifier = modifier.padding(horizontal = 20.dp)) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.activity_title),
            style = SearchScreenTitleText,
            color = colors.textPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.activity_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = colors.textSecondary
        )
        Spacer(modifier = Modifier.height(20.dp))

        // El indicador acompaña a la lista, por eso ambos comparten la misma altura.
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                state = listState,
                modifier = Modifier.padding(end = 12.dp),
                contentPadding = PaddingValues(bottom = 110.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                itemsIndexed(activityGroups, key = { _, group -> group.id }) { index, group ->
                    ActivitySection(
                        titleResId = group.titleResId,
                        activities = group.activities,
                        followedActivityIds = followedActivityIds,
                        onFollow = onFollow,
                        // Solo el periodo más reciente se presenta como tarjetas elevadas.
                        isHighlighted = index == 0
                    )
                }
            }

            ActivityScrollIndicator(
                listState = listState,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(vertical = 4.dp)
            )
        }
    }
}

/** Muestra una vista previa de la pantalla de actividad en el tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 900)
fun ActivityScreenPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(selectedItem = "activity", showBottomBar = true) { innerPadding ->
            ActivityScreen(modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
        }
    }
}

/** Muestra una vista previa de la pantalla de actividad en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 900)
fun ActivityScreenDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(selectedItem = "activity", showBottomBar = true) { innerPadding ->
            ActivityScreen(modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
        }
    }
}
