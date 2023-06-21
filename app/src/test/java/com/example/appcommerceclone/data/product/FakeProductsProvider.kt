package com.example.appcommerceclone.data.product

import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.util.productsList

class FakeProductsProvider : ProductsProvider {

    override suspend fun getProducts(): List<Product> = productsList
}