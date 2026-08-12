package com.example.devicersapp.ui.screens.login.components

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R

/**
 * Muestra la acción de recuperación de contraseña con la apariencia visual de texto plano.
 * El callback vacío conserva intencionalmente el prototipo actual solo de frontend.
 */
@Composable
fun ForgotPasswordLink(modifier: Modifier = Modifier) {
    TextButton(onClick = {}, modifier = modifier) {
        Text(
            text = stringResource(R.string.forgot_password),
            color = colorResource(R.color.text_secondary_light),
            fontSize = 10.sp
        )
    }
}

/** Muestra una vista previa del enlace estático de recuperación de contraseña. */
@Composable
@Preview(showBackground = true)
fun ForgotPasswordLinkPreview() {
    ForgotPasswordLink()
}
