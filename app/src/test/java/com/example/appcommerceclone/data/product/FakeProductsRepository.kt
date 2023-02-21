package com.example.appcommerceclone.data.product

import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_ELECTRONICS
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_JEWELRY
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_MENS_CLOTHING
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_WOMENS_CLOTHING

class FakeProductsRepository : ProductsRepository {

    private val products = mutableListOf<Product>()

    override suspend fun loadProductsList(): List<Product> {
        products.apply {
            add(0, productJewelery)
            add(1, productElectronic)
            add(2, productMensClothing)
            add(3, productWomensClothing)
        }

        return products
    }


    companion object {
        val productJewelery = Product(id = 1, name = "Jewelery", price = 5.0, category = CATEGORY_NAME_JEWELRY)
        val productElectronic = Product(id = 2, name = "Electronics", price = 10.0, category = CATEGORY_NAME_ELECTRONICS)
        val productMensClothing = Product(id = 3, name = "MensClothing", price = 15.0, category = CATEGORY_NAME_MENS_CLOTHING)
        val productWomensClothing = Product(id = 4, name = "WomensClothing", price = 20.0, category = CATEGORY_NAME_WOMENS_CLOTHING)
    }
}