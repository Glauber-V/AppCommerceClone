package com.example.appcommerceclone.data.user

import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.model.order.Order
import kotlinx.coroutines.withContext

class DefaultUserOrders(
    private val usersProvider: UsersProvider,
    private val dispatcher: DispatcherProvider
) : UserOrders {

    override suspend fun getOrdersByUserId(userId: Int): MutableList<Order> {
        return runCatching {
            withContext(dispatcher.io) {
                usersProvider.getUserOrders(userId)
            }
        }.getOrElse { mutableListOf() }
    }
}