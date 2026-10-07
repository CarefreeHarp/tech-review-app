package com.example.devicersapp.ui.screens.found_products.components

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
 * Anticipa el conteo y las tarjetas horizontales de los productos encontrados.
 * @param modifier Modificador aplicado al contenedor de la estructura de carga.
 */
@Composable
fun FoundProductsSkeleton(modifier: Modifier = Modifier) {
    val colors = LocalDevicersColors.current
    val description = stringResource(R.string.screen_loading)
    LazyColumn(
        modifier = modifier.fillMaxSize().semantics { contentDescription = description },
        contentPadding = PaddingValues(bottom = 110.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { SkeletonBlock(Modifier.width(160.dp).height(16.dp)) }
        items(6) {
            Row(
                modifier = Modifier.fillMaxWidth().background(colors.surface, RoundedCornerShape(18.dp)).padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SkeletonBlock(Modifier.size(62.dp), RoundedCornerShape(14.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SkeletonBlock(Modifier.fillMaxWidth().height(18.dp))
                    SkeletonBlock(Modifier.fillMaxWidth(0.65f).height(12.dp))
                    SkeletonBlock(Modifier.fillMaxWidth(0.5f).height(16.dp))
                }
                SkeletonBlock(Modifier.size(18.dp))
            }
        }
    }
}

/** Muestra la estructura de carga sin depender del backend, en ambos temas. */
@Preview(showBackground = true, heightDp = 900)
@Preview(showBackground = true, heightDp = 900, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FoundProductsSkeletonPreview() {
    DevicersAppTheme {
        Box(Modifier.fillMaxSize().background(LocalDevicersColors.current.background)) {
            FoundProductsSkeleton()
        }
    }
}
