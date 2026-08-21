package com.example.devicersapp.ui.screens.access.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.AuthenticationSupportText

/**
 * Muestra la acción de recuperación de contraseña como un enlace acentuado.
 * El callback vacío conserva intencionalmente el prototipo actual solo de frontend.
 */
@Composable
fun ForgotPasswordLink(modifier: Modifier = Modifier) {
    TextButton(onClick = {}, modifier = modifier) {
        Text(
            text = stringResource(R.string.forgot_password),
            // El acento de texto distingue el enlace del resto del formulario.
            color = LocalDevicersColors.current.primaryText,
            style = AuthenticationSupportText,
            fontWeight = FontWeight.Medium
        )
    }
}

/** Muestra una vista previa del enlace estático de recuperación de contraseña. */
@Composable
@Preview(showBackground = true)
fun ForgotPasswordLinkPreview() {
    ForgotPasswordLink()
}
