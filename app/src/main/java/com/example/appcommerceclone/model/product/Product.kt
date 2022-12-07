package com.example.appcommerceclone.model.product

import com.squareup.moshi.Json

data class Product(
    @Json(name = "id") val id: Int = 0,
    @Json(name = "title") val name: String = "",
    @Json(name = "price") val price: Double = 0.0,
    @Json(name = "description") val description: String = "",
    @Json(name = "category") val category: String = "",
    @Json(name = "image") val imageUrl: String = ""
)