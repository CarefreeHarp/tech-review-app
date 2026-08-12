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

/** Renderiza la pantalla estática de creación de cuenta del tema claro de Devicers. */
@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
    RegisterScreenContent(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_light))
    )
}

/** Reúne el contenido visual que muestra [RegisterScreen]. */
@Composable
fun RegisterScreenContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // El espaciado vertical conserva el ritmo visual definido por el prototipo claro.
        Spacer(modifier = Modifier.height(28.dp))
        AuthenticationHeader()
        Spacer(modifier = Modifier.height(55.dp))
        ScreenTitle(R.string.create_account, R.string.create_account_description)
        Spacer(modifier = Modifier.height(28.dp))
        AuthenticationField(
            labelResId = R.string.username,
            placeholderResId = R.string.username_placeholder
        )
        Spacer(modifier = Modifier.height(16.dp))
        AuthenticationField(
            labelResId = R.string.email,
            placeholderResId = R.string.email_placeholder
        )
        Spacer(modifier = Modifier.height(16.dp))
        AuthenticationField(
            labelResId = R.string.password,
            placeholderResId = R.string.password_placeholder,
            isPassword = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        AuthenticationField(
            labelResId = R.string.confirm_password,
            placeholderResId = R.string.password_placeholder,
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

/** Muestra una vista previa de la composición completa de creación de cuenta. */
@Composable
@Preview(showBackground = true)
fun RegisterScreenPreview() {
    RegisterScreen()
}
