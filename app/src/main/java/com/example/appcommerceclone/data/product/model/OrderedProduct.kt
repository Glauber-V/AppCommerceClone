package com.example.appcommerceclone.data.product.model

import com.squareup.moshi.Json
import java.text.NumberFormat

data class OrderedProduct(
    @Json(name = "productId") val id: Int = 0,
    @Json(name = "quantity") var quantity: Int = 1,
    @Json(ignore = true) val product: Product = Product()
) {

    constructor(product: Product) : this(product.id, 1, product)
    constructor(product: Product, quantity: Int) : this(product.id, quantity, product)

    fun getFormattedPrice(): String = NumberFormat.getCurrencyInstance().format(product.price * quantity)
}