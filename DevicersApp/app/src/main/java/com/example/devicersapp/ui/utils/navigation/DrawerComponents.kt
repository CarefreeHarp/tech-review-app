package com.example.devicersapp.ui.utils.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Representa un elemento disponible dentro del menú lateral.
 *
 * @param route Ruta de navegación asociada al elemento.
 * @param labelResId Recurso de texto mostrado al usuario.
 * @param iconResId Recurso gráfico utilizado como icono.
 */
private data class DrawerItem(
    val route: String,
    @StringRes val labelResId: Int,
    @DrawableRes val iconResId: Int
)

/**
 * Destinos principales disponibles desde el menú lateral.
 *
 * Se utilizan las mismas rutas que la barra de navegación inferior
 * para mantener consistente la navegación principal de la aplicación.
 */
private val drawerItems = listOf(
    DrawerItem(
        route = "home",
        labelResId = R.string.navigation_home,
        iconResId = R.drawable.home_icon
    ),
    DrawerItem(
        route = "search",
        labelResId = R.string.navigation_search,
        iconResId = R.drawable.explore_icon
    ),
    DrawerItem(
        route = "create",
        labelResId = R.string.navigation_create,
        iconResId = R.drawable.create_review_icon
    ),
    DrawerItem(
        route = "activity",
        labelResId = R.string.navigation_activity,
        iconResId = R.drawable.notifications_icon
    ),
    DrawerItem(
        route = "profile",
        labelResId = R.string.navigation_profile,
        iconResId = R.drawable.profile_icon
    )
)

/**
 * Contenido reutilizable del menú lateral de Devicers.
 *
 * Este componente únicamente representa la interfaz del Drawer.
 * La navegación real continúa siendo responsabilidad de AppNavigation.
 *
 * @param selectedItem Ruta actualmente seleccionada.
 * @param onItemClick Acción solicitada al seleccionar un destino.
 * @param onSignOutClick Acción solicitada al cerrar sesión.
 * @param modifier Modificador aplicado al contenedor del Drawer.
 */
@Composable
fun DevicersDrawerContent(
    selectedItem: String,
    onItemClick: (String) -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    ModalDrawerSheet(
        modifier = modifier.fillMaxHeight(),
        drawerContainerColor = colors.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = "Devicers",
                modifier = Modifier.padding(
                    horizontal = 24.dp,
                    vertical = 16.dp
                ),
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )

            HorizontalDivider(
                color = colors.border
            )

            drawerItems.forEach { item ->
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = stringResource(item.labelResId)
                        )
                    },
                    selected = selectedItem == item.route,
                    onClick = {
                        onItemClick(item.route)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(item.iconResId),
                            contentDescription = stringResource(item.labelResId),
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = colors.selection,
                        selectedTextColor = colors.textOnSelection,
                        selectedIconColor = colors.textOnSelection,
                        unselectedContainerColor = colors.surface,
                        unselectedTextColor = colors.textPrimary,
                        unselectedIconColor = colors.textSecondary
                    ),
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 2.dp
                    )
                )
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            HorizontalDivider(
                color = colors.border
            )

            NavigationDrawerItem(
                label = {
                    Text(
                        text = stringResource(R.string.sign_out)
                    )
                },
                selected = false,
                onClick = onSignOutClick,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.back_icon),
                        contentDescription = stringResource(R.string.sign_out),
                        modifier = Modifier.size(24.dp)
                    )
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = colors.surface,
                    unselectedTextColor = colors.textPrimary,
                    unselectedIconColor = colors.textSecondary
                ),
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                )
            )
        }
    }
}