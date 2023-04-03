package com.example.appcommerceclone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appcommerceclone.model.order.Order
import com.example.appcommerceclone.model.order.OrderedProduct
import com.example.appcommerceclone.model.product.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.NumberFormat
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {

    private var order = Order()

    private val _cartProducts = MutableLiveData(order.orderedProducts)
    val cartProducts: LiveData<MutableList<OrderedProduct>> = _cartProducts

    private var _cartTotalPrice = MutableLiveData(0.0)
    val cartTotalPrice: LiveData<Double> = _cartTotalPrice


    fun addToCart(product: Product) {
        isProductAlreadyInCart(product).also { orderedProduct ->
            orderedProduct?.apply {
                increaseQuantity(this)
                return
            }
        }

        val newOrderedProduct = OrderedProduct(product)

        _cartProducts.value?.add(newOrderedProduct)
        updateTotalPrice()
    }

    private fun isProductAlreadyInCart(product: Product): OrderedProduct? {
        var result: OrderedProduct? = null
        _cartProducts.value?.forEach { orderedProduct ->
            if (product.id == orderedProduct.id) result = orderedProduct
        }
        return result
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


    private fun updateTotalPrice() {
        var totalPrice = 0.0
        _cartProducts.value?.forEach { orderedProduct ->
            totalPrice += orderedProduct.product.price * orderedProduct.quantity
        }
        _cartTotalPrice.value = totalPrice
    }

    fun getFormattedTotalPrice(totalPrice: Double): String {
        return NumberFormat.getCurrencyInstance().format(totalPrice)
    }


    fun createOrder(): Order {
        order.orderedProducts = _cartProducts.value!!
        order.total = _cartTotalPrice.value!!
        return order
    }

    fun onOrderDispatched() {
        order = Order()
        _cartProducts.value = mutableListOf()
        updateTotalPrice()
    }


    fun abandonCart() {
        _cartProducts.value = mutableListOf()
        updateTotalPrice()
    }
}