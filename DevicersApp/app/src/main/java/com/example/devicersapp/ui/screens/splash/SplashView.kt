package com.example.devicersapp.ui.screens.splash

import com.example.devicersapp.ui.utils.loading.CenteredLoading

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.devicersapp.ui.theme.DevicersAppTheme

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

/** Centra el indicador de carga mientras se determina el estado de autenticación. */
@Composable
fun SplashViewContent(
    state: SplashState,
    modifier: Modifier = Modifier
) {
    CenteredLoading(modifier)
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
