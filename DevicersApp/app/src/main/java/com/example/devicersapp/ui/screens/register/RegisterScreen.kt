package com.example.devicersapp.ui.screens.register

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
import com.example.devicersapp.ui.screens.register.components.RegisterValidationWarning
import com.example.devicersapp.ui.utils.authentication.AuthenticationDividerText
import com.example.devicersapp.ui.utils.authentication.AuthenticationField
import com.example.devicersapp.ui.utils.authentication.AuthenticationFooter
import com.example.devicersapp.ui.utils.authentication.AuthenticationHeader
import com.example.devicersapp.ui.utils.authentication.PrimaryButton
import com.example.devicersapp.ui.utils.authentication.ScreenTitle
import com.example.devicersapp.ui.utils.authentication.SocialButtons

/** Verifica un formato básico de correo con usuario, arroba, dominio y extensión. */
private val EmailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

/** Exige una contraseña de seis caracteres o más que contenga al menos tres dígitos. */
private val PasswordRegex = Regex("^(?=(?:.*\\d){3,}).{6,}$")

/** Renderiza la pantalla estática de creación de cuenta del tema claro de Devicers. */
@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
    // Estado elevado del formulario para que los campos compartidos no conserven datos de negocio.
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmationPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var showValidationWarning by remember { mutableStateOf(false) }

    // Las validaciones se recalculan desde el estado actual sin convertirse en estado duplicado.
    val isEmailValid = email.matches(EmailRegex)
    val isPasswordValid = password.matches(PasswordRegex)
    val isConfirmationPasswordValid = confirmationPassword.isNotEmpty() && password == confirmationPassword

    RegisterScreenContent(
        username = username,
        email = email,
        password = password,
        confirmationPassword = confirmationPassword,
        isPasswordVisible = isPasswordVisible,
        showValidationWarning = showValidationWarning,
        isEmailValid = isEmailValid,
        isPasswordValid = isPasswordValid,
        isConfirmationPasswordValid = isConfirmationPasswordValid,
        onUsernameChange = { username = it },
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onConfirmationPasswordChange = { confirmationPassword = it },
        onPasswordVisibilityChange = { isPasswordVisible = !isPasswordVisible },
        onCreateAccount = { showValidationWarning = true },
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Reúne el contenido visual del formulario de registro controlado por [RegisterScreen].
 *
 * @param username Nombre de usuario controlado por la pantalla.
 * @param email Correo controlado por la pantalla.
 * @param password Contraseña controlada por la pantalla.
 * @param confirmationPassword Confirmación de contraseña controlada por la pantalla.
 * @param isPasswordVisible Indica si la contraseña se muestra sin ocultar.
 * @param showValidationWarning Indica si se deben mostrar requisitos incumplidos.
 * @param isEmailValid Indica si el correo cumple la expresión regular definida.
 * @param isPasswordValid Indica si la contraseña cumple la expresión regular definida.
 * @param isConfirmationPasswordValid Indica si la confirmación coincide con la contraseña.
 * @param onUsernameChange Acción que solicita actualizar el nombre de usuario.
 * @param onEmailChange Acción que solicita actualizar el correo.
 * @param onPasswordChange Acción que solicita actualizar la contraseña.
 * @param onConfirmationPasswordChange Acción que solicita actualizar la confirmación.
 * @param onPasswordVisibilityChange Acción que solicita alternar la visibilidad de la contraseña.
 * @param onCreateAccount Acción que solicita validar el formulario de registro.
 * @param modifier Modificador aplicado al contenido.
 */
@Composable
fun RegisterScreenContent(
    username: String,
    email: String,
    password: String,
    confirmationPassword: String,
    isPasswordVisible: Boolean,
    showValidationWarning: Boolean,
    isEmailValid: Boolean,
    isPasswordValid: Boolean,
    isConfirmationPasswordValid: Boolean,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmationPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            placeholderResId = R.string.username_placeholder,
            value = username,
            onValueChange = onUsernameChange
        )
        Spacer(modifier = Modifier.height(16.dp))
        AuthenticationField(
            labelResId = R.string.email,
            placeholderResId = R.string.email_placeholder,
            value = email,
            onValueChange = onEmailChange
        )
        Spacer(modifier = Modifier.height(16.dp))
        AuthenticationField(
            labelResId = R.string.password,
            placeholderResId = R.string.password_placeholder,
            value = password,
            onValueChange = onPasswordChange,
            isPassword = true,
            isPasswordVisible = isPasswordVisible,
            onPasswordVisibilityChange = onPasswordVisibilityChange
        )
        Spacer(modifier = Modifier.height(16.dp))
        AuthenticationField(
            labelResId = R.string.confirm_password,
            placeholderResId = R.string.password_placeholder,
            value = confirmationPassword,
            onValueChange = onConfirmationPasswordChange,
            isPassword = true,
            isPasswordVisible = isPasswordVisible,
            onPasswordVisibilityChange = onPasswordVisibilityChange
        )
        Spacer(modifier = Modifier.height(20.dp))
        // La advertencia se muestra únicamente después de que el usuario intenta crear la cuenta.
        if (showValidationWarning && (
                !isEmailValid || !isPasswordValid || !isConfirmationPasswordValid
            )
        ) {
            RegisterValidationWarning(
                isEmailValid = isEmailValid,
                isPasswordValid = isPasswordValid,
                isConfirmationPasswordValid = isConfirmationPasswordValid
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        PrimaryButton(
            textResId = R.string.create_account,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            onClick = onCreateAccount
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
