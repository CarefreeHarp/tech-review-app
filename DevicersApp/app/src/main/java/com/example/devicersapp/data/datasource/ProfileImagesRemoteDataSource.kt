package com.example.devicersapp.data.datasource

/** Consulta las fotos vigentes de los perfiles necesarios para una pantalla. */
fun interface ProfileImagesRemoteDataSource {
    /** Incluye un valor nulo cuando el perfil solicitado no tiene foto o no existe. */
    suspend fun getImagesByIds(userIds: Set<Int>): Map<Int, String?>
}
