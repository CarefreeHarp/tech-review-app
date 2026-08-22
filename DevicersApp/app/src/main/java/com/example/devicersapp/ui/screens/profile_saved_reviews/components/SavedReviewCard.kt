package com.example.devicersapp.ui.screens.profile_saved_reviews.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.SavedReviewContent
import com.example.devicersapp.ui.theme.CardHighlightText
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.rating.RatingStars

/**
 * Muestra una reseña guardada, encabezada por una zona amplia dedicada a la foto del producto.
 *
 * @param savedReview Contenido de la reseña guardada.
 * @param modifier Modificador aplicado a la tarjeta.
 * @param onRemoveClick Acción solicitada al quitar la reseña de los guardados.
 */
@Composable
fun SavedReviewCard(
    savedReview: SavedReviewContent,
    modifier: Modifier = Modifier,
    onRemoveClick: () -> Unit = {}
) {
    val colors = LocalDevicersColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(158.dp)
            // La sombra suave despega la tarjeta del fondo, como en el diseño editorial.
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(20.dp))
            .background(colors.surface, RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        // La foto del producto ocupa un bloque propio que domina el lado izquierdo.
        Box(
            modifier = Modifier
                .width(120.dp)
                .fillMaxHeight()
                .background(colors.background, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(savedReview.productImageResId),
                contentDescription = stringResource(savedReview.imageDescriptionResId),
                modifier = Modifier.size(84.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = stringResource(savedReview.authorResId),
                    modifier = Modifier.weight(1f),
                    color = colors.textSecondary,
                    style = CardHighlightText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                // El marcador va en acento porque la reseña ya está guardada.
                Icon(
                    painter = painterResource(R.drawable.bookmark_icon),
                    contentDescription = stringResource(R.string.review_action_save),
                    modifier = Modifier
                        .size(19.dp)
                        .clickable { onRemoveClick() },
                    tint = colors.primaryText
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(savedReview.productNameResId),
                color = colors.textPrimary,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                RatingStars(rating = savedReview.rating)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(savedReview.averageResId),
                    color = colors.textPrimary,
                    style = CardHighlightText
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(savedReview.textResId).trim(),
                color = colors.textSecondary,
                style = MaterialTheme.typography.bodySmall,
                // La tarjeta muestra un adelanto; la reseña completa vive en su propia pantalla.
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/** Muestra una vista previa de una reseña guardada. */
@Composable
@Preview(showBackground = true)
fun SavedReviewCardPreview() {
    DevicersAppTheme {
        SavedReviewCard(
            SavedReviewContent(
                productNameResId = R.string.profile_saved_first_product,
                productImageResId = R.drawable.device_01,
                imageDescriptionResId = R.string.profile_saved_image,
                authorResId = R.string.profile_saved_first_author,
                rating = 5,
                averageResId = R.string.profile_saved_first_average,
                textResId = R.string.profile_saved_first_text
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
