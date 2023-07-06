package com.example.appcommerceclone.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appcommerceclone.data.model.order.Order
import com.example.appcommerceclone.data.model.order.OrderedProduct
import com.example.appcommerceclone.util.TimeExt
import com.example.appcommerceclone.util.getTotalPrice
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class UserOrdersViewModel @Inject constructor() : ViewModel() {

    private val _orders = MutableLiveData<MutableList<Order>>(mutableListOf())
    val orders: LiveData<MutableList<Order>> = _orders


    fun createOrder(userId: Int, orderedProductList: List<OrderedProduct>) {
        val order = Order(
            id = Random.nextInt(from = 10, until = 999),
            userId = userId,
            date = TimeExt.getCurrentTime(),
            orderedProducts = orderedProductList,
            total = orderedProductList.getTotalPrice()
        )

        orders.value?.add(order)
    }
}