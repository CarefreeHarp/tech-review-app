package com.example.devicersapp.ui.screens.feed.components

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
import com.example.devicersapp.ui.models.FeedReviewContent
import com.example.devicersapp.ui.theme.ReviewContentText
import com.example.devicersapp.ui.utils.rating.ratingStarsResource

/** Muestra una reseña configurable de producto dentro del feed. */
@Composable
fun ReviewBox(review: FeedReviewContent, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth()
            .background(colorResource(R.color.surface_light), RoundedCornerShape(14.dp))
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
                Text(review.author, color = colorResource(R.color.text_secondary_light), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(review.productName, color = colorResource(R.color.text_primary_light), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(stringResource(ratingStarsResource(review.rating)), color = colorResource(R.color.primary_yellow), fontSize = 12.sp)
            }
            Text(review.timeAgo, color = colorResource(R.color.text_secondary_light), fontSize = 10.sp)
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = review.reviewText,
            style = ReviewContentText,
            color = colorResource(R.color.text_secondary_light)
        )
        Spacer(Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(painterResource(R.drawable.like_icon), null, Modifier.size(18.dp))
            Spacer(Modifier.width(4.dp))
            Text(review.likes.toString(), color = colorResource(R.color.text_secondary_light), fontSize = 10.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.weight(1f))
            Image(painterResource(R.drawable.send_icon), null, Modifier.size(18.dp))
            Spacer(Modifier.width(40.dp))
            Image(painterResource(R.drawable.bookmark_icon), null, Modifier.size(18.dp))
        }
    }
}

/** Muestra una vista previa de una reseña configurable del feed. */
@Composable
@Preview(showBackground = true)
fun ReviewBoxPreview() {
    ReviewBox(
        FeedReviewContent(
            productName = stringResource(R.string.feed_product_phone),
            productImageResId = R.drawable.electronic_phone,
            author = stringResource(R.string.feed_user_phone),
            reviewText = stringResource(R.string.feed_review_phone),
            likes = 125,
            timeAgo = stringResource(R.string.feed_time_two_days),
            rating = 5
        )
    )
}
