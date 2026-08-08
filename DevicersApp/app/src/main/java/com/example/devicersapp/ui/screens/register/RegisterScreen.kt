package com.example.devicersapp.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.utils.authentication.AuthenticationDividerText
import com.example.devicersapp.ui.utils.authentication.AuthenticationField
import com.example.devicersapp.ui.utils.authentication.AuthenticationFooter
import com.example.devicersapp.ui.utils.authentication.AuthenticationHeader
import com.example.devicersapp.ui.utils.authentication.PrimaryButton
import com.example.devicersapp.ui.utils.authentication.ScreenTitle
import com.example.devicersapp.ui.utils.authentication.SocialButtons

/** Renders the static light-theme registration screen from the Devicers prototype. */
@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
    RegisterScreenContent(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_light))
    )
}

/** Assembles the visual content displayed by [RegisterScreen]. */
@Composable
fun RegisterScreenContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Vertical spacing preserves the visual rhythm defined by the light prototype.
        Spacer(modifier = Modifier.height(50.dp))
        AuthenticationHeader()
        Spacer(modifier = Modifier.height(44.dp))
        ScreenTitle(R.string.create_account, R.string.create_account_description)
        Spacer(modifier = Modifier.height(28.dp))
        AuthenticationField(
            label = R.string.username,
            placeholder = R.string.username_placeholder
        )
        Spacer(modifier = Modifier.height(16.dp))
        AuthenticationField(
            label = R.string.email,
            placeholder = R.string.email_placeholder
        )
        Spacer(modifier = Modifier.height(16.dp))
        AuthenticationField(
            label = R.string.password,
            placeholder = R.string.password_placeholder,
            isPassword = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        AuthenticationField(
            label = R.string.confirm_password,
            placeholder = R.string.password_placeholder,
            isPassword = true
        )
        Spacer(modifier = Modifier.height(20.dp))
        PrimaryButton(
            textResId = R.string.create_account,
            modifier = Modifier.fillMaxWidth().height(48.dp)
        )
        Spacer(modifier = Modifier.height(18.dp))
        AuthenticationDividerText()
        Spacer(modifier = Modifier.height(12.dp))
        SocialButtons()
        Spacer(modifier = Modifier.height(20.dp))
        AuthenticationFooter(R.string.already_have_account, R.string.sign_in)
    }
}

/** Provides an editor preview of the complete registration composition. */
@Composable
@Preview(showBackground = true, locale = "es")
fun RegisterScreenPreview() {
    RegisterScreen()
}

/** Provides an editor preview of the visual registration content. */
@Composable
@Preview(showBackground = true, locale = "es")
fun RegisterScreenContentPreview() {
    RegisterScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_light))
    )
}
