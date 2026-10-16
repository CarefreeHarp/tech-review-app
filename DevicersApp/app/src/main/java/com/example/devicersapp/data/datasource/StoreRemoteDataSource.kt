package com.example.devicersapp.data.datasource
import com.example.devicersapp.ui.models.*
/** Operaciones de tiendas; devuelve modelos comunes y propaga los errores de la fuente seleccionada. */
interface StoreRemoteDataSource {
    suspend fun getStores(): List<StoreInfo>
}
