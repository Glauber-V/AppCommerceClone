package com.example.appcommerceclone.util

import com.example.appcommerceclone.model.order.OrderedProduct
import com.example.appcommerceclone.model.product.Product
import java.text.NumberFormat

/** Utility function to retrieve a [OrderedProduct] from a list using a [Product]. Make sure the list is not empty. */
fun MutableList<OrderedProduct>.getOrderedProduct(product: Product): OrderedProduct {
    var orderedProduct: OrderedProduct? = null
    forEach {
        if (product.id == it.id) {
            orderedProduct = it
        }
    }
    return orderedProduct ?: OrderedProduct()
}

fun getFormattedPrice(product: Product): String =
    NumberFormat.getCurrencyInstance().format(product.price)

fun getFormattedPrice(orderedProduct: OrderedProduct): String =
    NumberFormat.getCurrencyInstance().format(orderedProduct.product.price * orderedProduct.quantity)