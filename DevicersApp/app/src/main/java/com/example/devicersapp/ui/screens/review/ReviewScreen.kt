package com.example.devicersapp.ui.screens.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.screens.review.components.CuadroResena
import com.example.devicersapp.ui.screens.review.components.ResponderUsuario
import com.example.devicersapp.ui.screens.review.components.Respuestas
import com.example.devicersapp.ui.screens.review.components.EncabezadoResena
import com.example.devicersapp.ui.screens.review.components.Item
import com.example.devicersapp.ui.screens.review.components.RespuestaItem


@Composable
fun ReviewScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {

        // Encabezado superior
        EncabezadoResena()

        // Contenido principal
        Column(
            modifier = Modifier
                .weight(1f)
        ) {

            // Producto
            Item(
                idImage = R.drawable.auriculares_logo,
                nombreItem = "Auriculares FX3 Pro",
                marca = "Sony",
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            )

            // Reseña principal
            CuadroResena(
                inicial = "D",
                usuario = "@usuario",
                tiempo = "Hace 2 días",
                estrellas = "★★★★★",
                respuesta = "El sonido es claro, la batería dura bastante y la cancelación de ruido ayuda mucho para estudiar o trabajar.",
                likes = "128",
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            )

            // Línea separadora
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )

            // Sección de respuestas CENTRADA VERTICALMENTE
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {

                Respuestas(
                    numRespuestas = 2,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 4.dp
                    )
                )

                RespuestaItem(
                    inicial = "E",
                    usuario = "@papichulo",
                    tiempo = "Hace 1 día",
                    respuesta = "Super bueno para movil"
                )

                RespuestaItem(
                    inicial = "A",
                    usuario = "@jordi",
                    tiempo = "Hace 1 día",
                    respuesta = "No me gusta la capacidad"
                )
            }
        }

        // Barra inferior
        ResponderUsuario()
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewScreenPreview() {
    ReviewScreen()
}