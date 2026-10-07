package com.example.devicersapp.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.loading.SkeletonBlock

/**
 * Reserva las miniaturas, autores, texto y acciones de las reseñas del feed.
 * @param modifier Modificador aplicado al contenedor de la estructura de carga.
 */
@Composable
fun HomeSkeleton(modifier: Modifier = Modifier) {
    val colors = LocalDevicersColors.current
    val description = stringResource(R.string.screen_loading)
    LazyColumn(
        modifier = modifier.fillMaxSize().semantics { contentDescription = description },
        contentPadding = PaddingValues(top = 20.dp, bottom = 110.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(3) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SkeletonBlock(Modifier.size(width = 108.dp, height = 132.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                            SkeletonBlock(Modifier.size(34.dp), CircleShape)
                            SkeletonBlock(Modifier.weight(1f).height(14.dp))
                        }
                        SkeletonBlock(Modifier.fillMaxWidth(0.65f).height(12.dp))
                        SkeletonBlock(Modifier.fillMaxWidth().height(20.dp))
                        SkeletonBlock(Modifier.fillMaxWidth(0.7f).height(18.dp))
                    }
                }
                SkeletonBlock(Modifier.fillMaxWidth(0.7f).height(16.dp))
                SkeletonBlock(Modifier.fillMaxWidth().height(12.dp))
                SkeletonBlock(Modifier.fillMaxWidth().height(12.dp))
                SkeletonBlock(Modifier.fillMaxWidth(0.6f).height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    repeat(3) { SkeletonBlock(Modifier.width(44.dp).height(24.dp)) }
                }
                HorizontalDivider(color = colors.border)
            }
        }
    }
}

/** Muestra la estructura de carga sin depender del backend, en ambos temas. */
@Preview(showBackground = true, heightDp = 900)
@Preview(showBackground = true, heightDp = 900, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeSkeletonPreview() {
    DevicersAppTheme {
        Box(Modifier.fillMaxSize().background(LocalDevicersColors.current.background)) {
            HomeSkeleton()
        }
    }
}
