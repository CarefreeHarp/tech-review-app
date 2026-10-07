package com.example.devicersapp.ui.screens.edit_review.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.devicersapp.R
import com.example.devicersapp.ui.screens.edit_review.EditReviewState
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/** Muestra el error inicial sin habilitar un formulario incompleto. */
@Composable
fun EditReviewStatus(state: EditReviewState, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        state.errorResId?.let {
            Text(stringResource(it), color = LocalDevicersColors.current.textPrimary)
            if (it != R.string.edit_review_owner_error) {
                TextButton(onClick = onRetry) { Text(stringResource(R.string.home_feed_retry)) }
            }
        }
    }
}

/** Permite revisar la presentación del fallo de carga. */
@Preview(showBackground = true)
@Composable
fun EditReviewStatusPreview() {
    DevicersAppTheme {
        EditReviewStatus(EditReviewState(errorResId = R.string.edit_review_load_error), onRetry = {})
    }
}
