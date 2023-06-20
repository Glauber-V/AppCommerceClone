package com.example.appcommerceclone.util

import com.example.appcommerceclone.model.product.Product

val baseExampleProduct = Product(
    description = "Product crafted by specialists in the industry. Satisfaction guaranteed. " +
            "Return or exchange any order within 30 days. Designed by Product Factory"
)

val exampleProductJewelry = baseExampleProduct.copy(
    id = 10,
    name = "Solid Gold Petite Micro",
    price = 168.00,
    category = Constants.CATEGORY_NAME_JEWELRY,
)

val exampleProductElectronic = baseExampleProduct.copy(
    id = 20,
    name = "Sandisk SSD Plus ITB - SATA III 6Gb/s",
    price = 109.00,
    category = Constants.CATEGORY_NAME_ELECTRONICS,
)

val exampleProductMensClothing = baseExampleProduct.copy(
    id = 30,
    name = "Casual Premium Slim",
    price = 22.30,
    category = Constants.CATEGORY_NAME_MENS_CLOTHING,
)

val exampleProductWomensClothing = baseExampleProduct.copy(
    id = 40,
    name = "Short Sleeve Moisture",
    price = 7.95,
    category = Constants.CATEGORY_NAME_WOMENS_CLOTHING,
)

val exampleProductsList = listOf(
    exampleProductJewelry,
    exampleProductElectronic,
    exampleProductMensClothing,
    exampleProductWomensClothing
)