package com.example.devicersapp.ui.screens.home.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalProfileProvider
import com.example.devicersapp.data.local.LocalReviewProvider
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.FeedReviewActionCountText
import com.example.devicersapp.ui.theme.RatingStarsLargeText
import com.example.devicersapp.ui.theme.ReviewContentText
import com.example.devicersapp.ui.utils.profile.ProfileAvatar
import com.example.devicersapp.ui.utils.review.ReviewActionsRow
import com.example.devicersapp.ui.utils.rating.RatingStars

/**
 * Muestra una reseña del feed editorial: miniatura del producto, autor, calificación y acciones.
 *
 * @param review Contenido de la reseña publicada.
 * @param onViewMoreClick Acción solicitada al mostrar el detalle completo de la reseña.
 * @param onCommentClick Acción solicitada al abrir los comentarios de la reseña.
 * @param onSendClick Acción solicitada al compartir la reseña.
 * @param modifier Modificador aplicado al contenedor de la reseña.
 */
@Composable
fun FeedReviewItem(
    review: ReviewContent,
    author: ProfileContent,
    onViewMoreClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onSendClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current


    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            // La miniatura se muestra directamente, sin marco ni superficie intermedia.
            Image(
                painter = painterResource(review.productImageResId),
                contentDescription = stringResource(R.string.review_product_image),
                modifier = Modifier
                    .size(width = 108.dp, height = 132.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ProfileAvatar(
                        avatarResId = author.avatarResId,
                        modifier = Modifier.size(34.dp)
                    )
                    Spacer(modifier = Modifier.width(9.dp))
                    Text(
                        text = stringResource(author.handleResId),
                        modifier = Modifier.weight(1f),
                        color = colors.textPrimary,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = stringResource(requireNotNull(review.timeAgoResId)),
                        color = colors.textSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(review.productMetadataResId),
                    color = colors.textSecondary,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = stringResource(review.productNameResId),
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
                        text = stringResource(requireNotNull(review.productAverageResId)),
                        color = colors.textPrimary,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(review.textResId),
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
            comments = review.comments.size,
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
    val review = LocalReviewProvider.reviews.first()

    DevicersAppTheme {
        FeedReviewItem(
            review = review,
            author = requireNotNull(
                LocalProfileProvider.getProfileById(review.authorId)
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
