package com.example.appcommerceclone.data.product

import com.example.appcommerceclone.model.product.Product
import retrofit2.http.GET

interface ProductsProvider {

    @GET("products")
    suspend fun getProducts(): List<Product>
}