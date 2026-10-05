package com.example.devicersapp.data.datasource

import com.example.devicersapp.data.dto.ReviewBookmarkDto

/** Contrato remoto exclusivo de reseñas guardadas; propaga los errores de consulta. */
interface ReviewBookmarkRemoteDataSource {
    suspend fun getReviewBookmarks(): List<ReviewBookmarkDto>
}
