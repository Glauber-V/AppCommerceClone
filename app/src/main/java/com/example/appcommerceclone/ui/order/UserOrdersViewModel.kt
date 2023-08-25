package com.example.appcommerceclone.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appcommerceclone.data.product.model.Order
import com.example.appcommerceclone.data.product.model.OrderedProduct
import com.example.appcommerceclone.data.product.model.Product
import com.example.appcommerceclone.util.getCurrentTime
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class UserOrdersViewModel @Inject constructor() : ViewModel() {

    private val _orders = MutableLiveData<MutableList<Order>>(mutableListOf())
    val orders: LiveData<MutableList<Order>> = _orders

    fun createOrder(userId: Int, product: Product) {
        val order = Order(
            id = Random.nextInt(from = 10, until = 999),
            userId = userId,
            date = getCurrentTime(),
            orderedProducts = listOf(OrderedProduct(product))
        )
        _orders.value?.add(order)
    }

    fun createOrder(userId: Int, orderedProductList: List<OrderedProduct>) {
        val order = Order(
            id = Random.nextInt(from = 10, until = 999),
            userId = userId,
            date = getCurrentTime(),
            orderedProducts = orderedProductList
        )
        _orders.value?.add(order)
    }
}