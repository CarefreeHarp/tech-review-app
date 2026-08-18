package com.example.devicersapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

                // LoginScreen()

                // RegisterScreen()

                // ReviewScreen()


                // =========================
                // PANTALLAS CON BOTTOM BAR
                // =========================

                // ScreenWithBottomBar("home") {
                //     FeedScreen(modifier = it)
                // }

                // ScreenWithBottomBar("search") {
                //     SearchScreen(modifier = it)
                // }

                  ScreenWithBottomBar("add") {
                      RateProductScreen(modifier = it)
                  }

                // ScreenWithBottomBar("favorite") {
                //     NotificationsScreen(modifier = it)
                // }

                // ScreenWithBottomBar("profile") {
                //     ProfileScreen(modifier = it)
                // }

                 //ScreenWithBottomBar("search") {
                 //    ProductScreen(modifier = it)
                 //}
            }
        }
    }
}


/**
 * Envuelve una pantalla con el Scaffold general de Devicers y muestra la barra de navegación inferior.
 * @param selectedItem Elemento que debe aparecer seleccionado en la barra de navegación.
 * @param content Pantalla que se mostrará dentro del Scaffold.
 */
@Composable
fun ScreenWithBottomBar(
    selectedItem: String,
    content: @Composable (Modifier) -> Unit
) {
    DevicersScaffold(
        selectedItem = selectedItem
    ) { innerPadding ->

        content(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
