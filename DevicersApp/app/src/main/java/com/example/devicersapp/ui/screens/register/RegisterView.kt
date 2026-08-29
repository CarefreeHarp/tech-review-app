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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.screens.register.components.RegisterValidationWarning
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.authentication.AuthenticationDividerText
import com.example.devicersapp.ui.utils.authentication.AuthenticationField
import com.example.devicersapp.ui.utils.authentication.AuthenticationFooter
import com.example.devicersapp.ui.utils.authentication.PrimaryButton
import com.example.devicersapp.ui.utils.authentication.ScreenTitle
import com.example.devicersapp.ui.utils.authentication.SocialButtons
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/** Renderiza el registro y conecta sus eventos con el estado del ViewModel. */
@Composable
fun RegisterView(
    onCreateAccountClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    RegisterViewContent(
        uiState = uiState,
        onUsernameChange = viewModel::onUsernameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmationPasswordChange = viewModel::onConfirmationPasswordChange,
        onPasswordVisibilityChange = viewModel::onPasswordVisibilityChange,
        onCreateAccount = {
            if (viewModel.onCreateAccount()) {
                onCreateAccountClick()
            }
        },
        onSignInClick = onSignInClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/** Ensambla el formulario controlado por el estado inmutable de registro. */
@Composable
fun RegisterViewContent(
    uiState: RegisterState,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmationPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    onCreateAccount: () -> Unit,
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(36.dp))
        ScreenTitle(R.string.create_account_title, R.string.create_account_description)
        Spacer(modifier = Modifier.height(32.dp))
        AuthenticationField(
            labelResId = R.string.username,
            placeholderResId = R.string.username_placeholder,
            value = uiState.username,
            onValueChange = onUsernameChange
        )
        Spacer(modifier = Modifier.height(18.dp))
        AuthenticationField(
            labelResId = R.string.email,
            placeholderResId = R.string.email_placeholder,
            value = uiState.email,
            onValueChange = onEmailChange
        )
        Spacer(modifier = Modifier.height(18.dp))
        AuthenticationField(
            labelResId = R.string.password,
            placeholderResId = R.string.password_placeholder,
            value = uiState.password,
            onValueChange = onPasswordChange,
            isPassword = true,
            isPasswordVisible = uiState.isPasswordVisible,
            onPasswordVisibilityChange = onPasswordVisibilityChange
        )
        Spacer(modifier = Modifier.height(18.dp))
        AuthenticationField(
            labelResId = R.string.confirm_password,
            placeholderResId = R.string.password_placeholder,
            value = uiState.confirmationPassword,
            onValueChange = onConfirmationPasswordChange,
            isPassword = true,
            isPasswordVisible = uiState.isPasswordVisible,
            onPasswordVisibilityChange = onPasswordVisibilityChange
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.showValidationWarning &&
            (!uiState.isEmailValid || !uiState.isPasswordValid ||
                !uiState.isConfirmationPasswordValid)
        ) {
            RegisterValidationWarning(
                isEmailValid = uiState.isEmailValid,
                isPasswordValid = uiState.isPasswordValid,
                isConfirmationPasswordValid = uiState.isConfirmationPasswordValid
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        PrimaryButton(
            textResId = R.string.create_account,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            onClick = onCreateAccount
        )
        Spacer(modifier = Modifier.height(26.dp))
        AuthenticationDividerText()
        Spacer(modifier = Modifier.height(18.dp))
        SocialButtons()
        Spacer(modifier = Modifier.height(28.dp))
        AuthenticationFooter(
            textResId = R.string.already_have_account,
            actionResId = R.string.sign_in,
            onActionClick = onSignInClick
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

/** Muestra la vista de registro completa en tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 900)
fun RegisterViewPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(topBarNumber = 5) { innerPadding ->
            RegisterView(
                modifier = Modifier.padding(innerPadding),
                viewModel = RegisterViewModel()
            )
        }
    }
}

/** Muestra la vista de registro completa en tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 900)
fun RegisterViewDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(topBarNumber = 5) { innerPadding ->
            RegisterView(
                modifier = Modifier.padding(innerPadding),
                viewModel = RegisterViewModel()
            )
        }
    }
}
