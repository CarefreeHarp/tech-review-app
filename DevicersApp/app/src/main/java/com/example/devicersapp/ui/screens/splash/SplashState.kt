package com.example.devicersapp.ui.screens.splash

enum class SplashDestination { HOME, LOGIN, COMPLETE_GOOGLE_PROFILE }

/** Representa el resultado observable de comprobar la sesión y su perfil. */
data class SplashState(
    val destination: SplashDestination? = null
)
