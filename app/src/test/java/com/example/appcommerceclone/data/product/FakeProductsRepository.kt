package com.example.appcommerceclone.data.product

import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.model.product.Product
import kotlinx.coroutines.withContext

class FakeProductsRepository(
    private val productsProvider: ProductsProvider,
    private val dispatcherProvider: DispatcherProvider
) : ProductsRepository {

    override suspend fun loadProductsList(): List<Product> {
        return withContext(dispatcherProvider.default) {
            productsProvider.getProducts()
        }
    }
}