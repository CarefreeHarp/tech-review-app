package com.example.devicersapp.ui.screens.review.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R


//Fondo
@Composable
fun Fondo(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(
                color = colorResource(R.color.surface_light)
            )
    ) {
    }
}

//Caja de Responder a @usuario
@Composable
fun Responder(
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = "Responder a @usuario",
        onValueChange = {},
        modifier = modifier,
        singleLine = true,
        shape = RoundedCornerShape(15.dp)
    )
}

//Boton de enviar
@Composable
fun Enviar(
    modifier: Modifier = Modifier
){
    IconButton(
        onClick = {},
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.enviar_logo),
            contentDescription = "Enviar",
            modifier = Modifier
                .width(28.dp)
                .height(28.dp)
        )
    }
}

//Mezclar las 3 cosas
@Composable
fun ResponderUsuario(
    modifier: Modifier = Modifier
){
    Box(modifier = modifier
        .fillMaxWidth()
        .height(55.dp)
    ){
    Fondo()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.Center)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Responder(
            modifier = Modifier
                .width(280.dp)
                .height(48.dp)
        )

        Enviar()
    }
    }
}

//Ver fondo
@Preview
@Composable
fun FondoPreview(){
    Fondo()
}

//Ver caja de Responder a @usuario
@Preview(showBackground = true)
@Composable
fun ResponderPreview(){
    Responder()
}

//Ver icono enviar
@Preview(showBackground = true)
@Composable
fun EnviarPreview(){
    Enviar()
}

//Ver ResponderUsuario
@Preview(
    showBackground = true
)
@Composable
fun ResponderUsuarioPreview(){
    ResponderUsuario()
}

