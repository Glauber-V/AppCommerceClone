package com.example.appcommerceclone.data.product.model

data class OrderedProduct(
    val id: Int = 0,
    var quantity: Int = 1,
    val product: Product = Product()
) {

    constructor(product: Product) : this(product.id, 1, product)
    constructor(product: Product, quantity: Int) : this(product.id, quantity, product)
}