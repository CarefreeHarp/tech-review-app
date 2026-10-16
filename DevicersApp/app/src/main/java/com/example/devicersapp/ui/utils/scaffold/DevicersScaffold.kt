package com.example.devicersapp.ui.utils.scaffold

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.devicersapp.ui.utils.profile.ProfileOptionsContent
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
 * @param onEditProfileClick Edición habilitada por navegación solo para el perfil de sesión.
 * @param onSavedReviewsClick Abre los guardados desde la barra lateral del perfil propio.
 * @param onUserPreferencesClick Abre la pantalla independiente de preferencias.
 * @param content Contenido principal de la pantalla.
 * @param topBarUserId Identificador del usuario mostrado en la barra de perfil.
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
    topBarUserId: String? = null,
    modifier: Modifier = Modifier,
    onNavigationItemClick: (String) -> Unit = {},
    onTopBarBackClick: () -> Unit = {},
    onSignOutClick: () -> Unit = {},
    onEditProfileClick: (() -> Unit)? = null,
    onSavedReviewsClick: (() -> Unit)? = null,
    onUserPreferencesClick: (() -> Unit)? = null,
    onProfileClick: (String) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val colors = LocalDevicersColors.current

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val coroutineScope = rememberCoroutineScope()

    val profileDrawerState = rememberDrawerState(DrawerValue.Closed)
    val layoutDirection = LocalLayoutDirection.current
    BackHandler(enabled = profileDrawerState.isOpen) {
        coroutineScope.launch { profileDrawerState.close() }
    }
    // La inversión se limita al contenedor del panel para abrirlo desde la derecha.
    // El contenido de la aplicación conserva su dirección y su menú principal izquierdo.
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = profileDrawerState,
            modifier = modifier.fillMaxSize(),
            gesturesEnabled = profileDrawerState.isOpen,
            scrimColor = colors.textPrimary.copy(alpha = 0.32f),
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                    ProfileOptionsContent(
                        onSignOutClick = { coroutineScope.launch { profileDrawerState.close(); onSignOutClick() } },
                        onEditProfileClick = onEditProfileClick?.let { edit ->
                            { coroutineScope.launch { profileDrawerState.close(); edit() }; Unit }
                        },
                        onSavedReviewsClick = onSavedReviewsClick?.let { saved ->
                            { coroutineScope.launch { profileDrawerState.close(); saved() }; Unit }
                        },
                        onUserPreferencesClick = onUserPreferencesClick?.let { preferences ->
                            { coroutineScope.launch { profileDrawerState.close(); preferences() }; Unit }
                        }
                    )
                }
            }
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
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
                        modifier = Modifier.fillMaxSize(),
                        containerColor = colors.background,
                        topBar = {
                            // Centraliza las variantes de encabezado para que
                            // las pantallas solo soliciten su número.
                            when (topBarNumber) {
                                1 -> TopBar1(
                                    onProfileClick = onProfileClick,
                                    userHandleResId = topBarUserHandleResId,
                                    userHandle = topBarUserHandle,
                                    userId = topBarUserId,
                                    onBackClick = onTopBarBackClick,
                                    onOptionsClick = { coroutineScope.launch { profileDrawerState.open() } }
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

                                13 -> TitleTopBar(R.string.follow_list_title, onTopBarBackClick)

                                12 -> TitleTopBar(R.string.profile_edit, onTopBarBackClick)

                                11 -> TitleTopBar(R.string.edit_review_title, onTopBarBackClick)

                                10 -> TopBar10(
                                    onProfileClick = onProfileClick,
                                    profileImageUrl = topBarProfileImageUrl,
                                    profileId = topBarUserId
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
        }
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
