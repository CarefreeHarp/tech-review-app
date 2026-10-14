package com.example.devicersapp.data.repository

import android.net.Uri
import com.example.devicersapp.data.datasource.StorageRemoteDataSource
import com.example.devicersapp.ui.models.UserInfo
import java.util.UUID
import javax.inject.Inject

/** Define la carga y el guardado del perfil de sesión sin aceptar IDs por navegación. */
interface EditProfileRepository {
    suspend fun getProfile(): UserInfo
    suspend fun saveProfile(username: String, biography: String, photo: Uri?)
}

/** Guarda los campos en Firestore y la foto seleccionada en Firebase Storage. */
class FirebaseEditProfileRepository @Inject constructor(
    private val session: SessionRepository,
    private val storage: StorageRemoteDataSource
) : EditProfileRepository {
    override suspend fun getProfile(): UserInfo = session.refreshCurrentProfile()

    override suspend fun saveProfile(username: String, biography: String, photo: Uri?) {
        val profile = session.requireCurrentProfile()
        val imageUrl = photo?.let {
            // Una ruta nueva conserva la foto publicada si falla el guardado del documento.
            storage.uploadImage("profileImages/${profile.firebaseUid}/${UUID.randomUUID()}.jpg", it)
        }
        session.updateProfile(username = username.trim(), profileImageUrl = imageUrl, biography = biography.trim())
    }
}
