package com.example.devicersapp.ui.screens.review.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.RatingStarsLargeText
import com.example.devicersapp.ui.theme.ReviewContentText
import com.example.devicersapp.ui.utils.profile.ProfileAvatar
import com.example.devicersapp.ui.utils.rating.RatingStars
import com.example.devicersapp.ui.utils.review.ReviewActionsRow

/**
 * Muestra el cuerpo de una reseña: su autor, su calificación, su texto y sus interacciones.
 *
 * El contenido se apoya directamente sobre el fondo, separado por aire y divisores, en vez
 * de encerrarse en una tarjeta, tal como lo plantea el diseño editorial.
 *
 * @param review Información visible de la reseña.
 * @param modifier Modificador aplicado al contenedor.
 */
@Composable
fun ReviewDetail(review: ReviewContent, modifier: Modifier = Modifier) {
    val colors = LocalDevicersColors.current

    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ProfileAvatar(
                avatarResId = review.avatarResId,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = stringResource(review.authorResId),
                    style = MaterialTheme.typography.titleSmall,
                    color = colors.textPrimary
                )
                review.timeAgoResId?.let { timeAgoResId ->
                    Text(
                        text = stringResource(timeAgoResId),
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            RatingStars(rating = review.rating, style = RatingStarsLargeText)
            // El promedio del producto acompaña a la calificación entera que dio el autor.
            review.productAverageResId?.let { averageResId ->
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(averageResId),
                    style = MaterialTheme.typography.titleSmall,
                    color = colors.textPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = stringResource(review.textResId),
            style = ReviewContentText,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(18.dp))

        ReviewActionsRow(likes = review.likes, comments = review.comments)
    }
}

/** Muestra una vista previa del detalle de una reseña. */
@Composable
@Preview(showBackground = true)
fun ReviewDetailPreview() {
    DevicersAppTheme {
        ReviewDetail(
            ReviewContent(
                avatarResId = R.drawable.profile_avatar_02,
                authorResId = R.string.review_author,
                rating = 5,
                textResId = R.string.review_detail_text,
                likes = 128,
                comments = 18,
                timeAgoResId = R.string.review_time,
                productAverageResId = R.string.review_product_average
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
