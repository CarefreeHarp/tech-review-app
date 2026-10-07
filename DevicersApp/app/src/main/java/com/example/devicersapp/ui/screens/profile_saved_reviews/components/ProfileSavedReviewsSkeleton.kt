package com.example.devicersapp.ui.screens.profile_saved_reviews.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
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
import com.example.devicersapp.ui.utils.tabs.SectionTabsRow

/**
 * Anticipa la identidad y la cuadrícula del perfil mientras se consultan sus datos.
 * @param onReviewsClick Acción para regresar a las reseñas aun durante la carga.
 * @param modifier Modificador aplicado al contenedor de la estructura de carga.
 */
@Composable
fun ProfileSavedReviewsSkeleton(onReviewsClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalDevicersColors.current
    val description = stringResource(R.string.screen_loading)
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize().padding(horizontal = 20.dp).semantics { contentDescription = description },
        contentPadding = PaddingValues(top = 16.dp, bottom = 110.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SkeletonBlock(Modifier.size(84.dp), CircleShape)
                SkeletonBlock(Modifier.fillMaxWidth(0.55f).height(18.dp))
                SectionTabsRow(
                    startLabelResId = R.string.profile_reviews,
                    endLabelResId = R.string.profile_saved,
                    isStartSelected = false,
                    onStartClick = onReviewsClick,
                    onEndClick = {},
                    selectedColor = colors.primaryText
                )
            }
        }
        items(6) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SkeletonBlock(Modifier.fillMaxWidth().aspectRatio(1f), RoundedCornerShape(12.dp))
                SkeletonBlock(Modifier.fillMaxWidth(0.75f).height(12.dp))
                SkeletonBlock(Modifier.width(90.dp).height(16.dp))
            }
        }
    }
}

/** Muestra la estructura de carga sin depender del backend, en ambos temas. */
@Preview(showBackground = true, heightDp = 900)
@Preview(showBackground = true, heightDp = 900, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProfileSavedReviewsSkeletonPreview() {
    DevicersAppTheme {
        Box(Modifier.fillMaxSize().background(LocalDevicersColors.current.background)) {
            ProfileSavedReviewsSkeleton(onReviewsClick = {})
        }
    }
}
