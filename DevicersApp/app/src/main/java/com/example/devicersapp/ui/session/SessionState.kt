package com.example.devicersapp.ui.session

import androidx.compose.runtime.staticCompositionLocalOf
import com.example.devicersapp.ui.models.UserInfo

/** Comparte la identidad observable de la sesión con los elementos que representan usuarios. */
val LocalSessionState = staticCompositionLocalOf { SessionState() }

/** Presenta un nombre de usuario con un único prefijo @ y omite los valores vacíos. */
fun String.asUserHandle(): String {
    val username = trim().trimStart('@').trim()
    return if (username.isBlank()) "" else "@$username"
}

/** Representa la información de sesión necesaria para los elementos globales de la interfaz. */
data class SessionState(
    val currentProfileHandle: String = "",
    val profileImageUrl: String? = null,
    val firebaseUid: String? = null,
    val userId: Int? = null,
    val profile: UserInfo? = null
) {
    /** Resuelve la identidad de sesión cuando corresponde y presenta todo usuario con @. */
    fun userNameFor(userId: Int?, fallback: String): String {
        val username = if (isCurrentUser(userId) && currentProfileHandle.isNotBlank()) {
            currentProfileHandle
        } else {
            fallback
        }
        return username.asUserHandle()
    }

    /** Usa exclusivamente la foto de sesión para el usuario actual, incluso cuando no tiene foto. */
    fun userImageFor(userId: Int?, fallback: String?): String? =
        if (isCurrentUser(userId)) profileImageUrl else fallback

    /** Comprueba la identidad únicamente contra el perfil real de la sesión. */
    fun isCurrentUser(candidateId: Int?): Boolean =
        candidateId != null && candidateId == userId
}
