package com.example.devicersapp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBarsPadding
import com.example.devicersapp.R
import com.example.devicersapp.ui.utils.navigation.BottomNavigationBar
import com.example.devicersapp.ui.screens.profile.components.ProfileStats
import com.example.devicersapp.ui.screens.profile.components.ProfileReviewCard
import com.example.devicersapp.ui.utils.navigation.AppTopBar

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    ProfileScreenContent(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_light))
            .statusBarsPadding()
    )
}

@Composable
fun ProfileScreenContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp)
        ) {

            Spacer(modifier = Modifier.height(35.dp))

            AppTopBar()

            Spacer(modifier = Modifier.height(16.dp))

            ProfileStats()

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Mis reseñas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.text_primary_light)
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileReviewCard()

            Spacer(modifier = Modifier.height(12.dp))

            ProfileReviewCard()

            Spacer(modifier = Modifier.height(144.dp))
        }

        BottomNavigationBar(
            modifier = Modifier.fillMaxWidth(),
            selectedItem = "profile"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
