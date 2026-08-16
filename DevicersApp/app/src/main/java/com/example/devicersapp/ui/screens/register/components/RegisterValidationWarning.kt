package com.example.devicersapp.ui.screens.register.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
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
 * @param modifier Modificador aplicado al aviso.
 */
@Composable
fun RegisterValidationWarning(
    isEmailValid: Boolean,
    isPasswordValid: Boolean,
    isConfirmationPasswordValid: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = colorResource(R.color.surface_secondary_light),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(12.dp)
    ) {
        if (!isEmailValid) {
            Text(
                text = stringResource(R.string.register_validation_email),
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.text_primary_light)
            )
        }
        if (!isPasswordValid) {
            Text(
                text = stringResource(R.string.register_validation_password),
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.text_primary_light)
            )
        }
        if (!isConfirmationPasswordValid) {
            Text(
                text = stringResource(R.string.register_validation_confirmation),
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.text_primary_light)
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
