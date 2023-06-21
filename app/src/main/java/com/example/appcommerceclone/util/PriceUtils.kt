package com.example.appcommerceclone.util

import com.example.appcommerceclone.model.order.OrderedProduct
import java.text.NumberFormat

fun Collection<OrderedProduct>.getTotalPrice(): Double =
    sumOf { orderedProduct: OrderedProduct ->
        orderedProduct.product.price * orderedProduct.quantity
    }

fun Double.formatPrice(): String =
    NumberFormat.getCurrencyInstance().format(this)