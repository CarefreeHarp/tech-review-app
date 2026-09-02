package com.example.devicersapp.ui.screens.access

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
import com.example.devicersapp.ui.screens.access.components.ForgotPasswordLink
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.authentication.AuthenticationDividerText
import com.example.devicersapp.ui.utils.authentication.AuthenticationField
import com.example.devicersapp.ui.utils.authentication.AuthenticationFooter
import com.example.devicersapp.ui.utils.authentication.PrimaryButton
import com.example.devicersapp.ui.utils.authentication.ScreenTitle
import com.example.devicersapp.ui.utils.authentication.SocialButtons
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/** Renderiza la pantalla de acceso y observa su estado desde el ViewModel. */
@Composable
fun AccessView(
    modifier: Modifier = Modifier,
    onSignInClick: () -> Unit,
    onCreateAccountClick: () -> Unit = {},
    viewModel: AccessViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    AccessViewContent(
        email = uiState.email,
        password = uiState.password,
        isPasswordVisible = uiState.isPasswordVisible,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onPasswordVisibilityChange = viewModel::onPasswordVisibilityChange,
        onSignInClick = onSignInClick,
        onCreateAccountClick = onCreateAccountClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/** Contenido visual y sin estado propio de la pantalla de acceso. */
@Composable
fun AccessViewContent(
    email: String,
    password: String,
    isPasswordVisible: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    onSignInClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        ScreenTitle(
            R.string.sign_in_title,
            R.string.sign_in_description
        )

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

        ForgotPasswordLink(
            modifier = Modifier.align(Alignment.End)
        )

        Spacer(modifier = Modifier.height(18.dp))

        PrimaryButton(
            textResId = R.string.sign_in,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            onClick = onSignInClick
        )

        Spacer(modifier = Modifier.height(26.dp))

        AuthenticationDividerText()

        Spacer(modifier = Modifier.height(18.dp))

        SocialButtons()

        Spacer(modifier = Modifier.height(28.dp))

        AuthenticationFooter(
            textResId = R.string.no_account,
            actionResId = R.string.create_account,
            onActionClick = onCreateAccountClick
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
@Preview(showBackground = true, heightDp = 800)
fun AccessViewPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(topBarNumber = 5) { innerPadding ->
            AccessView(
                modifier = Modifier.padding(innerPadding),
                onSignInClick = {},
                onCreateAccountClick = {},
                viewModel = AccessViewModel()
            )
        }
    }
}

@Composable
@Preview(showBackground = true, heightDp = 800)
fun AccessViewDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(topBarNumber = 5) { innerPadding ->
            AccessView(
                modifier = Modifier.padding(innerPadding),
                onSignInClick = {},
                onCreateAccountClick = {},
                viewModel = AccessViewModel()
            )
        }
    }
}