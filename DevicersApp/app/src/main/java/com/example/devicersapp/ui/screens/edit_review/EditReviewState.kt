package com.example.devicersapp.ui.screens.edit_review

import androidx.annotation.StringRes
import com.example.devicersapp.ui.models.ProductContent

/** Estado completo del formulario que modifica una reseña existente. */
data class EditReviewState(
    val reviewId: Int? = null,
    val product: ProductContent? = null,
    val rating: Int = 0,
    val title: String = "",
    val experience: String = "",
    val advantage: String = "",
    val disadvantage: String = "",
    val loading: Boolean = false,
    val saving: Boolean = false,
    val saved: Boolean = false,
    val canEdit: Boolean = false,
    @param:StringRes val errorResId: Int? = null
)
