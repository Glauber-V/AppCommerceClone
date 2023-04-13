package com.example.appcommerceclone.model.order

import com.squareup.moshi.Json
import java.text.NumberFormat

data class Order(
    @Json(name = "id") var id: Int = 0,
    @Json(name = "userId") var userId: Int = 0,
    @Json(name = "date") var date: String = "",
    @Json(name = "products") var orderedProducts: MutableList<OrderedProduct> = mutableListOf(),
    @Json(ignore = true) var total: Double = 0.0
) {

    fun getFormattedPrice(): String =
        NumberFormat.getCurrencyInstance().format(orderedProducts.sumOf { it.product.price * it.quantity })
}