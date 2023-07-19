package com.example.appcommerceclone.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.ui.cart.CartViewModel
import com.example.appcommerceclone.util.getOrAwaitValue
import com.example.appcommerceclone.util.productElectronic
import com.example.appcommerceclone.util.productJewelry
import com.example.appcommerceclone.util.productMensClothing
import com.example.appcommerceclone.util.productWomensClothing
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CartViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var cartViewModel: CartViewModel

    @Before
    fun setup() {
        cartViewModel = CartViewModel()
    }

    @Test
    fun increaseOrderedProductQuantity_verifyQuantityIsEqualTo2() {
        val product = productJewelry
        cartViewModel.addToCart(product)

        val orderedProduct = cartViewModel.cartProducts.getOrAwaitValue().first()
        assertThat(orderedProduct.product).isEqualTo(product)

        cartViewModel.increaseQuantity(orderedProduct)
        assertThat(orderedProduct.quantity).isEqualTo(2)
    }

    @Test
    fun increaseOrderedProductQuantity_byAddingTheSameProductTwice_verifyQuantityIsEqualTo2() {
        val product = productJewelry
        cartViewModel.addToCart(product)
        cartViewModel.addToCart(product)

        val orderedProduct = cartViewModel.cartProducts.getOrAwaitValue().first()
        assertThat(orderedProduct.product).isEqualTo(product)
        assertThat(orderedProduct.quantity).isEqualTo(2)
    }

    @Test
    fun decreaseOrderedProductQuantity_verifyQuantityIsEqualTo1() {
        val product = productElectronic
        cartViewModel.addToCart(product)

        val orderedProduct = cartViewModel.cartProducts.getOrAwaitValue().first()
        assertThat(orderedProduct.product).isEqualTo(product)

        cartViewModel.increaseQuantity(orderedProduct)
        assertThat(orderedProduct.quantity).isEqualTo(2)

        cartViewModel.decreaseQuantity(orderedProduct)
        assertThat(orderedProduct.quantity).isEqualTo(1)
    }

    @Test
    fun decreaseOrderedProductWhenQuantityIs1_verifyOrderedProductWasRemoved() {
        val product = productElectronic
        cartViewModel.addToCart(product)

        val orderedProduct = cartViewModel.cartProducts.getOrAwaitValue().first()
        assertThat(orderedProduct.product).isEqualTo(product)
        assertThat(orderedProduct.quantity).isEqualTo(1)

        cartViewModel.decreaseQuantity(orderedProduct)

        val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isEmpty()
    }

    @Test
    fun updateTotalPrice_verifyIfWasUpdated() {
        var totalPrice = cartViewModel.cartTotalPrice.getOrAwaitValue()
        assertThat(totalPrice).isEqualTo(0.0)

        val product = productMensClothing
        cartViewModel.addToCart(product)

        val orderedProduct = cartViewModel.cartProducts.getOrAwaitValue().first()
        assertThat(orderedProduct.product).isEqualTo(product)
        assertThat(orderedProduct.quantity).isEqualTo(1)

        totalPrice = cartViewModel.cartTotalPrice.getOrAwaitValue()
        assertThat(totalPrice).isEqualTo(orderedProduct.quantity * orderedProduct.product.price)
    }

    @Test
    fun abandonCart_verifyCartListIsEmpty_and_totalPriceIsEqualTo0() {
        var cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isEmpty()

        cartViewModel.addToCart(productJewelry)
        cartViewModel.addToCart(productElectronic)
        cartViewModel.addToCart(productMensClothing)
        cartViewModel.addToCart(productWomensClothing)

        cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isNotEmpty()
        assertThat(cartProducts).hasSize(4)

        cartViewModel.abandonCart()

        cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isEmpty()
    }
}