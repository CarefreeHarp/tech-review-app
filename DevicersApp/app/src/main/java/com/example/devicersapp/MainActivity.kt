package com.example.devicersapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.devicersapp.ui.screens.login.LoginScreen


/** Android entry activity that displays the frontend login prototype. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Login is the initial static screen until navigation is implemented.
            LoginScreen()
        }
    }
}
