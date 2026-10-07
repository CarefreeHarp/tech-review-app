package com.example.devicersapp.ui.screens.splash.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/** Indica la comprobación inicial de sesión, antes de conocer la pantalla de destino. */
@Composable
fun SplashLoading(modifier: Modifier = Modifier) {
    val colors = LocalDevicersColors.current
    val description = stringResource(R.string.screen_loading)
    Box(modifier = modifier.fillMaxSize().background(colors.background), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = colors.primary,
            modifier = Modifier.semantics { contentDescription = description })
    }
}

/** Muestra el indicador exclusivo de la comprobación inicial de sesión. */
@Preview(showBackground = true)
@Composable
fun SplashLoadingPreview() {
    DevicersAppTheme { SplashLoading() }
}
