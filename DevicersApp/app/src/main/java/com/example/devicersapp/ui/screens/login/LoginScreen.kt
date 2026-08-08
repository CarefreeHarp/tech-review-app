package com.example.devicersapp.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.screens.login.components.ForgotPasswordLink
import com.example.devicersapp.ui.utils.authentication.AuthenticationDividerText
import com.example.devicersapp.ui.utils.authentication.AuthenticationField
import com.example.devicersapp.ui.utils.authentication.AuthenticationFooter
import com.example.devicersapp.ui.utils.authentication.AuthenticationHeader
import com.example.devicersapp.ui.utils.authentication.PrimaryButton
import com.example.devicersapp.ui.utils.authentication.ScreenTitle
import com.example.devicersapp.ui.utils.authentication.SocialButtons

/** Renders the static light-theme login screen from the Devicers prototype. */
@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    LoginScreenContent(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_light))
    )
}

/** Assembles the visual content displayed by [LoginScreen]. */
@Composable
fun LoginScreenContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Vertical spacing mirrors the composition of the light Figma screen.
        Spacer(modifier = Modifier.height(50.dp))
        AuthenticationHeader()
        Spacer(modifier = Modifier.height(50.dp))
        ScreenTitle(R.string.sign_in_title, R.string.sign_in_description)
        Spacer(modifier = Modifier.height(60.dp))
        AuthenticationField(R.string.email, R.string.email_placeholder)
        Spacer(modifier = Modifier.height(30.dp))
        AuthenticationField(R.string.password, R.string.password_placeholder, isPassword = true)
        ForgotPasswordLink(modifier = Modifier.align(Alignment.End))
        Spacer(modifier = Modifier.height(25.dp))
        PrimaryButton(R.string.sign_in, Modifier.fillMaxWidth().height(48.dp))
        Spacer(modifier = Modifier.height(25.dp))
        AuthenticationDividerText()
        Spacer(modifier = Modifier.height(25.dp))
        SocialButtons()
        Spacer(modifier = Modifier.height(20.dp))
        AuthenticationFooter(R.string.no_account, R.string.create_account)
    }
}

/** Provides an editor preview of the complete login composition. */
@Composable
@Preview(showBackground = true, locale = "es")
fun LoginScreenPreview() {
    LoginScreen()
}

/** Provides an editor preview of the visual login content. */
@Composable
@Preview(showBackground = true, locale = "es")
fun LoginScreenContentPreview() {
    LoginScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_light))
    )
}
