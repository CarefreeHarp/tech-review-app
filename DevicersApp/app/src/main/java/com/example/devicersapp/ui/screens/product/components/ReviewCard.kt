package com.example.devicersapp.ui.screens.product.components

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.theme.ReviewContentText
import com.example.devicersapp.ui.utils.profile.ProfileAvatar
import com.example.devicersapp.ui.utils.rating.ratingStarsResource

/**
 * Muestra una tarjeta de reseña con avatar, autor, calificación y cantidad de me gusta.
 *
 * @param review Información de la reseña, incluido el recurso del avatar de su autor.
 * @param modifier Modificador aplicado a la tarjeta.
 */
@Composable
fun ReviewCard(review: ReviewContent, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = colorResource(R.color.surface_light),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileAvatar(
                avatarResId = review.avatarResId,
                modifier = Modifier.size(34.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = review.author,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.text_primary_light)
                )
                Text(
                    text = stringResource(ratingStarsResource(review.rating)),
                    fontSize = 12.sp,
                    color = colorResource(R.color.primary_yellow)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = review.text,
            style = ReviewContentText,
            color = colorResource(R.color.text_secondary_light)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.like_icon),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = review.likes.toString(),
                fontSize = 11.sp,
                color = colorResource(R.color.text_secondary_light),
                fontWeight = FontWeight.Black
            )
        }
    }
}

/** Muestra una vista previa de una tarjeta de reseña. */
@Composable
@Preview(showBackground = true)
fun ReviewCardPreview() {
    ReviewCard(
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_01,
            author = stringResource(R.string.review_card_username),
            rating = 5,
            text = stringResource(R.string.review_card_text),
            likes = 23
        )
    )
}
