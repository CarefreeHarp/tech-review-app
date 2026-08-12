package com.example.devicersapp.ui.screens.product.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R

@Composable
fun ProductHeader(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {

        Text(
            text = "‹",
            fontSize = 28.sp,
            fontWeight = FontWeight.Normal,
            color = colorResource(R.color.text_primary_light),
            modifier = Modifier.align(Alignment.CenterStart)
        )

        Image(
            painter = painterResource(R.drawable.logo_claro),
            contentDescription = "Devicers",
            modifier = Modifier
                .width(125.dp)
                .height(44.dp)
                .align(Alignment.Center)
        )

        Text(
            text = "⋮",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.text_primary_light),
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}