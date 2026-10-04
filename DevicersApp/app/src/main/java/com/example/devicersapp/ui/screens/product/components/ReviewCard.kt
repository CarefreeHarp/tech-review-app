package com.example.devicersapp.ui.screens.product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.devicersapp.data.dto.ReviewDto
import com.example.devicersapp.ui.theme.CardHighlightText
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.ReviewContentText
import com.example.devicersapp.ui.utils.rating.RatingStars

/**
 * Muestra una reseña recibida desde el backend.
 */
@Composable
fun ReviewCard(
    review: ReviewDto,
    onViewMoreClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = colors.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = review.user?.username ?: "Usuario ${review.userId}",
                    style = CardHighlightText,
                    color = colors.textPrimary
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                RatingStars(
                    rating = review.rating
                )
            }
        }

        if (!review.title.isNullOrBlank()) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = review.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = review.body,
            style = ReviewContentText,
            color = colors.textSecondary,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        TextButton(
            onClick = onViewMoreClick,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = "Ver más",
                color = colors.primary,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
