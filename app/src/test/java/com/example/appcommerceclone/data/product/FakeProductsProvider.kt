package com.example.appcommerceclone.data.product

import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.util.Constants

class FakeProductsProvider : ProductsProvider {

    private val products = listOf(productJewelery, productElectronic, productMensClothing, productWomensClothing)


    override suspend fun getProducts(): List<Product> = products


    companion object {
        val productJewelery = Product(
            id = 1,
            name = "Jewelery",
            price = 5.0,
            description = "Jewelery Desc",
            category = Constants.CATEGORY_NAME_JEWELRY
        )
        val productElectronic = Product(
            id = 2,
            name = "Electronics",
            price = 10.0,
            description = "Electronic Desc",
            category = Constants.CATEGORY_NAME_ELECTRONICS
        )
        val productMensClothing = Product(
            id = 3,
            name = "MensClothing",
            price = 15.0,
            description = "MensClothing Desc",
            category = Constants.CATEGORY_NAME_MENS_CLOTHING
        )
        val productWomensClothing = Product(
            id = 4,
            name = "WomensClothing Desc",
            price = 20.0,
            description = "WomensClothing",
            category = Constants.CATEGORY_NAME_WOMENS_CLOTHING
        )
    }
}