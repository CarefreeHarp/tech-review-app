package com.example.devicersapp.data.dto

import com.example.devicersapp.ui.models.BrandInfo

/** Marca de un artículo tal como la devuelve el endpoint `brands`. */
data class BrandDto(
    val id: Int,
    val name: String,
)

/** Traduce los campos de la API al modelo de marca del front. */
fun BrandDto.toBrandInfo(): BrandInfo = BrandInfo(
    id = id,
    name = name,
)
