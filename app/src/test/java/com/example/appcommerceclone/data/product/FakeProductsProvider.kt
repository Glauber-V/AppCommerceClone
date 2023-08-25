package com.example.appcommerceclone.data.product

import com.example.appcommerceclone.data.product.model.Product
import com.example.appcommerceclone.util.productList

class FakeProductsProvider : ProductsProvider {

    override suspend fun getProducts(): List<Product> = productList
}