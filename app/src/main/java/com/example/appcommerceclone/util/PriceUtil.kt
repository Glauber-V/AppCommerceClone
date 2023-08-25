package com.example.appcommerceclone.util

import com.example.appcommerceclone.data.product.model.Order
import com.example.appcommerceclone.data.product.model.OrderedProduct
import com.example.appcommerceclone.data.product.model.Product
import java.text.NumberFormat

fun Double.formatPrice(): String = NumberFormat.getCurrencyInstance().format(this)

fun Product.formatPrice(): String = price.formatPrice()

fun OrderedProduct.getPrice(): Double = product.price * quantity

fun OrderedProduct.formatPrice(): String = getPrice().formatPrice()

fun Collection<OrderedProduct>.getTotalPrice(): Double {
    return sumOf { orderedProduct: OrderedProduct ->
        orderedProduct.product.price * orderedProduct.quantity
    }
}

fun Order.getTotalPrice(): Double = orderedProducts.getTotalPrice()

fun Order.formatTotalPrice(): String = getTotalPrice().formatPrice()

fun Order.getProductsNamesAsString(): String {
    val nameList = orderedProducts.map { it.product.name }
    return nameList.joinToString()
}