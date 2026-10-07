package com.example.devicersapp.ui.screens.create_review.components

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
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
import com.example.devicersapp.ui.theme.SearchHeadingText
import com.example.devicersapp.ui.utils.loading.SkeletonBlock

/**
 * Anticipa categorías y productos sugeridos, conservando sus proporciones.
 * @param modifier Modificador aplicado al contenedor de la estructura de carga.
 */
@Composable
fun CreateReviewSkeleton(modifier: Modifier = Modifier) {
    val colors = LocalDevicersColors.current
    val description = stringResource(R.string.screen_loading)
    Column(modifier = modifier.fillMaxSize().semantics { contentDescription = description }) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(4) { SkeletonBlock(Modifier.width(88.dp).height(36.dp), RoundedCornerShape(50)) }
        }
        Spacer(Modifier.height(11.dp))
        Text(stringResource(R.string.create_review_suggested), color = colors.textPrimary, style = SearchHeadingText)
        Spacer(Modifier.height(16.dp))
        HorizontalDivider(thickness = 3.dp, color = colors.border)
        LazyColumn(contentPadding = PaddingValues(top = 12.dp, bottom = 120.dp)) {
            items(7) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SkeletonBlock(Modifier.size(58.dp), RoundedCornerShape(14.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SkeletonBlock(Modifier.fillMaxWidth().height(16.dp))
                        SkeletonBlock(Modifier.fillMaxWidth(0.65f).height(12.dp))
                    }
                    SkeletonBlock(Modifier.width(88.dp).height(40.dp), RoundedCornerShape(50))
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
fun CreateReviewSkeletonPreview() {
    DevicersAppTheme {
        Box(Modifier.fillMaxSize().background(LocalDevicersColors.current.background)) {
            CreateReviewSkeleton()
        }
    }
}
