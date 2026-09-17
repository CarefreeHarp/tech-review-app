package com.example.devicersapp.data.repository

import android.net.Uri
import com.example.devicersapp.data.datasource.AuthRemoteDataSource
import com.example.devicersapp.data.datasource.StorageRemoteDataSource
import javax.inject.Inject

class StorageRepository @Inject constructor(
    private val storage: StorageRemoteDataSource,
    private val auth: AuthRemoteDataSource
    ){

    //subir la imagen de perfil del usuario
    suspend fun uploadProfileImage(uri: Uri): Result<String>{
        return try{
            val userId = auth.currentUser?.uid ?: return Result.failure(Exception("Usuario no encontrado"))

            val path = "profileImages/$userId.jpg"
            val url = storage.uploadImage(path, uri)

            //Actualizar url del usuario
            auth.updateProfileImage(url)

            Result.success(url)
        } catch (e: Exception){
            Result.failure(Exception("No fue posbile subir la imagen"))
        }
    }
}