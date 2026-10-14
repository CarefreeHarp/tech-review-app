package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.ProductRemoteDataSource
import com.example.devicersapp.data.dto.ProductDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.gson.JsonParser

class ProductFirestoreDataSourceImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProductRemoteDataSource {

    override suspend fun getProducts(): List<ProductDto> {

        return firestore
            .collection("articles")
            .get()
            .await()
            .documents
            .map { document ->

                mapProduct(document)
            }
    }


    override suspend fun getProductById(productId: Int): ProductDto {

        val document = firestore
            .collection("articles")
            .whereEqualTo("id", productId)
            .get()
            .await()
            .documents
            .firstOrNull()
            ?: throw IllegalStateException(
                "No existe el producto $productId"
            )

        return mapProduct(document)
    }


    private fun mapProduct(
        document: com.google.firebase.firestore.DocumentSnapshot
    ): ProductDto {

        return ProductDto(

            id = document.getLong("id")?.toInt()
                ?: throw IllegalStateException("Producto sin id"),

            category_id = document.getLong("category_id")?.toInt()
                ?: throw IllegalStateException("Producto sin category_id"),

            brand_id = document.getLong("brand_id")?.toInt()
                ?: throw IllegalStateException("Producto sin brand_id"),

            name = document.getString("name")
                ?: "",

            model = document.getString("model"),

            description = document.getString("description"),

            image_url = document.getString("image_url"),

            release_date = null,

            specifications = document.get("specifications")?.let {
                Gson().toJsonTree(it)
            },

            is_active = document.getBoolean("is_active")
                ?: true,

            createdAt = document.getTimestamp("createdAt")
                ?.toDate()
                ?.toInstant()
                ?.toString()
                ?: "",

            updatedAt = document.getTimestamp("updatedAt")
                ?.toDate()
                ?.toInstant()
                ?.toString()
                ?: ""
        )
    }
}