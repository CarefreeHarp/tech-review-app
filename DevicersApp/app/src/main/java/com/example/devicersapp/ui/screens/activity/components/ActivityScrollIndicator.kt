package com.example.devicersapp.ui.screens.activity.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Muestra a un costado cuánto se ha recorrido la lista de actividad.
 *
 * El tamaño y la posición del indicador se derivan del estado de la lista, de modo que
 * el usuario perciba cuánta actividad queda por leer.
 *
 * @param listState Estado de desplazamiento de la lista que acompaña el indicador.
 * @param modifier Modificador aplicado al indicador.
 */
@Composable
fun ActivityScrollIndicator(
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    // La proporción visible y el avance se recalculan solo cuando el desplazamiento cambia.
    val visibleFraction by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val visibleItems = listState.layoutInfo.visibleItemsInfo.size
            if (totalItems == 0) 1f else (visibleItems.toFloat() / totalItems).coerceIn(0.15f, 1f)
        }
    }
    val scrolledFraction by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val visibleItems = listState.layoutInfo.visibleItemsInfo.size
            val lastStartIndex = (totalItems - visibleItems).coerceAtLeast(1)
            (listState.firstVisibleItemIndex.toFloat() / lastStartIndex).coerceIn(0f, 1f)
        }
    }

    Box(
        modifier = modifier
            .width(4.dp)
            .fillMaxHeight()
            .background(colors.border, RoundedCornerShape(2.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                // El recorrido restante determina dónde se dibuja el tramo resaltado.
                .layout { measurable, constraints ->
                    val thumbHeight = (constraints.maxHeight * visibleFraction).toInt()
                    val availableTravel = constraints.maxHeight - thumbHeight
                    val placeable = measurable.measure(
                        constraints.copy(minHeight = thumbHeight, maxHeight = thumbHeight)
                    )
                    layout(placeable.width, constraints.maxHeight) {
                        placeable.placeRelative(0, (availableTravel * scrolledFraction).toInt())
                    }
                }
                .background(colors.primary, RoundedCornerShape(2.dp))
        )
    }
}

/** Muestra una vista previa del indicador de desplazamiento. */
@Composable
@Preview(showBackground = true, heightDp = 200)
fun ActivityScrollIndicatorPreview() {
    DevicersAppTheme {
        ActivityScrollIndicator(
            listState = rememberLazyListState(),
            modifier = Modifier.padding(16.dp)
        )
    }
}
