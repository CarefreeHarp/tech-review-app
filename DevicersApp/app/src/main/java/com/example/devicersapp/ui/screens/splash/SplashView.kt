package com.example.devicersapp.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.authentication.AuthenticationHeader

/** Observa la sesión inicial y delega la navegación al destino correspondiente. */
@Composable
fun SplashView(
    onUserAuthenticated: () -> Unit,
    onUserUnauthenticated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isUserAuthenticated) {
        when (uiState.isUserAuthenticated) {
            true -> onUserAuthenticated()
            false -> onUserUnauthenticated()
            null -> Unit
        }
    }

    SplashViewContent(
        state = uiState,
        modifier = modifier
    )
}

/** Muestra la identidad de la aplicación mientras se determina el estado de autenticación. */
@Composable
fun SplashViewContent(
    state: SplashState,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AuthenticationHeader()
            Spacer(modifier = Modifier.height(20.dp))
            CircularProgressIndicator(color = colors.primary)
        }
    }
}

/** Muestra una vista previa de la pantalla de bienvenida. */
@Composable
@Preview(showBackground = true)
fun SplashViewPreview() {
    DevicersAppTheme {
        SplashViewContent(
            state = SplashState()
        )
    }
}
