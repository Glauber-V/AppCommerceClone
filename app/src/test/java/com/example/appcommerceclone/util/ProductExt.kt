package com.example.appcommerceclone.util

import com.example.appcommerceclone.model.order.OrderedProduct
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.viewmodels.CartViewModel
import java.text.NumberFormat

/** Get [CartViewModel]'s most updated list of [OrderedProduct] */
fun CartViewModel.getOrderedProducts(): List<OrderedProduct> {
    return cartProducts.getOrAwaitValue()
}

/** Get the most updated [OrderedProduct] from a [CartViewModel] using a [Product] as parameter */
fun CartViewModel.getOrderedProduct(product: Product): OrderedProduct {
    return cartProducts.getOrAwaitValue().firstOrNull { it.product.id == product.id } ?: OrderedProduct()
}

fun getFormattedPrice(product: Product): String =
    NumberFormat.getCurrencyInstance().format(product.price)

fun getFormattedPrice(orderedProduct: OrderedProduct): String =
    NumberFormat.getCurrencyInstance().format(orderedProduct.product.price * orderedProduct.quantity)