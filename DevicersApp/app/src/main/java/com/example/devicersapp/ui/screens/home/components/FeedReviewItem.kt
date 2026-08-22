package com.example.devicersapp.ui.screens.home.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.FeedReviewContent
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.RatingStarsLargeText
import com.example.devicersapp.ui.theme.ReviewContentText
import com.example.devicersapp.ui.utils.profile.ProfileAvatar
import com.example.devicersapp.ui.utils.review.ReviewActionsRow
import com.example.devicersapp.ui.utils.rating.RatingStars

/**
 * Muestra una reseña del feed editorial: miniatura del producto, autor, calificación y acciones.
 *
 * @param review Contenido de la reseña publicada.
 * @param modifier Modificador aplicado al contenedor de la reseña.
 */
@Composable
fun FeedReviewItem(review: FeedReviewContent, modifier: Modifier = Modifier) {
    val colors = LocalDevicersColors.current

    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            // La miniatura grande ancla la reseña al producto reseñado.
            Box(
                modifier = Modifier
                    .size(width = 108.dp, height = 132.dp)
                    .background(colors.surface, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(review.productImageResId),
                    contentDescription = stringResource(R.string.review_product_image),
                    modifier = Modifier.size(86.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ProfileAvatar(
                        avatarResId = review.avatarResId,
                        modifier = Modifier.size(34.dp)
                    )
                    Spacer(modifier = Modifier.width(9.dp))
                    Text(
                        text = stringResource(review.author),
                        modifier = Modifier.weight(1f),
                        color = colors.textPrimary,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = stringResource(review.timeAgo),
                        color = colors.textSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(review.productMetadata),
                    color = colors.textSecondary,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = stringResource(review.productName),
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
                        text = stringResource(review.productAverage),
                        color = colors.textPrimary,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(review.reviewText),
            color = colors.textSecondary,
            style = ReviewContentText,
            // El feed muestra un adelanto de la reseña; el detalle completo vive en su pantalla.
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(14.dp))

        ReviewActionsRow(likes = review.likes, comments = review.comments)
    }
}

/** Muestra una vista previa de una reseña del feed editorial. */
@Composable
@Preview(showBackground = true)
fun FeedReviewItemPreview() {
    DevicersAppTheme {
        FeedReviewItem(
            FeedReviewContent(
                productName = R.string.feed_product_audio,
                productImageResId = R.drawable.device_01,
                productMetadata = R.string.feed_metadata_audio,
                author = R.string.feed_user_audio,
                avatarResId = R.drawable.profile_avatar_02,
                reviewText = R.string.feed_review_audio,
                likes = 128,
                comments = 18,
                timeAgo = R.string.feed_time_five_hours,
                rating = 5,
                productAverage = R.string.feed_average_audio
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
