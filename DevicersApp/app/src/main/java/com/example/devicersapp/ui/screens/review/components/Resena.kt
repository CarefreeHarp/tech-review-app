package com.example.devicersapp.ui.screens.review.components

import android.media.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import com.example.devicersapp.R

@Composable
fun Item(
    idImage: Int,
    nombreItem: String,
    marca: String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(color = Color.LightGray)
            .border(
                width = 1.dp,
                color = Color.Yellow,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //Cuadro de la imagen del articulo
        Box(
            modifier = Modifier
                .width(52.dp)
                .height(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
        ){
            Image(
                painter = painterResource(idImage),
                contentDescription = nombreItem,
                modifier = Modifier
                    .width(52.dp)
                    .height(52.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }

        //Nombre del articulo y marca
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
        ) {
            Text(text = nombreItem, fontWeight = FontWeight.Bold)
            Text(text = marca, fontWeight = FontWeight.Thin, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemPreview() {
    Item(
        idImage = R.drawable.auriculares_logo,
        nombreItem = "Auriculares FX3 Pro",
        marca = "Sony",
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun UsuarioInfo(
    inicial: String,
    usuario: String,
    tiempo: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {

        // Circulo con la inicial
        Box(
            modifier = Modifier
                .width(38.dp)
                .height(38.dp)
                .clip(CircleShape)
                .background(color = Color.Gray)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = inicial, fontWeight = FontWeight.Bold)
        }

        // Usuario y tiempo
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
        ) {
            Text(
                text = usuario,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = tiempo,
                fontWeight = FontWeight.Thin
            )
        }
    }
}

@Composable
fun CuadroResena(
    inicial: String,
    usuario: String,
    tiempo: String,
    estrellas: String,
    respuesta: String,
    likes: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.LightGray)
            .padding(16.dp)
    ) {

        // Usuario, inicial y fecha
        UsuarioInfo(
            inicial = inicial,
            usuario = usuario,
            tiempo = tiempo
        )

        // Estrellas
        Text(
            text = estrellas,
            color = Color.Yellow,
            modifier = Modifier.padding(
                top = 20.dp,
                bottom = 20.dp
            )
        )

        // Comentario
        Text(
            text = respuesta
        )

        //Parte inferior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Corazón y cantidad de likes
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.heart_logo),
                    contentDescription = "Me gusta",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )

                Text(
                    text = likes,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }

            // Enviar y Guardar
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(R.drawable.enviar_logo),
                    contentDescription = "Enviar",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )

                Image(
                    painter = painterResource(R.drawable.save),
                    contentDescription = "Guardar",
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .width(24.dp)
                        .height(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CuadroResenaPreview() {
    CuadroResena(
        inicial = "D",
        usuario = "@usuario",
        tiempo = "Hace 2 días",
        estrellas = "★★★★★",
        respuesta = "El sonido es claro, la batería dura bastante y la cancelación de ruido ayuda mucho para estudiar o trabajar.",
        likes = "123",
        modifier = Modifier.padding(16.dp)
    )
}