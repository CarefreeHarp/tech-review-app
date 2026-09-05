package com.example.devicersapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/** Inicializa el contenedor raíz de dependencias administrado por Hilt. */
@HiltAndroidApp
class BaseApplication : Application()
