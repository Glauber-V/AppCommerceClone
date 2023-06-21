package com.example.appcommerceclone.util

import com.example.appcommerceclone.model.order.OrderedProduct
import com.example.appcommerceclone.model.product.Product

val baseExampleProduct = Product(
    description = "Product crafted by specialists in the industry. Satisfaction guaranteed. " +
            "Return or exchange any order within 30 days. Designed by Product Factory"
)

val productJewelry = baseExampleProduct.copy(
    id = 10,
    name = "Solid Gold Petite Micro",
    price = 168.00,
    category = Constants.CATEGORY_NAME_JEWELRY,
)

val productElectronic = baseExampleProduct.copy(
    id = 20,
    name = "Sandisk SSD Plus ITB - SATA III 6Gb/s",
    price = 109.00,
    category = Constants.CATEGORY_NAME_ELECTRONICS,
)

val productMensClothing = baseExampleProduct.copy(
    id = 30,
    name = "Casual Premium Slim",
    price = 22.30,
    category = Constants.CATEGORY_NAME_MENS_CLOTHING,
)

val productWomensClothing = baseExampleProduct.copy(
    id = 40,
    name = "Short Sleeve Moisture",
    price = 7.95,
    category = Constants.CATEGORY_NAME_WOMENS_CLOTHING,
)

val productsList = listOf(
    productJewelry,
    productElectronic,
    productMensClothing,
    productWomensClothing
)

val orderedProductJewelery = OrderedProduct(product = productJewelry, quantity = 1)
val orderedProductElectronic = OrderedProduct(product = productElectronic, quantity = 1)
val orderedProductMensClothing = OrderedProduct(product = productMensClothing, quantity = 1)
val orderedProductWomensClothing = OrderedProduct(product = productWomensClothing, quantity = 1)

val orderedProductList = listOf(
    orderedProductJewelery,
    orderedProductElectronic,
    orderedProductMensClothing,
    orderedProductWomensClothing
)