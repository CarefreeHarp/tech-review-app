package com.example.devicersapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.devicersapp.ui.screens.create_review.CreateReviewScreen
import com.example.devicersapp.ui.screens.feed.FeedScreen
import com.example.devicersapp.ui.screens.login.LoginScreen
import com.example.devicersapp.ui.screens.notifications.NotificationsScreen
import com.example.devicersapp.ui.screens.product.ProductScreen
import com.example.devicersapp.ui.screens.profile.ProfileScreen
import com.example.devicersapp.ui.screens.rateProduct.RateProductScreen
import com.example.devicersapp.ui.screens.register.RegisterScreen
import com.example.devicersapp.ui.screens.review.ReviewScreen
import com.example.devicersapp.ui.screens.search.SearchScreen
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DevicersAppTheme {
                // =========================
                // PANTALLAS SIN BOTTOM BAR
                // =========================

                 ScreenWithScaffold(selectedItem = "login", topBarNumber = 5) {
                     LoginScreen(modifier = it)
                 }
                
                /*
                 ScreenWithScaffold(selectedItem = "register", topBarNumber = 5) {
                     RegisterScreen(modifier = it)
                 }
                 */
                /*
                 ScreenWithScaffold(selectedItem = "review", topBarNumber = 4) {
                     ReviewScreen(modifier = it)
                 }
                */
                // =========================
                // PANTALLAS CON BOTTOM BAR
                // =========================
                /*
                ScreenWithScaffold(selectedItem = "home", showBottomBar = true) {
                     FeedScreen(modifier = it)
                 }
                */
                /*
                 ScreenWithScaffold(selectedItem = "search", showBottomBar = true) {
                     SearchScreen(modifier = it)
                 }
                */
                /*
                 ScreenWithScaffold(selectedItem = "add", showBottomBar = true, topBarNumber = 2) {
                     RateProductScreen(modifier = it)
                 }
                */
                /*
                 ScreenWithScaffold(selectedItem = "favorite", showBottomBar = true) {
                     NotificationsScreen(modifier = it)
                 }
                */
                /*
                 ScreenWithScaffold(selectedItem = "profile", showBottomBar = true, topBarNumber = 1) {
                     ProfileScreen(modifier = it)
                 }
                */
                /*
                ScreenWithScaffold(selectedItem = "search", showBottomBar = true, topBarNumber = 1) {
                   ProductScreen(modifier = it)
                }
                */
                /*
                 ScreenWithScaffold(selectedItem = "add", showBottomBar = true, topBarNumber = 3) {
                     CreateReviewScreen(modifier = it)
                 }

                 */
            }
        }
    }
}
/**
 * Envuelve una pantalla con el Scaffold general de Devicers.
 * @param selectedItem Elemento que debe aparecer seleccionado en la barra de navegación.
 * @param showBottomBar Indica si se debe mostrar la barra de navegación inferior.
 * @param topBarNumber Número de la barra superior que se debe mostrar, o `null` si no corresponde.
 * @param content Pantalla que se mostrará dentro del Scaffold.
 * @param modifier Modificador aplicado al Scaffold compartido.
 * @param onNavigationItemClick Acción solicitada al seleccionar un elemento de navegación.
 * @param onTopBarBackClick Acción solicitada al seleccionar volver en una barra superior interactiva.
 */
@Composable
fun ScreenWithScaffold(
    selectedItem: String,
    showBottomBar: Boolean = false,
    topBarNumber: Int? = null,
    modifier: Modifier = Modifier,
    onNavigationItemClick: (String) -> Unit = {},
    onTopBarBackClick: () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    DevicersScaffold(
        selectedItem = selectedItem,
        showBottomBar = showBottomBar,
        topBarNumber = topBarNumber,
        modifier = modifier,
        onNavigationItemClick = onNavigationItemClick,
        onTopBarBackClick = onTopBarBackClick
    ) { innerPadding ->
        // El contenido recibe los insets del Scaffold para no invadir las barras del sistema.
        content(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
