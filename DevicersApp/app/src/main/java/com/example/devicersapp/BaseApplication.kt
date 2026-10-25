package com.example.devicersapp

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import dagger.hilt.android.HiltAndroidApp

/** Inicializa Firebase y conecta los SDK a los emuladores locales durante la depuración. */
@HiltAndroidApp
class BaseApplication : Application() {

    /** Configura los destinos antes de que las pantallas y los repositorios utilicen Firebase. */
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        if (BuildConfig.DEBUG) {
            // En Android Emulator, esta dirección permite acceder al computador anfitrión.
            val emulatorHost = "10.0.2.2"
            Firebase.firestore.useEmulator(emulatorHost, 8080)
            Firebase.auth.useEmulator(emulatorHost, 9099)
            Firebase.storage.useEmulator(emulatorHost, 9199)
            // Las Functions se activan desde Firestore local; FCM conserva su servicio real.
        }
    }
}
