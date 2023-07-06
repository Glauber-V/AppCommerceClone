package com.example.appcommerceclone.data.product

import com.example.appcommerceclone.data.model.product.Product

interface ProductsRepository {

    suspend fun loadProductsList(): List<Product>
}