package com.example.devicersapp.data.datasource
import com.example.devicersapp.ui.models.*
/** Operaciones de comentarios; devuelve modelos comunes y propaga los errores de la fuente seleccionada. */
interface CommentRemoteDataSource {
    suspend fun getComments(reviewId: Int? = null): List<CommentInfo>
    /** Publica el comentario con el nombre y la foto actuales del autor y devuelve el comentario guardado. */
    suspend fun createComment(draft: CommentDraft): CommentInfo
}
