package com.example.devicersapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.devicersapp.ui.screens.feed.FeedScreen
import com.example.devicersapp.ui.screens.login.LoginScreen
import com.example.devicersapp.ui.screens.register.RegisterScreen
import com.example.devicersapp.ui.screens.search.SearchScreen


/** Actividad de entrada que muestra el prototipo visual de inicio de sesión. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Inicio de sesión es la pantalla inicial estática hasta implementar navegación.
            SearchScreen()
        }
    }
}
