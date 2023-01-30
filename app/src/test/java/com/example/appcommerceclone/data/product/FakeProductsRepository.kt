package com.example.appcommerceclone.data.product

import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_ELECTRONICS
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_JEWELRY
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_MENS_CLOTHING
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_WOMENS_CLOTHING

class FakeProductsRepository : ProductsRepository {

    private val products = mutableListOf<Product>()


    override suspend fun loadProductsList(): List<Product> {
        val product1 = Product(id = 1, price = 5.0, category = CATEGORY_NAME_JEWELRY)
        val product2 = Product(id = 2, price = 10.0, category = CATEGORY_NAME_ELECTRONICS)
        val product3 = Product(id = 3, price = 15.0, category = CATEGORY_NAME_MENS_CLOTHING)
        val product4 = Product(id = 4, price = 20.0, category = CATEGORY_NAME_WOMENS_CLOTHING)

        products.add(0, product1)
        products.add(1, product2)
        products.add(2, product3)
        products.add(3, product4)

        return products
    }
}