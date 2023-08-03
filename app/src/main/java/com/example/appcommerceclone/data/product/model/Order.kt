package com.example.appcommerceclone.data.product.model

data class Order(
    val id: Int = 0,
    val userId: Int = 0,
    val date: String = "",
    val orderedProducts: List<OrderedProduct> = listOf()
)