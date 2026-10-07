package com.example.devicersapp.ui.screens.review.components

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
 * Anticipa el producto, autor, cuerpo de la reseña, respuestas y compositor.
 * @param modifier Modificador aplicado al contenedor de la estructura de carga.
 */
@Composable
fun ReviewSkeleton(modifier: Modifier = Modifier) {
    val colors = LocalDevicersColors.current
    val description = stringResource(R.string.screen_loading)
    Box(modifier = modifier.fillMaxSize().semantics { contentDescription = description }) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 30.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().background(colors.surface, RoundedCornerShape(20.dp)).padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SkeletonBlock(Modifier.width(136.dp).height(122.dp), RoundedCornerShape(16.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SkeletonBlock(Modifier.fillMaxWidth().height(20.dp))
                        SkeletonBlock(Modifier.fillMaxWidth(0.65f).height(12.dp))
                    }
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        SkeletonBlock(Modifier.size(48.dp), CircleShape)
                        SkeletonBlock(Modifier.fillMaxWidth(0.55f).height(16.dp))
                    }
                    SkeletonBlock(Modifier.width(140.dp).height(22.dp))
                    SkeletonBlock(Modifier.fillMaxWidth(0.8f).height(20.dp))
                    repeat(4) { SkeletonBlock(Modifier.fillMaxWidth().height(14.dp)) }
                    SkeletonBlock(Modifier.fillMaxWidth(0.6f).height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        repeat(3) { SkeletonBlock(Modifier.width(44.dp).height(24.dp)) }
                    }
                    HorizontalDivider(color = colors.border)
                }
            }
            items(3) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SkeletonBlock(Modifier.size(36.dp), CircleShape)
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SkeletonBlock(Modifier.fillMaxWidth(0.5f).height(14.dp))
                        SkeletonBlock(Modifier.fillMaxWidth().height(12.dp))
                        SkeletonBlock(Modifier.fillMaxWidth(0.8f).height(12.dp))
                    }
                }
            }
        }
        SkeletonBlock(Modifier.align(Alignment.BottomCenter).padding(16.dp).fillMaxWidth().height(48.dp), RoundedCornerShape(24.dp))
    }
}

/** Muestra la estructura de carga sin depender del backend, en ambos temas. */
@Preview(showBackground = true, heightDp = 900)
@Preview(showBackground = true, heightDp = 900, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ReviewSkeletonPreview() {
    DevicersAppTheme {
        Box(Modifier.fillMaxSize().background(LocalDevicersColors.current.background)) {
            ReviewSkeleton()
        }
    }
}
