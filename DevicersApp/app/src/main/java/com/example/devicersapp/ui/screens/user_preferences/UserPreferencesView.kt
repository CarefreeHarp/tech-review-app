package com.example.devicersapp.ui.screens.user_preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.ui.screens.user_preferences.components.UserPreferencesHeader
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/** Observa la presentación independiente de preferencias dentro del contenedor estándar. */
@Composable
fun UserPreferencesView(viewModel: UserPreferencesViewModel, modifier: Modifier = Modifier, onBackClick: () -> Unit = {}) {
    val state by viewModel.uiState.collectAsState()
    Scaffold(modifier = modifier.fillMaxSize(), containerColor = LocalDevicersColors.current.background,
        topBar = { UserPreferencesHeader(state.titleResId, onBackClick) }) { padding ->
        UserPreferencesViewContent(state = state, modifier = Modifier.padding(padding).fillMaxSize())
    }
}

/** Muestra el mensaje provisional del contenido, sin preferencias o datos de usuario. */
@Composable
fun UserPreferencesViewContent(state: UserPreferencesState, modifier: Modifier = Modifier) {
    val colors = LocalDevicersColors.current
    Column(modifier = modifier.background(colors.background).padding(20.dp)) {
        Text(stringResource(state.emptyMessageResId), style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)
    }
}

/** Previsualiza la vista raíz sin crear una sesión ni un usuario de ejemplo. */
@Preview(showBackground = true, heightDp = 900)
@Composable
fun UserPreferencesViewPreview() {
    val viewModel = remember { UserPreferencesViewModel() }
    DevicersAppTheme { UserPreferencesView(viewModel) }
}
