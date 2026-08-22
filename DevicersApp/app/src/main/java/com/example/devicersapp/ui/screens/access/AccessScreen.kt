package com.example.devicersapp.ui.screens.access

import com.example.devicersapp.ui.theme.LocalDevicersColors

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.screens.access.components.ForgotPasswordLink
import com.example.devicersapp.ui.utils.authentication.AuthenticationDividerText
import com.example.devicersapp.ui.utils.authentication.AuthenticationField
import com.example.devicersapp.ui.utils.authentication.AuthenticationFooter
import com.example.devicersapp.ui.utils.authentication.PrimaryButton
import com.example.devicersapp.ui.utils.authentication.ScreenTitle
import com.example.devicersapp.ui.utils.authentication.SocialButtons

/** Renderiza la pantalla de acceso, la puerta de entrada a la comunidad de Devicers. */
@Composable
fun AccessScreen(modifier: Modifier = Modifier, onSignInClick: () -> Unit) {
    // La pantalla conserva el estado; los campos reutilizables solo lo muestran y emiten cambios.
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    AccessScreenContent(
        email = email,
        password = password,
        isPasswordVisible = isPasswordVisible,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onPasswordVisibilityChange = { isPasswordVisible = !isPasswordVisible },
        onSignInClick = onSignInClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Reúne el contenido visual que muestra [AccessScreen].
 *
 * @param email Correo controlado por la pantalla de acceso.
 * @param password Contraseña controlada por la pantalla de acceso.
 * @param isPasswordVisible Indica si la contraseña se muestra sin ocultar.
 * @param onEmailChange Acción que solicita actualizar el correo.
 * @param onPasswordChange Acción que solicita actualizar la contraseña.
 * @param onPasswordVisibilityChange Acción que solicita alternar la visibilidad de la contraseña.
 * @param modifier Modificador aplicado al contenido.
 */
@Composable
fun AccessScreenContent(
    email: String,
    password: String,
    isPasswordVisible: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // La marca se muestra desde TopBar5, gestionada por el Scaffold compartido.
        Spacer(modifier = Modifier.height(36.dp))
        ScreenTitle(R.string.sign_in_title, R.string.sign_in_description)
        Spacer(modifier = Modifier.height(32.dp))
        AuthenticationField(
            labelResId = R.string.email,
            placeholderResId = R.string.email_placeholder,
            value = email,
            onValueChange = onEmailChange
        )
        Spacer(modifier = Modifier.height(18.dp))
        AuthenticationField(
            labelResId = R.string.password,
            placeholderResId = R.string.password_placeholder,
            value = password,
            onValueChange = onPasswordChange,
            isPassword = true,
            isPasswordVisible = isPasswordVisible,
            onPasswordVisibilityChange = onPasswordVisibilityChange
        )
        // El enlace de recuperación se alinea al borde derecho del formulario.
        ForgotPasswordLink(modifier = Modifier.align(Alignment.End))
        Spacer(modifier = Modifier.height(18.dp))
        PrimaryButton(R.string.sign_in, Modifier.fillMaxWidth().height(52.dp), onClick = onSignInClick)
        Spacer(modifier = Modifier.height(26.dp))
        AuthenticationDividerText()
        Spacer(modifier = Modifier.height(18.dp))
        SocialButtons()
        Spacer(modifier = Modifier.height(28.dp))
        AuthenticationFooter(R.string.no_account, R.string.create_account)
        Spacer(modifier = Modifier.height(24.dp))
    }
}

/** Muestra una vista previa de la composición completa de acceso en el tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 800)
fun AccessScreenPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(topBarNumber = 5) { innerPadding ->
            AccessScreen(modifier = Modifier.padding(innerPadding), {})
        }
    }
}

/** Muestra una vista previa de la composición completa de acceso en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 800)
fun AccessScreenDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(topBarNumber = 5) { innerPadding ->
            AccessScreen(modifier = Modifier.padding(innerPadding), {})
        }
    }
}
