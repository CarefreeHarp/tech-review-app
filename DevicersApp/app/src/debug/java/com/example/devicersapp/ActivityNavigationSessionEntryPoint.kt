package com.example.devicersapp

import com.example.devicersapp.data.repository.SessionRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** Acceso de la prueba de navegación al contenedor real; solo presente en debug. */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface ActivityNavigationSessionEntryPoint {
    fun session(): SessionRepository
}
