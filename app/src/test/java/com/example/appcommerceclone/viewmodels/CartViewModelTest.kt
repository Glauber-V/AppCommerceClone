package com.example.appcommerceclone.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.data.product.FakeProductsRepository.Companion.productElectronic
import com.example.appcommerceclone.data.product.FakeProductsRepository.Companion.productJewelery
import com.example.appcommerceclone.data.product.FakeProductsRepository.Companion.productMensClothing
import com.example.appcommerceclone.data.product.FakeProductsRepository.Companion.productWomensClothing
import com.example.appcommerceclone.util.getOrAwaitValue
import com.example.appcommerceclone.util.getOrderedProduct
import com.example.appcommerceclone.util.getOrderedProducts
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CartViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var cartViewModel: CartViewModel

    @Before
    fun setup() {
        cartViewModel = CartViewModel()
        cartViewModel.apply {
            addToCart(productJewelery)
            addToCart(productElectronic)
            addToCart(productWomensClothing)
            addToCart(productMensClothing)
        }
    }


    @Test
    fun verifyCartIsNotEmpty_and_sizeIsEqualTo4() {
        val orderedProducts = cartViewModel.getOrderedProducts()
        assertThat(orderedProducts).isNotEmpty()
        assertThat(orderedProducts.size).isEqualTo(4)
    }

    @Test
    fun increaseOrderedProductQuantity_verifyQuantityIsEqualTo2() {
        val orderedProduct = cartViewModel.getOrderedProduct(productJewelery)

        cartViewModel.increaseQuantity(orderedProduct)

        assertThat(orderedProduct.quantity).isEqualTo(2)
    }

    @Test
    fun increaseOrderedProductQuantity_byAddingTheSameProductTwice_verifyQuantityIsEqualTo2() {
        cartViewModel.addToCart(productJewelery)

        val orderedProduct = cartViewModel.getOrderedProduct(productJewelery)

        assertThat(orderedProduct.quantity).isEqualTo(2)
    }

    @Test
    fun decreaseOrderedProductQuantity_verifyQuantityIsEqualTo1() {
        val orderedProduct = cartViewModel.getOrderedProduct(productJewelery)

        cartViewModel.increaseQuantity(orderedProduct)
        assertThat(orderedProduct.quantity).isEqualTo(2)

        cartViewModel.decreaseQuantity(orderedProduct)
        assertThat(orderedProduct.quantity).isEqualTo(1)
    }

    @Test
    fun decreaseOrderedProductWhenQuantityIs1_verifyOrderedProductWasRemoved() {
        val orderedProduct = cartViewModel.getOrderedProduct(productJewelery)
        assertThat(orderedProduct.quantity).isEqualTo(1)

        cartViewModel.decreaseQuantity(orderedProduct)
        assertThat(cartViewModel.getOrderedProducts()).doesNotContain(orderedProduct)
    }

    @Test
    fun updateTotalPrice_verifyIfWasUpdated() {
        val firstPriceCheck = cartViewModel.cartTotalPrice.getOrAwaitValue()
        assertThat(firstPriceCheck).isEqualTo(50.0)

        cartViewModel.getOrderedProducts().forEach { cartViewModel.increaseQuantity(it) }

        val secondPriceCheck = cartViewModel.cartTotalPrice.getOrAwaitValue()
        assertThat(secondPriceCheck).isEqualTo(100.0)
    }

    @Test
    fun abandonCart_verifyCartListIsEmpty_and_totalPriceIsEqualTo0() {
        var orderedProducts = cartViewModel.getOrderedProducts()
        assertThat(orderedProducts).isNotEmpty()

        cartViewModel.abandonCart()

        orderedProducts = cartViewModel.getOrderedProducts()
        assertThat(orderedProducts).isEmpty()

        val totalPrice = cartViewModel.cartTotalPrice.getOrAwaitValue()
        assertThat(totalPrice).isEqualTo(0.0)
    }
}