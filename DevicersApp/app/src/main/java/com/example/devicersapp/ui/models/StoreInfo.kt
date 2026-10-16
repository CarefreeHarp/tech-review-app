package com.example.devicersapp.ui.models
/** Datos del catálogo independiente de tiendas. */
data class StoreInfo(val id: Int, val name: String, val websiteUrl: String?)
/** Copia de la tienda y enlace particular del artículo; puede diferir del catálogo. */
data class ArticleStoreInfo(val storeId: Int, val name: String?, val websiteUrl: String?, val productUrl: String?)
