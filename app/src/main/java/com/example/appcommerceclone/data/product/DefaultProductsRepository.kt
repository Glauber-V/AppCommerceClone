package com.example.appcommerceclone.data.product

import com.example.appcommerceclone.data.product.model.Product

class DefaultProductsRepository(private val productsProvider: ProductsProvider) : ProductsRepository {

    override suspend fun loadProductsList(): List<Product> {
        return productsProvider.getProducts()
    }
}