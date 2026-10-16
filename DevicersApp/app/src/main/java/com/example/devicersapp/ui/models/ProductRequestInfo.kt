package com.example.devicersapp.ui.models
/** Solicitud de producto; su identificador textual también es el ID del documento. */
data class ProductRequestInfo(val id: String, val userId: Int, val productName: String, val imageUrl: String?,
    val category: String?, val brand: String?, val releaseDate: String?, val status: String, val createdAt: String)
/** Contenido editable para crear una sugerencia, sin metadatos de Firebase Auth. */
data class ProductSuggestion(val productName: String, val category: String, val brand: String, val releaseDate: String,
    val imageUrl: String? = null)
