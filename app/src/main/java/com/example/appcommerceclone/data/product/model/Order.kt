package com.example.appcommerceclone.data.product.model

import com.squareup.moshi.Json
import java.text.NumberFormat

data class Order(
    @Json(name = "id") val id: Int = 0,
    @Json(name = "userId") val userId: Int = 0,
    @Json(name = "date") val date: String = "",
    @Json(name = "products") val orderedProducts: List<OrderedProduct> = mutableListOf(),
    @Json(ignore = true) val total: Double = 0.0
) {

    fun getFormattedPrice(): String =
        NumberFormat.getCurrencyInstance().format(orderedProducts.sumOf { it.product.price * it.quantity })

    fun getOrderedProductListAsString(): String {
        val namesList = orderedProducts.map { it.product.name }
        return namesList.joinToString()
    }
}