package com.example.devicersapp.ui.screens.product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
 * Reserva el título, imagen, resumen de calificaciones y reseñas del producto.
 * @param modifier Modificador aplicado al contenedor de la estructura de carga.
 */
@Composable
fun ProductSkeleton(modifier: Modifier = Modifier) {
    val colors = LocalDevicersColors.current
    val description = stringResource(R.string.screen_loading)
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 20.dp).semantics { contentDescription = description },
        contentPadding = PaddingValues(bottom = 110.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SkeletonBlock(Modifier.fillMaxWidth(0.75f).height(28.dp))
                SkeletonBlock(Modifier.fillMaxWidth(0.4f).height(12.dp))
                HorizontalDivider(thickness = 3.dp, color = colors.border)
            }
        }
        item { SkeletonBlock(Modifier.fillMaxWidth().height(230.dp), RoundedCornerShape(20.dp)) }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SkeletonBlock(Modifier.fillMaxWidth().height(14.dp))
                SkeletonBlock(Modifier.fillMaxWidth(0.8f).height(14.dp))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.CenterVertically) {
                SkeletonBlock(Modifier.width(80.dp).height(80.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(5) { SkeletonBlock(Modifier.fillMaxWidth().height(10.dp)) }
                }
            }
        }
        item { SkeletonBlock(Modifier.fillMaxWidth().height(52.dp), RoundedCornerShape(12.dp)) }
        item { SkeletonBlock(Modifier.fillMaxWidth(0.6f).height(20.dp)) }
        items(2) {
            Column(
                modifier = Modifier.fillMaxWidth().background(colors.surface, RoundedCornerShape(16.dp)).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    SkeletonBlock(Modifier.size(40.dp), CircleShape)
                    SkeletonBlock(Modifier.fillMaxWidth(0.6f).height(16.dp))
                }
                SkeletonBlock(Modifier.fillMaxWidth().height(12.dp))
                SkeletonBlock(Modifier.fillMaxWidth(0.8f).height(12.dp))
            }
        }
    }
}

/** Muestra la estructura de carga sin depender del backend, en ambos temas. */
@Preview(showBackground = true, heightDp = 900)
@Preview(showBackground = true, heightDp = 900, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProductSkeletonPreview() {
    DevicersAppTheme {
        Box(Modifier.fillMaxSize().background(LocalDevicersColors.current.background)) {
            ProductSkeleton()
        }
    }
}
