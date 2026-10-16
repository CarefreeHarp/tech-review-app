package com.example.devicersapp.data.datasource
import com.example.devicersapp.ui.models.*
/** Operaciones de likes de comentarios; devuelve modelos comunes y propaga los errores de la fuente seleccionada. */
interface CommentLikeRemoteDataSource {
    suspend fun getCommentLikes(reviewId: Int? = null, commentId: Int? = null): List<CommentLikeInfo>
}
