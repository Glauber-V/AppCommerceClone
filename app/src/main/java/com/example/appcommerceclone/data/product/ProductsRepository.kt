package com.example.appcommerceclone.data.product

import com.example.appcommerceclone.data.product.model.Product

interface ProductsRepository {

    suspend fun loadProductsList(): List<Product>
}