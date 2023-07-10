package com.example.appcommerceclone.data.product

import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.product.model.Product
import kotlinx.coroutines.withContext

class DefaultProductsRepository(
    private val productsProvider: ProductsProvider,
    private val dispatcherProvider: DispatcherProvider
) : ProductsRepository {

    override suspend fun loadProductsList(): List<Product> {
        return runCatching {
            withContext(dispatcherProvider.default) {
                productsProvider.getProducts()
            }
        }.getOrElse {
            emptyList()
        }
    }
}