package com.example.appcommerceclone.model.order

import com.example.appcommerceclone.model.product.Product
import com.squareup.moshi.Json

data class OrderedProduct(
    @Json(name = "productId") var id: Int = 0,
    @Json(name = "quantity") var quantity: Int = 1,
    @Json(ignore = true) var product: Product = Product()
) {

    constructor(product: Product) : this(product.id, 1, product)
    constructor(product: Product, quantity: Int) : this(product.id, quantity, product)
}