package com.example.devicersapp.ui.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R

@Composable
fun ProfileReviewCard(
    productName: String = "Auriculares",
    reviewText: String = "Muy cómodo y con excelente calidad de sonido.",
    rating: String = "★★★★★",
    likes: Int = 23,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = colorResource(R.color.surface_secondary_light),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(14.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = productName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.text_primary_light)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = rating,
                    fontSize = 12.sp,
                    color = colorResource(R.color.primary_yellow)
                )
            }

            Text(
                text = "♡",
                fontSize = 19.sp,
                color = colorResource(R.color.text_secondary_light)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = reviewText,
            fontSize = 13.sp,
            color = colorResource(R.color.text_secondary_light)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "$likes me gusta",
            modifier = Modifier.align(Alignment.End),
            fontSize = 11.sp,
            color = colorResource(R.color.text_secondary_light)
        )
    }
}