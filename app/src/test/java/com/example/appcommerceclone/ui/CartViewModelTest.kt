package com.example.appcommerceclone.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.data.product.model.OrderedProduct
import com.example.appcommerceclone.ui.cart.CartViewModel
import com.example.appcommerceclone.util.getOrAwaitValue
import com.example.appcommerceclone.util.getPrice
import com.example.appcommerceclone.util.getTotalPrice
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
        cartViewModel.addToCart(productJewelry)

        val orderedProduct = cartViewModel.cartProducts.first()
        assertThat(orderedProduct.product).isEqualTo(productJewelry)

        cartViewModel.increaseQuantity(orderedProduct)
        assertThat(orderedProduct.quantity).isEqualTo(2)
    }

    @Test
    fun increaseOrderedProductQuantity_byAddingTheSameProductTwice_verifyQuantityIsEqualTo2() {
        cartViewModel.addToCart(productElectronic)
        cartViewModel.addToCart(productElectronic)

        val orderedProduct = cartViewModel.cartProducts.first()
        assertThat(orderedProduct.product).isEqualTo(productElectronic)
        assertThat(orderedProduct.quantity).isEqualTo(2)
    }

    @Test
    fun decreaseOrderedProductQuantity_verifyQuantityIsEqualTo1() {
        cartViewModel.addToCart(productMensClothing)

        val orderedProduct = cartViewModel.cartProducts.first()
        assertThat(orderedProduct.product).isEqualTo(productMensClothing)

        cartViewModel.increaseQuantity(orderedProduct)
        assertThat(orderedProduct.quantity).isEqualTo(2)

        cartViewModel.decreaseQuantity(orderedProduct)
        assertThat(orderedProduct.quantity).isEqualTo(1)
    }

    @Test
    fun decreaseOrderedProductWhenQuantityIs1_verifyOrderedProductWasRemoved() {
        cartViewModel.addToCart(productWomensClothing)

        val orderedProduct = cartViewModel.cartProducts.first()
        assertThat(orderedProduct.product).isEqualTo(productWomensClothing)
        assertThat(orderedProduct.quantity).isEqualTo(1)

        cartViewModel.decreaseQuantity(orderedProduct)
        assertThat(cartViewModel.cartProducts).isEmpty()
    }

    @Test
    fun updateTotalPrice_verifyIfWasUpdated() {
        var totalPrice = cartViewModel.cartTotalPrice.getOrAwaitValue()
        assertThat(totalPrice).isEqualTo(0.0)

        cartViewModel.addToCart(productJewelry)

        val orderedProduct = cartViewModel.cartProducts.first()
        assertThat(orderedProduct.product).isEqualTo(productJewelry)
        assertThat(orderedProduct.quantity).isEqualTo(1)

        totalPrice = cartViewModel.cartTotalPrice.getOrAwaitValue()
        assertThat(totalPrice).isEqualTo(orderedProduct.getPrice())
    }

    @Test
    fun abandonCart_verifyCartListIsEmpty_and_totalPriceIsEqualTo0() {
        val cartProducts = cartViewModel.cartProducts
        assertThat(cartProducts).isEmpty()

        cartViewModel.addToCart(productJewelry)
        cartViewModel.addToCart(productElectronic)
        cartViewModel.addToCart(productMensClothing)
        cartViewModel.addToCart(productWomensClothing)

        assertThat(cartProducts).isNotEmpty()
        assertThat(cartProducts).hasSize(4)
        assertThat(cartProducts).contains(OrderedProduct(productJewelry))
        assertThat(cartProducts).contains(OrderedProduct(productElectronic))
        assertThat(cartProducts).contains(OrderedProduct(productMensClothing))
        assertThat(cartProducts).contains(OrderedProduct(productWomensClothing))

        var cartTotalPrice = cartViewModel.cartTotalPrice.getOrAwaitValue()
        assertThat(cartTotalPrice).isEqualTo(cartProducts.getTotalPrice())

        cartViewModel.abandonCart()
        assertThat(cartProducts).isEmpty()

        cartTotalPrice = cartViewModel.cartTotalPrice.getOrAwaitValue()
        assertThat(cartTotalPrice).isEqualTo(0.0)
    }
}