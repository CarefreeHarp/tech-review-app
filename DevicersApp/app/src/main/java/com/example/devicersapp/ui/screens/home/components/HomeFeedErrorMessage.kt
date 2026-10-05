package com.example.devicersapp.ui.screens.home.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.authentication.PrimaryButton

/**
 * Informa que el feed no se pudo cargar y ofrece intentarlo de nuevo.
 *
 * @param messageResId Mensaje que explica el error al usuario.
 * @param onRetryClick Acción solicitada al reintentar la carga.
 * @param modifier Modificador aplicado al contenedor del mensaje.
 */
@Composable
fun HomeFeedErrorMessage(
    @StringRes messageResId: Int,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(messageResId),
            color = LocalDevicersColors.current.textSecondary,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        PrimaryButton(
            textResId = R.string.home_feed_retry,
            onClick = onRetryClick
        )
    }
}

/** Muestra una vista previa del mensaje de error del feed. */
@Composable
@Preview(showBackground = true)
fun HomeFeedErrorMessagePreview() {
    DevicersAppTheme {
        HomeFeedErrorMessage(
            messageResId = R.string.home_feed_error_connection,
            onRetryClick = {}
        )
    }
}
