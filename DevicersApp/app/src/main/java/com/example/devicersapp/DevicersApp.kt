package com.example.devicersapp

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.devicersapp.navigation.AppNavigation
import com.example.devicersapp.navigation.NavigationLogic
import com.example.devicersapp.navigation.navigateToDestination
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/**
 * Compone la aplicación Devicers con su tema global y su grafo de navegación principal.
 *
 * @param modifier Modificador aplicado al contenedor de navegación raíz.
 */
@Composable
fun DevicersApp(modifier: Modifier = Modifier) {
    DevicersAppTheme {
        val navController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()
        val configuration = NavigationLogic.configurationFor(backStackEntry?.destination?.route)

        DevicersScaffold(
            selectedItem = configuration.selectedItem,
            showBottomBar = configuration.showBottomBar,
            topBarNumber = configuration.topBarNumber,
            modifier = modifier,
            onNavigationItemClick = { route ->
                navController.navigateToDestination(route)
            },
            onTopBarBackClick = { navController.popBackStack() }
        ) { innerPadding ->
            AppNavigation(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
