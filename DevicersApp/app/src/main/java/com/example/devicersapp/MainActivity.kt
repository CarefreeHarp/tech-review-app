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
import com.example.devicersapp.ui.screens.found_products.FoundProductsScreen
import com.example.devicersapp.ui.screens.home.HomeScreen
import com.example.devicersapp.ui.screens.own_profile.OwnProfileScreen
import com.example.devicersapp.ui.screens.profile_saved_reviews.ProfileSavedReviewsScreen
import com.example.devicersapp.ui.screens.profile_search_results.ProfileSearchResultsScreen
import com.example.devicersapp.ui.screens.search_profile.SearchProfileScreen
import com.example.devicersapp.ui.screens.access.AccessScreen
import com.example.devicersapp.ui.screens.activity.ActivityScreen
import com.example.devicersapp.ui.screens.product.ProductScreen
import com.example.devicersapp.ui.screens.profile.ProfileScreen
import com.example.devicersapp.ui.screens.rate_product.RateProductScreen
import com.example.devicersapp.ui.screens.register.RegisterScreen
import com.example.devicersapp.ui.screens.request_product.RequestProductScreen
import com.example.devicersapp.ui.screens.review.ReviewScreen
import com.example.devicersapp.ui.screens.search_product.SearchProductScreen
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
                 /*
                 ScreenWithScaffold(selectedItem = "access", topBarNumber = 5) {
                     AccessScreen(modifier = it)
                 }
                */

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
                ScreenWithScaffold(selectedItem = "home", showBottomBar = true, topBarNumber = 5) {
                     HomeScreen(modifier = it)
                 }
                */
                /*
                 ScreenWithScaffold(selectedItem = "search", showBottomBar = true) {
                     SearchProductScreen(modifier = it)
                 }
                */
                /*
                 ScreenWithScaffold(selectedItem = "create", topBarNumber = 2) {
                     RateProductScreen(modifier = it)
                 }
                */
                /*
                 ScreenWithScaffold(selectedItem = "activity", showBottomBar = true) {
                     ActivityScreen(modifier = it)
                 }
                */

                /*
                 ScreenWithScaffold(selectedItem = "profile", showBottomBar = true, topBarNumber = 1) {
                     ProfileScreen(modifier = it)
                 }
                */

                /*
                 ScreenWithScaffold(selectedItem = "profile", showBottomBar = true, topBarNumber = 1) {
                     OwnProfileScreen(modifier = it)
                 }
                */
                /*
                 ScreenWithScaffold(selectedItem = "profile", showBottomBar = true, topBarNumber = 1) {
                     ProfileSavedReviewsScreen(modifier = it)
                 }
                */

                /*
                 ScreenWithScaffold(selectedItem = "search", showBottomBar = true) {
                     SearchProfileScreen(modifier = it)
                 }
                */

                /*
                 ScreenWithScaffold(selectedItem = "search", showBottomBar = true, topBarNumber = 7) {
                     ProfileSearchResultsScreen(modifier = it)
                 }
                */

                /*
                 ScreenWithScaffold(selectedItem = "search", showBottomBar = true, topBarNumber = 8) {
                     FoundProductsScreen(modifier = it)
                 }
                */
                /*
                 ScreenWithScaffold(selectedItem = "create", showBottomBar = true, topBarNumber = 9) {
                     RequestProductScreen(modifier = it)
                 }
                */
                /*
                ScreenWithScaffold(selectedItem = "search", showBottomBar = true, topBarNumber = 6) {
                   ProductScreen(modifier = it)
                }
                */
                /*
                 ScreenWithScaffold(selectedItem = "create", showBottomBar = true, topBarNumber = 3) {
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
