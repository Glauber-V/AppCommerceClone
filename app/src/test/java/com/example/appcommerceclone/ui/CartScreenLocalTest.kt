package com.example.appcommerceclone.ui

import androidx.activity.ComponentActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.model.order.OrderedProduct
import com.example.appcommerceclone.ui.cart.CartScreen
import com.example.appcommerceclone.ui.cart.CartViewModel
import com.example.appcommerceclone.util.formatPrice
import com.example.appcommerceclone.util.getStringResource
import com.example.appcommerceclone.util.productJewelry
import com.example.appcommerceclone.util.showSemanticTreeInConsole
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class CartScreenLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeAndroidRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartProducts: State<List<OrderedProduct>>
    private lateinit var cartTotalPrice: State<Double>

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        showSemanticTreeInConsole()
        composeAndroidRule.setContent {
            cartViewModel = CartViewModel()
            cartProducts = cartViewModel.cartProducts.observeAsState(initial = emptyList())
            cartTotalPrice = cartViewModel.cartTotalPrice.observeAsState(initial = 0.0)
            MaterialTheme {
                CartScreen(
                    cartProducts = cartProducts.value,
                    onQuantityIncrease = { cartViewModel.increaseQuantity(it) },
                    onQuantityDecrease = { cartViewModel.decreaseQuantity(it) },
                    cartTotalPrice = cartTotalPrice.value.formatPrice(),
                    onAbandonCart = { cartViewModel.abandonCart() },
                    onConfirmPurchase = { }
                )
            }
        }
    }

    @Test
    fun onCartScreen_clickIncreaseQuantityBtn_verifyCartWasUpdatedCorrectly() {

        assertThat(cartProducts.value).isEmpty()

        cartViewModel.addToCart(productJewelry)
        assertThat(cartProducts.value).isNotEmpty()
        assertThat(cartProducts.value).hasSize(1)

        with(composeAndroidRule) {

            onRoot().printToLog("onCartScreen|IncreaseQuantity")

            val orderedProduct = cartProducts.value.first()
            assertThat(orderedProduct.quantity).isEqualTo(1)

            onNodeWithText(orderedProduct.product.name)
                .assertExists()
                .assertIsDisplayed()
                .onParent()
                .onChildren()
                .filterToOne(hasContentDescription(getStringResource(R.string.content_desc_item_cart_increase_btn)))
                .performClick()

            assertThat(orderedProduct.quantity).isEqualTo(2)
        }
    }

    @Test
    fun onCartScreen_clickDecreaseQuantityBtn_verifyCartWasUpdatedCorrectly() {

        assertThat(cartProducts.value).isEmpty()

        cartViewModel.addToCart(productJewelry)
        cartViewModel.addToCart(productJewelry)

        assertThat(cartProducts.value).isNotEmpty()
        assertThat(cartProducts.value).hasSize(1)

        with(composeAndroidRule) {

            onRoot().printToLog("onCartScreen|DecreaseQuantity")

            val orderedProduct = cartProducts.value.first()
            assertThat(orderedProduct.quantity).isEqualTo(2)

            onNodeWithText(orderedProduct.product.name)
                .assertExists()
                .assertIsDisplayed()
                .onParent()
                .onChildren()
                .filterToOne(hasContentDescription(getStringResource(R.string.content_desc_item_cart_decrease_btn)))
                .performClick()

            assertThat(orderedProduct.quantity).isEqualTo(1)
        }
    }

    @Test
    fun onCartScreen_clickDecreaseQuantityBtn_whenQuantityIs1_verifyCartWasUpdatedCorrectly() {

        assertThat(cartProducts.value).isEmpty()

        cartViewModel.addToCart(productJewelry)
        assertThat(cartProducts.value).isNotEmpty()
        assertThat(cartProducts.value).hasSize(1)

        with(composeAndroidRule) {

            onRoot().printToLog("onCartScreen|RemoveProduct")

            val orderedProduct = cartProducts.value.first()
            assertThat(orderedProduct.quantity).isEqualTo(1)

            onNodeWithText(orderedProduct.product.name)
                .assertExists()
                .assertIsDisplayed()
                .onParent()
                .onChildren()
                .filterToOne(hasContentDescription(getStringResource(R.string.content_desc_item_cart_decrease_btn)))
                .performClick()

            assertThat(cartProducts.value).isEmpty()
        }
    }
}