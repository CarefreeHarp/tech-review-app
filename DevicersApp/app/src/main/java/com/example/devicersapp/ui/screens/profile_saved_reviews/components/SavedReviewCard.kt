package com.example.devicersapp.ui.screens.profile_saved_reviews.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalProfileProvider
import com.example.devicersapp.data.local.LocalReviewProvider
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.rating.RatingStars

/**
 * Muestra una reseña guardada en la cuadrícula, con el producto destacado sobre su imagen.
 *
 * @param review Reseña existente que el perfil guardó.
 * @param modifier Modificador aplicado a la tarjeta.
 * @param onClick Acción solicitada al abrir la reseña guardada.
 */
@Composable
fun SavedReviewCard(
    review: ReviewContent,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val colors = LocalDevicersColors.current
    val imageShape = RoundedCornerShape(12.dp)
    val author = requireNotNull(LocalProfileProvider.getProfileById(review.authorId))

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        // El recorte mantiene imágenes de catálogo heterogéneas en una retícula consistente.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                // La elevación da la sensación de que la fotografía flota sobre el fondo.
                .shadow(elevation = 8.dp, shape = imageShape)
                .clip(imageShape)
                .background(colors.surface),
            contentAlignment = Alignment.BottomStart
        ) {
            Image(
                painter = painterResource(review.productImageResId),
                contentDescription = stringResource(R.string.review_product_image),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            // El velo uniforme oscurece la fotografía sin alterar el recurso original.
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(colors.textonPhoto.copy(alpha = 0.50f))
            )
            Text(
                text = stringResource(review.productNameResId),
                modifier = Modifier.padding(12.dp),
                color = colors.textOnPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                // La sombra derivada de la paleta mantiene legibilidad sobre fotografías claras.
                style = MaterialTheme.typography.titleMedium.copy(
                    shadow = Shadow(colors.textPrimary.copy(alpha = 0.6f), Offset(0f, 2f), 4f)
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(author.handleResId),
            color = colors.textPrimary,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(3.dp))

        RatingStars(rating = review.rating)
    }
}

/** Muestra una vista previa de una reseña guardada. */
@Composable
@Preview(showBackground = true)
fun SavedReviewCardPreview() {
    DevicersAppTheme {
        SavedReviewCard(
            review = LocalReviewProvider.reviews.first(),
            modifier = Modifier.padding(16.dp)
        )
    }
}
