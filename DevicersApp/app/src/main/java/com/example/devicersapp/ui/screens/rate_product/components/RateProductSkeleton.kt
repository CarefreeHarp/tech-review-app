package com.example.devicersapp.ui.screens.rate_product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
 * Reserva el producto, la calificación y los campos del formulario de reseña.
 * @param modifier Modificador aplicado al contenedor de la estructura de carga.
 */
@Composable
fun RateProductSkeleton(modifier: Modifier = Modifier) {
    val colors = LocalDevicersColors.current
    val description = stringResource(R.string.screen_loading)
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 20.dp).semantics { contentDescription = description },
        contentPadding = PaddingValues(top = 20.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth().height(90.dp).background(colors.surface, RoundedCornerShape(16.dp)).padding(horizontal = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SkeletonBlock(Modifier.size(60.dp), RoundedCornerShape(14.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SkeletonBlock(Modifier.fillMaxWidth().height(20.dp))
                    SkeletonBlock(Modifier.fillMaxWidth(0.6f).height(12.dp))
                }
            }
        }
        item {
            SkeletonBlock(Modifier.width(150.dp).height(20.dp))
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(5) { SkeletonBlock(Modifier.weight(1f).height(48.dp)) }
            }
        }
        items(4) { index ->
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SkeletonBlock(Modifier.width(140.dp).height(16.dp))
                SkeletonBlock(Modifier.fillMaxWidth().height(if (index == 0) 56.dp else 112.dp), RoundedCornerShape(12.dp))
            }
        }
        item { SkeletonBlock(Modifier.fillMaxWidth().height(52.dp), RoundedCornerShape(12.dp)) }
    }
}

/** Muestra la estructura de carga sin depender del backend, en ambos temas. */
@Preview(showBackground = true, heightDp = 900)
@Preview(showBackground = true, heightDp = 900, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RateProductSkeletonPreview() {
    DevicersAppTheme {
        Box(Modifier.fillMaxSize().background(LocalDevicersColors.current.background)) {
            RateProductSkeleton()
        }
    }
}
