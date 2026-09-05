package com.example.devicersapp.ui.screens.register.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R

/**
 * Muestra los requisitos incumplidos del correo o de la contraseña antes de crear una cuenta.
 *
 * @param isEmailValid Indica si el correo tiene un formato válido.
 * @param isPasswordValid Indica si la contraseña cumple los requisitos definidos.
 * @param isConfirmationPasswordValid Indica si la confirmación coincide con la contraseña.
 * @param errorMessage Mensaje de error retornado por Firebase al intentar registrar la cuenta.
 * @param modifier Modificador aplicado al aviso.
 */
@Composable
fun RegisterValidationWarning(
    isEmailValid: Boolean,
    isPasswordValid: Boolean,
    isConfirmationPasswordValid: Boolean,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                // El aviso es un área anidada del formulario, no una tarjeta principal.
                color = LocalDevicersColors.current.surfaceSecondary,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = LocalDevicersColors.current.error,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(14.dp),
        // Cada requisito incumplido necesita aire para leerse como una línea independiente.
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (!isEmailValid) {
            Text(
                text = stringResource(R.string.register_validation_email),
                style = MaterialTheme.typography.bodySmall,
                color = LocalDevicersColors.current.error
            )
        }
        if (!isPasswordValid) {
            Text(
                text = stringResource(R.string.register_validation_password),
                style = MaterialTheme.typography.bodySmall,
                color = LocalDevicersColors.current.error
            )
        }
        if (!isConfirmationPasswordValid) {
            Text(
                text = stringResource(R.string.register_validation_confirmation),
                style = MaterialTheme.typography.bodySmall,
                color = LocalDevicersColors.current.error
            )
        }
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = LocalDevicersColors.current.error
            )
        }
    }
}

/** Muestra una vista previa del aviso de requisitos del formulario de registro. */
@Composable
@Preview(showBackground = true)
fun RegisterValidationWarningPreview() {
    RegisterValidationWarning(
        isEmailValid = false,
        isPasswordValid = false,
        isConfirmationPasswordValid = false
    )
}
