package com.example.appcommerceclone.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.util.getOrAwaitValue
import com.example.appcommerceclone.util.getOrderedProduct
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
    }


    @Test
    fun `add product to cart and verify if it was added`() {
        val product = Product()
        cartViewModel.addToCart(product)

        val result = cartViewModel.cartProducts.getOrAwaitValue()
        val orderedProduct = result.getOrderedProduct(product)

        assertThat(result).isNotEmpty()
        assertThat(result.size).isEqualTo(1)
        assertThat(result).contains(orderedProduct)
    }

    @Test
    fun `add the same product twice to increase quantity and return true if quantity is greater than 1`() {
        val product = Product()
        cartViewModel.addToCart(product)
        cartViewModel.addToCart(product)

        val result = cartViewModel.cartProducts.getOrAwaitValue()
        val orderedProduct = result.getOrderedProduct(product)

        assertThat(orderedProduct).isNotNull()
        assertThat(orderedProduct.quantity).isGreaterThan(1)
    }

    @Test
    fun `increase orderedProduct quantity and return true if quantity is bigger than 1`() {
        val product = Product()
        cartViewModel.addToCart(product)

        val result = cartViewModel.cartProducts.getOrAwaitValue()
        val orderedProduct = result.getOrderedProduct(product)

        cartViewModel.increaseQuantity(orderedProduct)

        assertThat(orderedProduct).isNotNull()
        assertThat(orderedProduct.quantity).isGreaterThan(1)
    }

    @Test
    fun `decrease orderedProduct quantity and return true if quantity is equal to 1`() {
        val product = Product()
        cartViewModel.addToCart(product)

        val result = cartViewModel.cartProducts.getOrAwaitValue()
        val orderedProduct = result.getOrderedProduct(product)

        cartViewModel.increaseQuantity(orderedProduct)
        cartViewModel.decreaseQuantity(orderedProduct)

        assertThat(orderedProduct).isNotNull()
        assertThat(orderedProduct.quantity).isEqualTo(1)
    }

    @Test
    fun `remove orderedProduct if quantity is less than 1`() {
        val product = Product()
        cartViewModel.addToCart(product)

        val result = cartViewModel.cartProducts.getOrAwaitValue()
        val orderedProduct = result.getOrderedProduct(product)

        cartViewModel.decreaseQuantity(orderedProduct)

        assertThat(orderedProduct).isNotNull()
        assertThat(result).doesNotContain(orderedProduct)
    }

    @Test
    fun `update the totalPrice and return true if totalPrice was updated`() {
        val product1 = Product(id = 1, price = 5.0)
        val product2 = Product(id = 2, price = 10.0)
        cartViewModel.addToCart(product1)
        cartViewModel.addToCart(product2)

        val firstPriceCheck = cartViewModel.cartTotalPrice.getOrAwaitValue()

        val result = cartViewModel.cartProducts.getOrAwaitValue()
        val orderedProduct1 = result.getOrderedProduct(product1)
        val orderedProduct2 = result.getOrderedProduct(product2)

        cartViewModel.increaseQuantity(orderedProduct1)
        cartViewModel.increaseQuantity(orderedProduct2)

        val secondPriceCheck = cartViewModel.cartTotalPrice.getOrAwaitValue()

        assertThat(orderedProduct1).isNotNull()
        assertThat(orderedProduct2).isNotNull()
        assertThat(firstPriceCheck).isEqualTo(15.0)
        assertThat(secondPriceCheck).isEqualTo(30.0)
    }

    @Test
    fun `abandon cart and return true if cart list is empty`() {
        val product1 = Product(id = 1)
        val product2 = Product(id = 2)
        val product3 = Product(id = 3)
        val product4 = Product(id = 4)

        cartViewModel.addToCart(product1)
        cartViewModel.addToCart(product2)
        cartViewModel.addToCart(product3)
        cartViewModel.addToCart(product4)

        val result1 = cartViewModel.cartProducts.getOrAwaitValue()

        cartViewModel.abandonCart()

        val result2 = cartViewModel.cartProducts.getOrAwaitValue()

        assertThat(result1).isNotEmpty()
        assertThat(result2).isEmpty()
    }
}