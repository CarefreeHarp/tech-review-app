package com.example.devicersapp.ui.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R

@Composable
fun ProfileStats(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Foto de perfil temporal
        Box(
            modifier = Modifier
                .size(88.dp)
                .background(
                    color = colorResource(R.color.surface_light),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "J",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.text_primary_light)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Juan",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.text_primary_light)
        )

        Text(
            text = "@usuario",
            fontSize = 13.sp,
            color = colorResource(R.color.text_secondary_light)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Amante de la tecnología y las buenas recomendaciones.",
            modifier = Modifier.padding(horizontal = 24.dp),
            fontSize = 13.sp,
            color = colorResource(R.color.text_secondary_light),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Estadísticas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileStat(
                number = "12",
                label = "Reseñas"
            )

            ProfileStat(
                number = "245",
                label = "Seguidores"
            )

            ProfileStat(
                number = "180",
                label = "Siguiendo"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.primary_yellow),
                contentColor = colorResource(R.color.text_primary_light)
            )
        ) {
            Text(
                text = "Editar perfil",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/** Muestra una vista previa de las estadísticas del perfil. */
@Composable
@Preview(showBackground = true)
fun ProfileStatsPreview() {
    ProfileStats()
}

@Composable
private fun ProfileStat(
    number: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = number,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.text_primary_light)
        )

        Text(
            text = label,
            fontSize = 11.sp,
            color = colorResource(R.color.text_secondary_light)
        )
    }
}
