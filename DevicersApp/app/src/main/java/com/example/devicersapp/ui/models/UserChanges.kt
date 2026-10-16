package com.example.devicersapp.ui.models
/** Datos iniciales del perfil, vinculados a la identidad de Firebase Auth. */
data class UserDraft(val firebaseUid: String, val email: String, val username: String, val profileImageUrl: String?)
/** Actualización parcial; null significa conservar el valor actual. */
data class UserChanges(val username: String? = null, val biography: String? = null, val profileImageUrl: String? = null)
/** Proyección pública sin rellenar artificialmente un perfil privado completo. */
data class UserSummary(val id: Int, val username: String, val biography: String?, val profileImageUrl: String?)
/** Proyecta únicamente los campos públicos que necesita una lista de usuarios. */
fun UserInfo.toUserSummary() = UserSummary(id, username, biography, profileImageUrl)
