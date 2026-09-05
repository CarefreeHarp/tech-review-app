package com.example.devicersapp.ui.screens.access.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/** Muestra el mensaje de error retornado por Firebase al intentar iniciar sesión. */
@Composable
fun AccessAuthenticationError(
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = LocalDevicersColors.current.surfaceSecondary,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = LocalDevicersColors.current.error,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(14.dp)
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodySmall,
            color = LocalDevicersColors.current.error
        )
    }
}

/** Muestra una vista previa del mensaje de error de autenticación. */
@Composable
@Preview(showBackground = true)
fun AccessAuthenticationErrorPreview() {
    DevicersAppTheme {
        AccessAuthenticationError(
            errorMessage = stringResource(R.string.access_authentication_error_preview)
        )
    }
}
