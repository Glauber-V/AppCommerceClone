package com.example.appcommerceclone.data.user

import com.example.appcommerceclone.model.order.Order

interface UserOrders {

    suspend fun getOrdersByUserId(userId: Int): MutableList<Order>
}