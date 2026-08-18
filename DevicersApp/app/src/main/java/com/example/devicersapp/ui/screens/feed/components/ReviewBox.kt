package com.example.devicersapp.ui.screens.feed.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.FeedReviewContent
import com.example.devicersapp.ui.theme.ReviewContentText
import com.example.devicersapp.ui.utils.rating.ratingStarsResource

/** Muestra una reseña configurable de producto dentro del feed. */
@Composable
fun ReviewBox(review: FeedReviewContent, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth()
            .background(LocalDevicersColors.current.surface, RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            Image(
                painter = painterResource(review.productImageResId),
                contentDescription = stringResource(R.string.review_product_image),
                modifier = Modifier.size(65.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(stringResource(review.author), color = LocalDevicersColors.current.textSecondary, style = MaterialTheme.typography.labelSmall)
                Spacer(Modifier.height(4.dp))
                Text(stringResource(review.productName), color = LocalDevicersColors.current.textPrimary, style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(4.dp))
                Text(stringResource(ratingStarsResource(review.rating)), color = LocalDevicersColors.current.primaryYellow, style = MaterialTheme.typography.bodySmall)
            }
            Text(stringResource(review.timeAgo), color = LocalDevicersColors.current.textSecondary, style = MaterialTheme.typography.labelSmall)
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(review.reviewText,),
            style = ReviewContentText,
            color = LocalDevicersColors.current.textSecondary
        )
        Spacer(Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(painterResource(R.drawable.like_icon), null, Modifier.size(18.dp), tint = LocalDevicersColors.current.textPrimary)
            Spacer(Modifier.width(4.dp))
            Text(review.likes.toString(), color = LocalDevicersColors.current.textSecondary, style = MaterialTheme.typography.labelSmall)
            Spacer(Modifier.weight(1f))
            Icon(painterResource(R.drawable.send_icon), null, Modifier.size(18.dp), tint = LocalDevicersColors.current.textPrimary)
            Spacer(Modifier.width(40.dp))
            Icon(painterResource(R.drawable.bookmark_icon), null, Modifier.size(18.dp), tint = LocalDevicersColors.current.textPrimary)
        }
    }
}

/** Muestra una vista previa de una reseña configurable del feed. */
@Composable
@Preview(showBackground = true)
fun ReviewBoxPreview() {
    ReviewBox(
        FeedReviewContent(
            productName = R.string.feed_product_phone,
            productImageResId = R.drawable.electronic_phone,
            author = R.string.feed_user_phone,
            reviewText = R.string.feed_review_phone,
            likes = 125,
            timeAgo = R.string.feed_time_two_days,
            rating = 5
        )
    )
}
