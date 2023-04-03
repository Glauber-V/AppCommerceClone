package com.example.appcommerceclone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.user.UserOrders
import com.example.appcommerceclone.model.order.Order
import com.example.appcommerceclone.util.TimeExt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class UserOrdersViewModel @Inject constructor(
    private val userOrders: UserOrders,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _orders = MutableLiveData<MutableList<Order>>(mutableListOf())
    val orders: LiveData<MutableList<Order>> = _orders

    private val _order = MutableLiveData<Order?>()
    val order: LiveData<Order?> = _order

    private val _selectedOrder = MutableLiveData<Order?>()
    val selectedOrder: LiveData<Order?> = _selectedOrder


    fun refreshUserOrders(userId: Int) {
        viewModelScope.launch(dispatcherProvider.main) {
            _orders.value = userOrders.getOrdersByUserId(userId)
        }
    }


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


    fun selectOrder(order: Order) {
        _selectedOrder.value = order
    }

    fun onSelectOrderComplete() {
        _selectedOrder.value = null
    }
}