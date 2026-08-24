package com.example.devicersapp.ui.screens.product.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalProfileProvider
import com.example.devicersapp.data.local.LocalReviewProvider
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.theme.CardHighlightText
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.ReviewContentText
import com.example.devicersapp.ui.utils.profile.ProfileAvatar
import com.example.devicersapp.ui.utils.rating.RatingStars

/**
 * Muestra una tarjeta de reseña con avatar, autor, calificación y cantidad de me gusta.
 *
 * @param review Información de la reseña, incluido el recurso del avatar de su autor.
 * @param onViewMoreClick Acción solicitada al mostrar el detalle completo de la reseña.
 * @param modifier Modificador aplicado a la tarjeta.
 */
@Composable
fun ReviewCard(
    review: ReviewContent,
    onViewMoreClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val author = requireNotNull(LocalProfileProvider.getProfileById(review.authorId))

    Column(
        modifier = modifier
            .fillMaxWidth()
            // La sombra suave despega la tarjeta del fondo, como en el diseño editorial.
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp))
            .background(
                color = LocalDevicersColors.current.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileAvatar(
                avatarResId = author.avatarResId,
                modifier = Modifier.size(34.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(author.handleResId),
                    style = CardHighlightText,
                    color = LocalDevicersColors.current.textPrimary
                )
                Spacer(modifier = Modifier.height(3.dp))
                RatingStars(rating = review.rating)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(review.textResId),
            style = ReviewContentText,
            color = LocalDevicersColors.current.textSecondary,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        TextButton(
            onClick = onViewMoreClick,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = stringResource(R.string.review_show_more),
                color = LocalDevicersColors.current.primary,
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.like_icon),
                contentDescription = stringResource(R.string.review_action_like),
                tint = LocalDevicersColors.current.textSecondary,
                modifier = Modifier.size(17.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = review.likes.toString(),
                style = CardHighlightText,
                color = LocalDevicersColors.current.textPrimary
            )
        }
    }
}

/** Muestra una vista previa de una tarjeta de reseña. */
@Composable
@Preview(showBackground = true)
fun ReviewCardPreview() {
    DevicersAppTheme {
        ReviewCard(
            review = LocalReviewProvider.productReviews.first(),
            modifier = Modifier.padding(16.dp)
        )
    }
}
