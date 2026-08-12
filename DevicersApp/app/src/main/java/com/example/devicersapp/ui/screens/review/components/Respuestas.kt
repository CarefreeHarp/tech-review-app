package com.example.devicersapp.ui.screens.review.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RespuestaItem(
    inicial: String,
    usuario: String,
    tiempo: String,
    respuesta: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                top = 10.dp,
                bottom = 10.dp
            )
    ) {

        UsuarioInfo(
            inicial = inicial,
            usuario = usuario,
            tiempo = tiempo
        )

        Text(
            text = respuesta,
            modifier = Modifier
                .padding(
                    start = 50.dp,
                    top = 8.dp,
                    bottom = 10.dp
                )
        )

        Text(
            text = "Responder",
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(start = 50.dp)
        )
    }
}

@Preview (showBackground = true)
@Composable
fun RespuestaItemPreview(){
    Column() {
        RespuestaItem("E","@papichulo","Hace 1 dia","Super bueno para movil")
        RespuestaItem("A","@jordi","Hace 1 dia","No me gusta la capacidad")
    }

}

@Composable
fun Respuestas(
    numRespuestas: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Respuestas ($numRespuestas)",
        fontWeight = FontWeight.ExtraBold,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun RespuestasPreview(){
    Respuestas(2)
}
