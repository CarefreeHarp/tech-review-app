package com.example.devicersapp.ui.screens.review.components

import androidx.compose.foundation.Image
import com.example.devicersapp.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EncabezadoResena(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Flecha + título
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(R.drawable.back_icon),
                contentDescription = "Volver",
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )

            Text(
                text = "Reseña",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 20.dp)
            )
        }

        // Guardar
        Image(
            painter = painterResource(R.drawable.save),
            contentDescription = "Guardar",
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EncabezadoResenaPreview() {
    EncabezadoResena()
}