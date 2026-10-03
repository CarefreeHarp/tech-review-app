package com.example.devicersapp.data.repository

import android.net.Uri
import com.example.devicersapp.data.datasource.StorageRemoteDataSource
import com.google.firebase.storage.StorageException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Centraliza las operaciones de almacenamiento que consume la aplicación.
 *
 * El DataSource realiza las peticiones a Firebase Storage y este Repository
 * transforma sus resultados y errores en Result para las capas superiores.
 */
@Singleton
class StorageRepository @Inject constructor(
    private val storage: StorageRemoteDataSource,
    private val auth: AuthRepository
) {

    /**
     * Sube una nueva foto de perfil para el usuario autenticado.
     *
     * Si la carga es exitosa, actualiza también la URL de la foto
     * asociada al usuario en Firebase Authentication.
     */
    suspend fun uploadProfileImage(uri: Uri): Result<String> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("Debes iniciar sesión para cambiar tu foto de perfil.")
                )

            val path = "profileImages/$userId.jpg"
            val url = storage.uploadImage(path, uri)

            auth.updateProfileImage(url).fold(
                onSuccess = { Result.success(url) },
                onFailure = { exception -> Result.failure(exception) }
            )
        } catch (e: StorageException) {
            Result.failure(
                Exception(getStorageErrorMessage(e), e)
            )
        } catch (e: SecurityException) {
            Result.failure(
                Exception(
                    "No se tienen permisos para acceder a la imagen seleccionada.",
                    e
                )
            )
        } catch (e: Exception) {
            Result.failure(
                Exception(
                    "No fue posible actualizar la foto de perfil. Inténtalo nuevamente.",
                    e
                )
            )
        }
    }

    /**
     * Convierte los códigos técnicos de Firebase Storage
     * en mensajes comprensibles para el usuario.
     */
    private fun getStorageErrorMessage(exception: StorageException): String {
        return when (exception.errorCode) {
            StorageException.ERROR_OBJECT_NOT_FOUND ->
                "No se encontró el archivo solicitado."

            StorageException.ERROR_BUCKET_NOT_FOUND ->
                "El almacenamiento de la aplicación no está disponible."

            StorageException.ERROR_PROJECT_NOT_FOUND ->
                "No se encontró la configuración de almacenamiento del proyecto."

            StorageException.ERROR_NOT_AUTHENTICATED ->
                "Debes iniciar sesión para subir una foto de perfil."

            StorageException.ERROR_NOT_AUTHORIZED ->
                "No tienes permiso para subir esta imagen."

            StorageException.ERROR_QUOTA_EXCEEDED ->
                "Se alcanzó el límite de almacenamiento disponible."

            StorageException.ERROR_RETRY_LIMIT_EXCEEDED ->
                "La operación tardó demasiado. Revisa tu conexión e inténtalo nuevamente."

            StorageException.ERROR_CANCELED ->
                "La carga de la imagen fue cancelada."

            StorageException.ERROR_INVALID_CHECKSUM ->
                "La imagen no pudo verificarse correctamente. Selecciónala nuevamente."

            else ->
                "Ocurrió un error al subir la imagen. Inténtalo nuevamente."
        }
    }
}
