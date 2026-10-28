package com.example.devicersapp.data.config

/** Selecciona la fuente de información general; Firebase Auth y Storage se mantienen en ambos modos. */
object BackendConfig {
    /** Usa Firestore cuando es true y Express mediante Retrofit cuando es false; requiere recompilar. */
    const val USE_FIRESTORE = true
}
