package com.example.devicersapp.ui.screens.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R
import com.example.devicersapp.ui.screens.feed.components.ReviewBox
import com.example.devicersapp.ui.utils.navigation.BottomNavigationBar

/** Renderiza la pantalla estática del Feed del tema claro de Devicers. */
@Composable
fun FeedScreen(modifier: Modifier = Modifier) {
    FeedScreenContent(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_light))
    )
}

/** Reúne el contenido visual de la pantalla de Feed. */
@Composable
fun FeedScreenContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(R.drawable.logo_claro),
                contentDescription = stringResource(R.string.devicers_logo_description),
                modifier = Modifier.size(width = 186.dp, height = 50.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.feed),
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(R.color.text_primary_light),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .align(Alignment.Start)
                    .width(50.dp)
                    .height(2.dp)
                    .background(
                        colorResource(R.color.primary_yellow)
                    )
            )

            Spacer(modifier = Modifier.height(20.dp))

            ReviewBox(
                elementTypeResId = R.string.feed_product_phone,
                imageResId = R.drawable.iphone,
                usernameResId = R.string.feed_user_phone,
                reviewResId = R.string.feed_review_phone,
                likes = 125,
                timeAgoResId = R.string.feed_time_two_days
            )

            Spacer(modifier = Modifier.height(8.dp))

            ReviewBox(
                elementTypeResId = R.string.feed_product_audio,
                imageResId = R.drawable.iphone,
                usernameResId = R.string.feed_user_audio,
                reviewResId = R.string.feed_review_audio,
                likes = 128,
                timeAgoResId = R.string.feed_time_two_days
            )

            Spacer(modifier = Modifier.height(8.dp))

            ReviewBox(
                elementTypeResId = R.string.feed_product_watch,
                imageResId = R.drawable.iphone,
                usernameResId = R.string.feed_user_watch,
                reviewResId = R.string.feed_review_watch,
                likes = 128,
                timeAgoResId = R.string.feed_time_two_days
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        BottomNavigationBar(
            modifier = Modifier.fillMaxWidth(),
            selectedItem = "home"
        )
    }
}

/** Muestra una vista previa de la composición completa del Feed. */
@Composable
@Preview(showBackground = true)
fun FeedScreenPreview() {
    FeedScreen()
}
