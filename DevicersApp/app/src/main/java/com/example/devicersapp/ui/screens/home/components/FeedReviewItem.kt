package com.example.devicersapp.ui.screens.home.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import android.text.format.DateUtils
import androidx.compose.foundation.Image
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalFeedReviewProvider
import com.example.devicersapp.ui.models.FeedReviewContent
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.FeedReviewActionCountText
import com.example.devicersapp.ui.theme.RatingStarsLargeText
import com.example.devicersapp.ui.theme.ReviewContentText
import com.example.devicersapp.ui.utils.images.localImageResIdFor
import com.example.devicersapp.ui.utils.profile.ProfileAvatar
import com.example.devicersapp.ui.utils.review.ReviewActionsRow
import com.example.devicersapp.ui.utils.rating.RatingStars

/**
 * Muestra una reseña del feed editorial: miniatura del producto, autor, calificación y acciones.
 *
 * @param review Reseña obtenida del backend con su artículo, autor y conteos.
 * @param onProductClick Acción solicitada al abrir el detalle del artículo reseñado.
 * @param onViewMoreClick Acción solicitada al mostrar el detalle completo de la reseña.
 * @param onCommentClick Acción solicitada al abrir los comentarios de la reseña.
 * @param onSendClick Acción solicitada al compartir la reseña.
 * @param modifier Modificador aplicado al contenedor de la reseña.
 */
@Composable
fun FeedReviewItem(
    review: FeedReviewContent,
    onProductClick: () -> Unit = {},
    onViewMoreClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onSendClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current
    val productImageModifier = Modifier
        .size(width = 108.dp, height = 132.dp)
        .clip(RoundedCornerShape(16.dp))
        .clickable(onClick = onProductClick)
    // El backend envía el nombre de un drawable local; si no lo reconoce, el valor se trata como URL.
    val productImageResId = localImageResIdFor(review.productImage)
    val authorImageResId = localImageResIdFor(review.authorImage)
    // La antigüedad se calcula con el formato relativo del sistema, ya traducido al idioma del dispositivo.
    val timeAgo = DateUtils.getRelativeTimeSpanString(
        review.createdAtMillis,
        System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS
    ).toString()

    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            // La miniatura se muestra directamente, sin marco ni superficie intermedia.
            if (productImageResId != null) {
                Image(
                    painter = painterResource(productImageResId),
                    contentDescription = stringResource(R.string.review_product_image),
                    modifier = productImageModifier,
                    contentScale = ContentScale.Fit
                )
            } else {
                // Sin imagen o con una URL inválida se muestra el ícono de la app como respaldo neutro.
                AsyncImage(
                    model = review.productImage,
                    contentDescription = stringResource(R.string.review_product_image),
                    placeholder = painterResource(R.drawable.logo_icono_claro),
                    error = painterResource(R.drawable.logo_icono_claro),
                    modifier = productImageModifier,
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ProfileAvatar(
                        // Sin foto propia se usa el avatar por defecto, como en el resto de la app.
                        avatarResId = authorImageResId ?: R.drawable.profile_avatar_00,
                        imageUrl = review.authorImage.takeIf { authorImageResId == null },
                        modifier = Modifier.size(34.dp)
                    )
                    Spacer(modifier = Modifier.width(9.dp))
                    Text(
                        text = stringResource(R.string.home_feed_author_handle, review.authorUsername),
                        modifier = Modifier.weight(1f),
                        color = colors.textPrimary,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = timeAgo,
                        color = colors.textSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(
                        R.string.home_feed_product_metadata,
                        review.productBrand,
                        review.productCategory
                    ),
                    color = colors.textSecondary,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = review.productName,
                    modifier = Modifier.clickable(onClick = onProductClick),
                    color = colors.textPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RatingStars(rating = review.rating, style = RatingStarsLargeText)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.home_feed_product_average, review.productAverage),
                        color = colors.textPrimary,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // El título es opcional en el backend; solo ocupa espacio cuando la reseña lo tiene.
        if (!review.title.isNullOrBlank()) {
            Text(
                text = review.title,
                color = colors.textPrimary,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))
        }

        Text(
            text = review.body,
            color = colors.textSecondary,
            style = ReviewContentText,
            // El feed muestra un adelanto de la reseña; el detalle completo vive en su pantalla.
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        TextButton(
            onClick = onViewMoreClick,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = stringResource(R.string.review_show_more),
                color = colors.primary,
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        ReviewActionsRow(
            likes = review.likes,
            comments = review.comments,
            // El feed prioriza estas acciones con una escala 30 % superior a la del detalle.
            iconSize = 24.7.dp,
            countTextStyle = FeedReviewActionCountText,
            onCommentClick = onCommentClick,
            onSendClick = onSendClick
        )
    }
}

/** Muestra una vista previa de una reseña del feed editorial. */
@Composable
@Preview(showBackground = true)
fun FeedReviewItemPreview() {
    DevicersAppTheme {
        FeedReviewItem(
            review = LocalFeedReviewProvider.reviews.first(),
            modifier = Modifier.padding(16.dp)
        )
    }
}
