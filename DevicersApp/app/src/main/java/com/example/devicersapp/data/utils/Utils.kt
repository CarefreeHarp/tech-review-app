package com.example.devicersapp.data.utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
/** Consulta la sesión mantenida por Firebase Auth sin guardar otra identidad en memoria. */
object Utils {
    /** Devuelve el UID de Auth; no es el ID numérico de los perfiles existentes. */
    fun getCurrentUserId(): String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    /** Obtiene directamente la cuenta autenticada o null cuando Firebase no tiene sesión. */
    fun getCurrentUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser
}
