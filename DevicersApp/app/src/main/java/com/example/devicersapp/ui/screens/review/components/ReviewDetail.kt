package com.example.devicersapp.ui.screens.review.components


import com.example.devicersapp.ui.session.LocalSessionState

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.clickable
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
import com.example.devicersapp.data.local.LocalProfileProvider
import com.example.devicersapp.data.local.LocalReviewProvider
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
 * @param actions Acciones del autor alineadas al extremo derecho de su misma fila.
 */
@Composable
fun ReviewDetail(review: ReviewContent, modifier: Modifier = Modifier, onProfileClick: (String) -> Unit = {}, actions: @Composable () -> Unit = {}) {
    val colors = LocalDevicersColors.current
    val author = if (review.authorName == null) LocalProfileProvider.getProfileById(review.authorId) else null

    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Row(
                modifier = Modifier.weight(1f).then(
                    Modifier.clickable { onProfileClick(review.authorId) }
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileAvatar(
                    onProfileClick = onProfileClick,
                    avatarResId = author?.avatarResId ?: R.drawable.no_pfp_icon,
                    imageUrl = review.authorImageUrl,
                    userId = review.authorId.toIntOrNull(),
                    profileId = review.authorId,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        modifier = Modifier.clickable { onProfileClick(review.authorId) },
                        text = LocalSessionState.current.userNameFor(
                            review.authorId.toIntOrNull(),
                            review.authorName ?: author?.let { stringResource(it.handleResId) }.orEmpty()
                        ),
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
            actions()
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.clickable { onProfileClick(review.authorId) }, verticalAlignment = Alignment.CenterVertically) {
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

        review.title?.takeIf { it.isNotBlank() }?.let { title ->
            Text(title, style = MaterialTheme.typography.titleMedium, color = colors.textPrimary)
            Spacer(modifier = Modifier.height(8.dp))
        }
        Text(
            text = review.body ?: stringResource(review.textResId),
            style = ReviewContentText,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(18.dp))

        ReviewActionsRow(likes = review.likes, comments = review.comments.size)
    }
}

/** Muestra una vista previa del detalle de una reseña. */
@Composable
@Preview(showBackground = true)
fun ReviewDetailPreview() {
    DevicersAppTheme {
        ReviewDetail(
            review = LocalReviewProvider.reviews.first(),
            modifier = Modifier.padding(16.dp)
        )
    }
}
