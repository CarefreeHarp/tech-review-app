package com.example.devicersapp.ui.screens.review

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.models.ReplyContent
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.screens.review.components.ReplyComposer
import com.example.devicersapp.ui.screens.review.components.ReplyList
import com.example.devicersapp.ui.screens.review.components.ReviewDetailCard
import com.example.devicersapp.ui.screens.review.components.ReviewDetailHeader
import com.example.devicersapp.ui.screens.review.components.ReviewProductSummary

/** Configura el fondo y la estructura principal de la pantalla de detalle de reseña. */
@Composable
fun ReviewScreen(modifier: Modifier = Modifier) {
    ReviewScreenContent(
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
            .statusBarsPadding()
    )
}

/** Ensambla el producto, la reseña, las respuestas y el compositor de respuesta. */
@Composable
fun ReviewScreenContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        ReviewDetailHeader(modifier = Modifier.padding(horizontal = 18.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
        ) {
            ReviewProductSummary(
                product = ProductContent(
                    name = stringResource(R.string.review_product_name),
                    brand = stringResource(R.string.review_product_brand),
                    imageResId = R.drawable.auriculares_logo,
                    imageDescription = stringResource(R.string.review_product_image),
                    showImage = false
                )
            )
            ReviewDetailCard(
                review = ReviewContent(
                    avatarResId = R.drawable.profile_avatar_02,
                    author = stringResource(R.string.review_author),
                    rating = 5,
                    text = stringResource(R.string.review_detail_text),
                    likes = 128,
                    timeAgo = stringResource(R.string.review_time)
                ),
                modifier = Modifier.padding(top = 12.dp)
            )
            Spacer(modifier = Modifier.height(28.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(LocalDevicersColors.current.border)
            )
            ReplyList(
                replies = listOf(
                    ReplyContent(R.drawable.profile_avatar_03, stringResource(R.string.review_reply_author_one), stringResource(R.string.review_reply_time), stringResource(R.string.review_reply_text_one)),
                    ReplyContent(R.drawable.profile_avatar_04, stringResource(R.string.review_reply_author_two), stringResource(R.string.review_reply_time), stringResource(R.string.review_reply_text_two))
                ),
                modifier = Modifier.padding(vertical = 20.dp)
            )
        }
        ReplyComposer(modifier = Modifier.fillMaxWidth())
    }
}

/** Muestra una vista previa de la pantalla completa de detalle de reseña. */
@Composable
@Preview(showBackground = true)
fun ReviewScreenPreview() {
    ReviewScreen()
}
