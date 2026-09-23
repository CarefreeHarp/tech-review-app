package com.example.devicersapp.ui.utils.scaffold

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.navigation.BottomNavigationBar
import com.example.devicersapp.ui.utils.navigation.DevicersDrawerContent
import com.example.devicersapp.ui.utils.navigation.TitleTopBar
import com.example.devicersapp.ui.utils.navigation.TopBar1
import com.example.devicersapp.ui.utils.navigation.TopBar10
import com.example.devicersapp.ui.utils.navigation.TopBar2
import com.example.devicersapp.ui.utils.navigation.TopBar3
import com.example.devicersapp.ui.utils.navigation.TopBar4
import com.example.devicersapp.ui.utils.navigation.TopBar5
import com.example.devicersapp.ui.utils.navigation.TopBar6
import kotlinx.coroutines.launch

/**
 * Define la estructura general de las pantallas de Devicers.
 *
 * @param selectedItem Identificador del elemento seleccionado en la navegación principal.
 * @param showBottomBar Indica si el Scaffold debe mostrar la barra de navegación inferior.
 * @param showDrawer Indica si la pantalla permite utilizar el menú lateral.
 * @param topBarNumber Número de la barra superior que debe mostrar la pantalla, o `null` si no tiene.
 * @param topBarUserHandleResId Recurso del nombre de usuario que muestra la barra superior de perfil.
 * @param topBarUserHandle Alias dinámico de la cuenta autenticada que sustituye el recurso cuando existe.
 * @param topBarProfileImageUrl URL de la foto del usuario autenticado para encabezados que la muestran.
 * @param modifier Modificador aplicado al contenedor principal.
 * @param onNavigationItemClick Acción solicitada al seleccionar un elemento de navegación.
 * @param onTopBarBackClick Acción solicitada por una barra superior con regreso interactivo.
 * @param onSignOutClick Acción solicitada al cerrar sesión.
 * @param content Contenido principal de la pantalla.
 */
@Composable
fun DevicersScaffold(
    selectedItem: String = "",
    showBottomBar: Boolean = false,
    showDrawer: Boolean = false,
    topBarNumber: Int? = null,
    @StringRes topBarUserHandleResId: Int? = null,
    topBarUserHandle: String? = null,
    topBarProfileImageUrl: String? = null,
    modifier: Modifier = Modifier,
    onNavigationItemClick: (String) -> Unit = {},
    onTopBarBackClick: () -> Unit = {},
    onSignOutClick: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val colors = LocalDevicersColors.current

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showDrawer,
        drawerContent = {
            DevicersDrawerContent(
                selectedItem = selectedItem,
                onItemClick = { route ->
                    coroutineScope.launch {
                        drawerState.close()
                    }

                    onNavigationItemClick(route)
                },
                onSignOutClick = {
                    coroutineScope.launch {
                        drawerState.close()
                    }

                    onSignOutClick()
                }
            )
        }
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = colors.background,
            topBar = {
                // Centraliza las variantes de encabezado para que
                // las pantallas solo soliciten su número.
                when (topBarNumber) {
                    1 -> TopBar1(
                        userHandleResId = topBarUserHandleResId,
                        userHandle = topBarUserHandle,
                        onBackClick = onTopBarBackClick,
                        onSignOutClick = onSignOutClick
                    )

                    2 -> TopBar2(
                        onBackClick = onTopBarBackClick
                    )

                    3 -> TopBar3(
                        onBackClick = onTopBarBackClick
                    )

                    4 -> TopBar4(
                        onBackClick = onTopBarBackClick
                    )

                    5 -> TopBar5()

                    6 -> TopBar6(
                        onBackClick = onTopBarBackClick
                    )

                    7 -> TitleTopBar(
                        R.string.profile_search_results_title,
                        onTopBarBackClick
                    )

                    8 -> TitleTopBar(
                        R.string.found_products_title,
                        onTopBarBackClick
                    )

                    9 -> TitleTopBar(
                        R.string.request_product_title,
                        onTopBarBackClick
                    )

                    10 -> TopBar10(
                        profileImageUrl = topBarProfileImageUrl
                    )
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
}

/**
 * Muestra una vista previa del Scaffold con la barra
 * de navegación inferior.
 */
@Preview(showBackground = true)
@Composable
fun DevicersScaffoldPreview() {
    DevicersAppTheme {
        DevicersScaffold(
            selectedItem = "home",
            showBottomBar = true
        ) { }
    }
}