package com.example.devicersapp.ui.screens.found_products.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.devicersapp.R
import com.example.devicersapp.data.dto.ProductDto
import com.example.devicersapp.ui.theme.CardHighlightText
import com.example.devicersapp.ui.theme.CardMetadataText
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.rating.RatingStars
import kotlin.math.roundToInt

/**
 * Muestra un producto obtenido desde el backend.
 */
@Composable
fun FoundProductCard(
    product: ProductDto,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val colors = LocalDevicersColors.current

    /*
     * El endpoint de artículos puede incluir las reseñas de manera anidada.
     * Si las incluye, calculamos el promedio.
     * Si no, simplemente no mostramos la calificación.
     */
    val reviews = product.reviews ?: emptyList()

    val averageRating =
        if (reviews.isNotEmpty()) {
            reviews.map { it.rating }.average()
        } else {
            null
        }

    val roundedRating =
        averageRating
            ?.roundToInt()
            ?.coerceIn(1, 5)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(18.dp)
            )
            .background(
                color = colors.surface,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable {
                onClick()
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = product.imageUrl,
            contentDescription = product.name,
            placeholder = painterResource(
                R.drawable.device_00
            ),
            error = painterResource(
                R.drawable.device_00
            ),
            modifier = Modifier
                .size(62.dp)
                .clip(
                    RoundedCornerShape(14.dp)
                ),
            contentScale = ContentScale.Fit
        )

        Spacer(
            modifier = Modifier.width(14.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = product.name,
                color = colors.textPrimary,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = product.model ?: "Sin modelo",
                color = colors.textSecondary,
                style = CardMetadataText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (roundedRating != null) {

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    RatingStars(
                        rating = roundedRating
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = String.format(
                            "%.1f",
                            averageRating
                        ),
                        color = colors.textPrimary,
                        style = CardHighlightText
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        Icon(
            painter = painterResource(
                R.drawable.back_icon
            ),
            contentDescription = stringResource(
                R.string.found_products_open
            ),
            modifier = Modifier
                .size(18.dp)
                .rotate(180f),
            tint = colors.textSecondary
        )
    }
}