package com.example.appcommerceclone.data.product

import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.model.product.Product
import kotlinx.coroutines.withContext

class DefaultProductsRepository(
    private val productsProvider: ProductsProvider,
    private val dispatcher: DispatcherProvider
) : ProductsRepository {

    override suspend fun loadProductsList(): List<Product> {
        return runCatching {
            withContext(dispatcher.io) {
                productsProvider.getProducts()
            }
        }.getOrElse {
            emptyList()
        }
    }
}