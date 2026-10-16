package com.example.devicersapp.data.datasource
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
/** Almacena archivos y devuelve su URL pública para guardarla en cualquiera de las bases. */
class StorageRemoteDataSource @Inject constructor(private val storage: FirebaseStorage) {
    /** El llamador elige la ruta; la operación conserva las excepciones originales del SDK. */
    suspend fun uploadImage(path: String, uri: Uri): String {
        val reference = storage.reference.child(path)
        reference.putFile(uri).await()
        return reference.downloadUrl.await().toString()
    }
}
