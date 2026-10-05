package com.example.devicersapp.ui.models

/** Datos de categoría independientes del contrato JSON del backend. */
data class CategoryInfo(
    val id: Int,
    val parentCategoryId: Int?,
    val name: String,
    val description: String?,
)
