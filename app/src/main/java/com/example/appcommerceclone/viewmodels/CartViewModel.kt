package com.example.appcommerceclone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appcommerceclone.model.order.Order
import com.example.appcommerceclone.model.order.OrderedProduct
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.util.getTotalPrice
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {

    private val _cartProducts = MutableLiveData<MutableList<OrderedProduct>>(mutableListOf())
    val cartProducts: LiveData<MutableList<OrderedProduct>> = _cartProducts

    private var _cartTotalPrice = MutableLiveData(0.0)
    val cartTotalPrice: LiveData<Double> = _cartTotalPrice


    private fun updateTotalPrice() {
        _cartTotalPrice.value = _cartProducts.value?.getTotalPrice() ?: 0.0
    }

    fun addToCart(product: Product) {
        _cartProducts.value?.firstOrNull { orderedProduct ->
            orderedProduct.product == product
        }?.let {
            increaseQuantity(it)
            return
        }

        val newOrderedProduct = OrderedProduct(product)

        _cartProducts.value?.add(newOrderedProduct)
        updateTotalPrice()
    }

    fun increaseQuantity(orderedProduct: OrderedProduct) {
        orderedProduct.quantity += 1
        updateTotalPrice()
    }

    fun decreaseQuantity(orderedProduct: OrderedProduct) {
        orderedProduct.quantity -= 1
        if (orderedProduct.quantity < 1) _cartProducts.value?.remove(orderedProduct)
        updateTotalPrice()
    }

    fun createOrder(orderedProduct: List<OrderedProduct>): Order {
        return Order(
            orderedProducts = orderedProduct,
            total = orderedProduct.getTotalPrice()
        ).also { abandonCart() }
    }

    fun abandonCart() {
        _cartProducts.value = mutableListOf()
        updateTotalPrice()
    }
}