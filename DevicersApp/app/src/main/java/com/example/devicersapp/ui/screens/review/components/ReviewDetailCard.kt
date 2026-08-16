package com.example.devicersapp.ui.screens.review.components

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.utils.profile.ProfileAvatar
import com.example.devicersapp.ui.utils.rating.ratingStarsResource

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
            .background(colorResource(R.color.surface_light), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ProfileAvatar(
                avatarResId = review.avatarResId,
                modifier = Modifier.size(36.dp)
            )
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(review.author, style = MaterialTheme.typography.titleSmall, color = colorResource(R.color.text_primary_light))
                review.timeAgo?.let { timeAgo ->
                    Text(timeAgo, style = MaterialTheme.typography.bodySmall, color = colorResource(R.color.text_secondary_light))
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(stringResource(ratingStarsResource(review.rating)), color = colorResource(R.color.primary_yellow), style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(20.dp))
        Text(review.text, style = MaterialTheme.typography.bodyMedium, color = colorResource(R.color.text_primary_light))
        Spacer(Modifier.weight(1f))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.like_icon),
                    contentDescription = stringResource(R.string.review_like),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(review.likes.toString(), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = colorResource(R.color.text_primary_light))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.send_icon),
                    contentDescription = stringResource(R.string.review_send),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(20.dp))
                Image(
                    painter = painterResource(R.drawable.bookmark_icon),
                    contentDescription = stringResource(R.string.review_save),
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
            author = stringResource(R.string.review_author),
            rating = 5,
            text = stringResource(R.string.review_detail_text),
            likes = 128,
            timeAgo = stringResource(R.string.review_time)
        )
    )
}
