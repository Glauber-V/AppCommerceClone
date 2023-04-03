package com.example.appcommerceclone.data.user

import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.model.order.Order
import kotlinx.coroutines.withContext

class FakeUserOrders(
    private val usersProvider: UsersProvider,
    private val dispatcherProvider: DispatcherProvider
) : UserOrders {

    override suspend fun getOrdersByUserId(userId: Int): MutableList<Order> {
        return withContext(dispatcherProvider.default) {
            usersProvider.getUserOrders(userId)
        }
    }
}