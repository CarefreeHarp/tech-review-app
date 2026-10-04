package com.example.devicersapp.ui.screens.review.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.devicersapp.data.dto.ReviewDto
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.RatingStarsLargeText
import com.example.devicersapp.ui.theme.ReviewContentText
import com.example.devicersapp.ui.utils.rating.RatingStars

/**
 * Muestra el cuerpo de una reseña obtenida desde el backend.
 */
@Composable
fun ReviewDetail(
    review: ReviewDto,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    val username =
        review.user?.username ?: "Usuario ${review.userId}"

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Text(
            text = username,
            style = MaterialTheme.typography.titleSmall,
            color = colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            RatingStars(
                rating = review.rating,
                style = RatingStarsLargeText
            )
        }

        if (!review.title.isNullOrBlank()) {

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = review.title,
                style = MaterialTheme.typography.titleMedium,
                color = colors.textPrimary
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = review.body,
            style = ReviewContentText,
            color = colors.textPrimary
        )
    }
}