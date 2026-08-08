package com.example.devicersapp.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R

/** Muestra el logo de Devicers y permite controlar su tamaño y ubicación desde quien lo llama. */
@Composable
fun LogoApp(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.logue),
        contentDescription = stringResource(R.string.devicers_logo_description),
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

/** Muestra una vista previa independiente para verificar el recurso del logo. */
@Composable
@Preview(showBackground = true)
fun LogoAppPreview() {
    LogoApp(modifier = Modifier.size(120.dp))
}
