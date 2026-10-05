package com.example.devicersapp.data.dto

import com.example.devicersapp.ui.models.CategoryInfo

/** Categoría de un artículo tal como la devuelve el endpoint `categories`. */
data class CategoryDto(
    val id: Int,
    val parent_category_id: Int?,
    val name: String,
    val description: String?,
)

/** Traduce los campos de la API al modelo de categoría del front. */
fun CategoryDto.toCategoryInfo(): CategoryInfo = CategoryInfo(
    id = id,
    parentCategoryId = parent_category_id,
    name = name,
    description = description,
)

/** Adapta la categoría consultada al chip visible, conservando su jerarquía. */
fun CategoryInfo.toCategoryContent() = com.example.devicersapp.ui.models.ProductCategoryContent(
    id = id.toString(),
    labelResId = com.example.devicersapp.R.string.all,
    label = name,
    parentCategoryId = parentCategoryId?.toString()
)
