package com.example.devicersapp.ui.screens.review.components

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

/** Muestra el producto asociado a la reseña con su imagen, nombre y marca. */
@Composable
fun ReviewProductSummary(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colorResource(R.color.surface_light), RoundedCornerShape(14.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.auriculares_logo),
            contentDescription = stringResource(R.string.review_product_image),
            modifier = Modifier.size(56.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(stringResource(R.string.review_product_name), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = colorResource(R.color.text_primary_light))
            Spacer(Modifier.height(2.dp))
            Text(stringResource(R.string.review_product_brand), fontSize = 13.sp, color = colorResource(R.color.text_secondary_light))
        }
    }
}

/** Muestra una vista previa del resumen de producto. */
@Composable
@Preview(showBackground = true)
fun ReviewProductSummaryPreview() { ReviewProductSummary() }
