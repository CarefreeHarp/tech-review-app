package com.example.devicersapp.ui.screens.review.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.utils.profile.ProfileAvatar
import com.example.devicersapp.ui.utils.rating.ratingStars

/**
 * Muestra el contenido principal de una reseña, el avatar de su autor y sus acciones de interacción.
 *
 * @param review Información visible de la reseña.
 * @param modifier Modificador aplicado a la tarjeta.
 */
@Composable
fun ReviewDetailCard(review: ReviewContent, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 244.dp)
            .background(LocalDevicersColors.current.surface, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ProfileAvatar(
                avatarResId = review.avatarResId,
                modifier = Modifier.size(36.dp)
            )
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(stringResource(review.authorResId), style = MaterialTheme.typography.titleSmall, color = LocalDevicersColors.current.textPrimary)
                review.timeAgoResId?.let { timeAgoResId ->
                    Text(stringResource(timeAgoResId), style = MaterialTheme.typography.bodySmall, color = LocalDevicersColors.current.textSecondary)
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(ratingStars(review.rating), color = LocalDevicersColors.current.primaryYellow, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(20.dp))
        Text(stringResource(review.textResId), style = MaterialTheme.typography.bodyMedium, color = LocalDevicersColors.current.textPrimary)
        Spacer(Modifier.weight(1f))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.like_icon),
                    contentDescription = stringResource(R.string.review_like),
                    tint = LocalDevicersColors.current.textPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(review.likes.toString(), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = LocalDevicersColors.current.textPrimary)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.send_icon),
                    contentDescription = stringResource(R.string.review_send),
                    tint = LocalDevicersColors.current.textPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(20.dp))
                Icon(
                    painter = painterResource(R.drawable.bookmark_icon),
                    contentDescription = stringResource(R.string.review_save),
                    tint = LocalDevicersColors.current.textPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/** Muestra una vista previa de la tarjeta de detalle de reseña. */
@Composable
@Preview(showBackground = true)
fun ReviewDetailCardPreview() {
    ReviewDetailCard(
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_02,
            authorResId = R.string.review_author,
            rating = 5,
            textResId = R.string.review_detail_text,
            likes = 128,
            timeAgoResId = R.string.review_time
        )
    )
}
