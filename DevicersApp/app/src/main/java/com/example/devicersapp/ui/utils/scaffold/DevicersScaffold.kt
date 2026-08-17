package com.example.devicersapp.ui.utils.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.example.devicersapp.R
import com.example.devicersapp.ui.utils.navigation.BottomNavigationBar

/**
 * Define la estructura general de las pantallas de Devicers.
 *
 * @param selectedItem Elemento seleccionado en la barra inferior.
 * Si es null, la barra de navegación no se muestra.
 * @param modifier Modificador aplicado al Scaffold.
 * @param content Contenido principal de la pantalla.
 */
@Composable
fun DevicersScaffold(
    selectedItem: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colorResource(R.color.background_light),
        bottomBar = {
            if (selectedItem != null) {
                BottomNavigationBar(
                    selectedItem = selectedItem
                )
            }
        },
        content = content
    )
}