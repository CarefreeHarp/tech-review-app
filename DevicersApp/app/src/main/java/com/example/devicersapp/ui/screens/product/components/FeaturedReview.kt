package com.example.devicersapp.ui.screens.product.components

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
fun FeaturedReview(
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

            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(
                        color = colorResource(R.color.surface_light),
                        shape = RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "J",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.text_primary_light)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "@usuario",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.text_primary_light)
                )

                Text(
                    text = "★★★★★",
                    fontSize = 12.sp,
                    color = colorResource(R.color.primary_yellow)
                )
            }

            Text(
                text = "♡",
                fontSize = 18.sp,
                color = colorResource(R.color.text_secondary_light)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Muy cómodo y con buen sonido.",
            fontSize = 13.sp,
            color = colorResource(R.color.text_secondary_light)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "23 me gusta",
                fontSize = 11.sp,
                color = colorResource(R.color.text_secondary_light)
            )
        }
    }
}