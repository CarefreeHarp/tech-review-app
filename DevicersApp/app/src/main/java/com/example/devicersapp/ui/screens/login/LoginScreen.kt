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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

/** Renderiza la pantalla estática de inicio de sesión del tema claro de Devicers. */
@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    // La pantalla conserva el estado; los campos reutilizables solo lo muestran y emiten cambios.
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    LoginScreenContent(
        email = email,
        password = password,
        isPasswordVisible = isPasswordVisible,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onPasswordVisibilityChange = { isPasswordVisible = !isPasswordVisible },
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_light))
    )
}

/**
 * Reúne el contenido visual que muestra [LoginScreen].
 *
 * @param email Correo controlado por la pantalla de inicio de sesión.
 * @param password Contraseña controlada por la pantalla de inicio de sesión.
 * @param isPasswordVisible Indica si la contraseña se muestra sin ocultar.
 * @param onEmailChange Acción que solicita actualizar el correo.
 * @param onPasswordChange Acción que solicita actualizar la contraseña.
 * @param onPasswordVisibilityChange Acción que solicita alternar la visibilidad de la contraseña.
 * @param modifier Modificador aplicado al contenido.
 */
@Composable
fun LoginScreenContent(
    email: String,
    password: String,
    isPasswordVisible: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // El espaciado vertical reproduce la composición de la pantalla clara de Figma.
        Spacer(modifier = Modifier.height(28.dp))
        AuthenticationHeader()
        Spacer(modifier = Modifier.height(55.dp))
        ScreenTitle(R.string.sign_in_title, R.string.sign_in_description)
        Spacer(modifier = Modifier.height(60.dp))
        AuthenticationField(
            labelResId = R.string.email,
            placeholderResId = R.string.email_placeholder,
            value = email,
            onValueChange = onEmailChange
        )
        Spacer(modifier = Modifier.height(20.dp))
        AuthenticationField(
            labelResId = R.string.password,
            placeholderResId = R.string.password_placeholder,
            value = password,
            onValueChange = onPasswordChange,
            isPassword = true,
            isPasswordVisible = isPasswordVisible,
            onPasswordVisibilityChange = onPasswordVisibilityChange
        )
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

/** Muestra una vista previa de la composición completa de inicio de sesión. */
@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    LoginScreen()
}
