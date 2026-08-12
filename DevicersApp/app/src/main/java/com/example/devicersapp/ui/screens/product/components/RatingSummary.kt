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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R

@Composable
fun RatingSummary(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {

        Text(
            text = "Calificación promedio",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(R.color.text_primary_light)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Calificación general
            Column(
                modifier = Modifier.width(168.dp)
            ) {
                Text(
                    text = "4.6",
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.text_primary_light)
                )

                Text(
                    text = "★★★★★",
                    fontSize = 24.sp,
                    color = colorResource(R.color.primary_yellow)
                )

                Text(
                    text = "230 reseñas",
                    fontSize = 14.sp,
                    color = colorResource(R.color.text_secondary_light)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Distribución
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                RatingBarRow("5", 0.88f, "88%")
                RatingBarRow("4", 0.52f, "52%")
                RatingBarRow("3", 0.17f, "17%")
                RatingBarRow("2", 0.07f, "7%")
                RatingBarRow("1", 0.05f, "5%")
            }
        }
    }
}

/** Muestra una vista previa del resumen de calificaciones del producto. */
@Composable
@Preview(showBackground = true)
fun RatingSummaryPreview() {
    RatingSummary()
}

@Composable
private fun RatingBarRow(
    stars: String,
    progress: Float,
    percentage: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = stars,
            fontSize = 11.sp,
            color = colorResource(R.color.text_secondary_light),
            modifier = Modifier.width(14.dp)
        )

        Box(
            modifier = Modifier
                .width(145.dp)
                .height(8.dp)
                .background(
                    color = colorResource(R.color.surface_secondary_light),
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(8.dp)
                    .background(
                        color = colorResource(R.color.primary_yellow),
                        shape = RoundedCornerShape(10.dp)
                    )
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = percentage,
            fontSize = 10.sp,
            color = colorResource(R.color.text_secondary_light),
            modifier = Modifier.width(28.dp)
        )
    }
}
