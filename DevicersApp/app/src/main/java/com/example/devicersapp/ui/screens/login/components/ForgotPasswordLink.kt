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
 * Displays a password-recovery action with the visual appearance of plain text.
 * The empty callback intentionally preserves the current frontend-only prototype.
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

/** Provides an editor preview of the static password-recovery link. */
@Composable
@Preview(showBackground = true, locale = "es")
fun ForgotPasswordLinkPreview() {
    ForgotPasswordLink()
}
