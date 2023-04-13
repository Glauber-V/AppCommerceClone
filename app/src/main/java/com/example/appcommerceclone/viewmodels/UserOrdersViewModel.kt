package com.example.appcommerceclone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appcommerceclone.model.order.Order
import com.example.appcommerceclone.util.TimeExt
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class UserOrdersViewModel @Inject constructor() : ViewModel() {

    private val _orders = MutableLiveData<MutableList<Order>>(mutableListOf())
    val orders: LiveData<MutableList<Order>> = _orders

    private val _order = MutableLiveData<Order?>()
    val order: LiveData<Order?> = _order


    fun receiveOrder(order: Order) {
        _order.value = order
    }

    fun processOrder(order: Order, userId: Int) {
        order.id = Random.nextInt(from = 10, until = 999)
        order.userId = userId
        order.date = TimeExt.getCurrentTime()

        _orders.value?.add(order)
        onProcessOrderComplete()
    }

    private fun onProcessOrderComplete() {
        _order.value = null
    }
}