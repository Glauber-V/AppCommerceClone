package com.example.appcommerceclone.adapters.cart

import com.example.appcommerceclone.model.order.OrderedProduct

interface CartListener {

    fun cartIncreaseQuantity(orderedProduct: OrderedProduct, adapterPosition: Int)

    fun cartDecreaseQuantity(orderedProduct: OrderedProduct, adapterPosition: Int)
}