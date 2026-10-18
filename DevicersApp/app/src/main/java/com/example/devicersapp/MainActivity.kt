package com.example.devicersapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint

/** Inicia la interfaz principal de Devicers y solicita el permiso para mostrar notificaciones push. */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // Lanzador del diálogo del sistema en el que el usuario acepta o deniega las notificaciones.
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.d(TAG, if (isGranted) "Notificaciones aceptadas" else "Notificaciones denegadas")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Solo se pregunta al abrir la app, no al recrear la actividad por un cambio de configuración.
        if (savedInstanceState == null) requestNotificationPermission()
        setContent { DevicersApp() }
    }

    /** Android 13 (Tiramisu) o superior exige pedir en tiempo de ejecución el permiso de notificaciones. */
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        val isPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        // Si aún no fue concedido, el sistema muestra el popup de aceptar o denegar.
        if (!isPermissionGranted) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private companion object {
        const val TAG = "Permissions"
    }
}
