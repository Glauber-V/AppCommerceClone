package com.example.appcommerceclone.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appcommerceclone.data.product.model.OrderedProduct
import com.example.appcommerceclone.data.product.model.Product
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
        _cartTotalPrice.value = _cartProducts.value?.getTotalPrice()
    }

    fun addToCart(product: Product) {
        _cartProducts.value?.firstOrNull { orderedProduct ->
            orderedProduct.product == product
        }?.let { alreadyAddedOrderedProduct ->
            increaseQuantity(alreadyAddedOrderedProduct)
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

    fun abandonCart() {
        _cartProducts.value?.clear()
        updateTotalPrice()
    }
}