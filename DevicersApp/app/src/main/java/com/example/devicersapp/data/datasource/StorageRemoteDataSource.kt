package com.example.devicersapp.data.datasource;

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage;
import kotlinx.coroutines.tasks.await

import javax.inject.Inject;

class StorageRemoteDataSource @Inject constructor(
    private val storage:FirebaseStorage){

    //Guardar cualquier tipo de imagen dentro de firebase Storage
    suspend fun uploadImage(path: String, uri: Uri): String{
        val imageRef = storage.reference.child(path)
        imageRef.putFile(uri).await()
        return imageRef.downloadUrl.await().toString()
    }
}
