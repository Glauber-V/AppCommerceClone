package com.example.appcommerceclone.data.model.product

import com.squareup.moshi.Json
import java.text.NumberFormat

data class Product(
    @Json(name = "id") val id: Int = 0,
    @Json(name = "title") val name: String = "",
    @Json(name = "price") val price: Double = 0.0,
    @Json(name = "description") val description: String = "",
    @Json(name = "category") val category: String = "",
    @Json(name = "image") val imageUrl: String = ""
) {

    fun getFormattedPrice(): String = NumberFormat.getCurrencyInstance().format(price)
}