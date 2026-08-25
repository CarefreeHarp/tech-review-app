package com.example.devicersapp.ui.utils.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.annotation.StringRes
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.navigation.BottomNavigationBar
import com.example.devicersapp.ui.utils.navigation.TopBar1
import com.example.devicersapp.ui.utils.navigation.TopBar2
import com.example.devicersapp.ui.utils.navigation.TopBar3
import com.example.devicersapp.ui.utils.navigation.TopBar4
import com.example.devicersapp.ui.utils.navigation.TopBar5
import com.example.devicersapp.R
import com.example.devicersapp.ui.utils.navigation.TitleTopBar
import com.example.devicersapp.ui.utils.navigation.TopBar6
import com.example.devicersapp.ui.utils.navigation.TopBar10

/**
 * Define la estructura general de las pantallas de Devicers.
 *
 * @param selectedItem Identificador del elemento seleccionado en la barra inferior.
 * @param showBottomBar Indica si el Scaffold debe mostrar la barra de navegación inferior.
 * @param topBarNumber Número de la barra superior que debe mostrar la pantalla, o `null` si no tiene.
 * @param topBarUserHandleResId Recurso del nombre de usuario que muestra la barra superior de perfil.
 * @param modifier Modificador aplicado al Scaffold.
 * @param onNavigationItemClick Acción solicitada al seleccionar un elemento de navegación.
 * @param onTopBarBackClick Acción solicitada por una barra superior con regreso interactivo.
 * @param content Contenido principal de la pantalla.
 */
@Composable
fun DevicersScaffold(
    selectedItem: String = "",
    showBottomBar: Boolean = false,
    topBarNumber: Int? = null,
    @StringRes topBarUserHandleResId: Int? = null,
    modifier: Modifier = Modifier,
    onNavigationItemClick: (String) -> Unit = {},
    onTopBarBackClick: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val colors = LocalDevicersColors.current
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            // Centraliza las variantes de encabezado para que las pantallas solo soliciten su número.
            when (topBarNumber) {
                1 -> TopBar1(
                    userHandleResId = topBarUserHandleResId,
                    onBackClick = onTopBarBackClick
                )
                2 -> TopBar2(onBackClick = onTopBarBackClick)
                3 -> TopBar3(onBackClick = onTopBarBackClick)
                4 -> TopBar4(onBackClick = onTopBarBackClick)
                5 -> TopBar5()
                6 -> TopBar6(onBackClick = onTopBarBackClick)
                // Las barras que solo cambian de título reutilizan la misma variante.
                7 -> TitleTopBar(R.string.profile_search_results_title, onTopBarBackClick)
                8 -> TitleTopBar(R.string.found_products_title, onTopBarBackClick)
                9 -> TitleTopBar(R.string.request_product_title, onTopBarBackClick)
                10 -> TopBar10()
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    selectedItem = selectedItem,
                    onItemClick = onNavigationItemClick
                )
            }
        },
        content = content
    )
}

/** Muestra una vista previa del Scaffold con la barra de navegación inferior. */
@Preview(showBackground = true)
@Composable
fun DevicersScaffoldPreview() {
    DevicersAppTheme {
        DevicersScaffold(selectedItem = "home", showBottomBar = true) { }
    }
}
