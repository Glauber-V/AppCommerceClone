package com.example.appcommerceclone.ui

import androidx.activity.ComponentActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
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
import com.example.appcommerceclone.data.product.model.OrderedProduct
import com.example.appcommerceclone.ui.cart.CartScreen
import com.example.appcommerceclone.ui.cart.CartViewModel
import com.example.appcommerceclone.util.formatPrice
import com.example.appcommerceclone.util.getStringResource
import com.example.appcommerceclone.util.productElectronic
import com.example.appcommerceclone.util.productJewelry
import com.example.appcommerceclone.util.productMensClothing
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

    private lateinit var cartProducts: List<OrderedProduct>
    private lateinit var cartTotalPrice: State<Double>

    private val purchaseCompletionStatus = mutableStateOf(false)

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        showSemanticTreeInConsole()
        composeAndroidRule.setContent {
            cartViewModel = CartViewModel()
            cartProducts = cartViewModel.cartProducts
            cartTotalPrice = cartViewModel.cartTotalPrice.observeAsState(initial = 0.0)
            MaterialTheme {
                CartScreen(
                    cartProducts = cartProducts,
                    onQuantityIncrease = { cartViewModel.increaseQuantity(it) },
                    onQuantityDecrease = { cartViewModel.decreaseQuantity(it) },
                    cartTotalPrice = cartTotalPrice.value.formatPrice(),
                    onAbandonCart = { cartViewModel.abandonCart() },
                    onConfirmPurchase = { purchaseCompletionStatus.value = true }
                )
            }
        }
    }

    @Test
    fun onCartScreen_clickIncreaseQuantityBtn_verifyCartWasUpdatedCorrectly_verifyPurchaseCompletionStatusIsTrue() {
        assertThat(cartProducts).isEmpty()

        cartViewModel.addToCart(productJewelry)
        assertThat(cartProducts).isNotEmpty()
        assertThat(cartProducts).hasSize(1)
        assertThat(cartProducts).contains(OrderedProduct(productJewelry))

        with(composeAndroidRule) {
            onRoot().printToLog("onCartScreen|IncreaseQuantity")

            val orderedProduct = cartProducts.first()
            assertThat(orderedProduct.quantity).isEqualTo(1)

            onNodeWithText(orderedProduct.product.name)
                .assertExists()
                .assertIsDisplayed()
                .onParent()
                .onChildren()
                .filterToOne(hasContentDescription(getStringResource(R.string.content_desc_item_cart_increase_btn)))
                .performClick()

            assertThat(orderedProduct.quantity).isEqualTo(2)

            onNodeWithText(orderedProduct.quantity.toString())
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText(getStringResource(R.string.cart_confirm_purchase_btn))
                .assertExists()
                .assertIsDisplayed()
                .performClick()

            assertThat(purchaseCompletionStatus.value).isTrue()
        }
    }

    @Test
    fun onCartScreen_clickDecreaseQuantityBtn_verifyCartWasUpdatedCorrectly_verifyPurchaseCompletionStatusIsTrue() {
        assertThat(cartProducts).isEmpty()

        cartViewModel.addToCart(productElectronic)
        cartViewModel.addToCart(productElectronic)

        assertThat(cartProducts).isNotEmpty()
        assertThat(cartProducts).hasSize(1)
        assertThat(cartProducts).contains(OrderedProduct(productElectronic, 2))

        with(composeAndroidRule) {
            onRoot().printToLog("onCartScreen|DecreaseQuantity")

            val orderedProduct = cartProducts.first()
            assertThat(orderedProduct.quantity).isEqualTo(2)

            onNodeWithText(orderedProduct.product.name)
                .assertExists()
                .assertIsDisplayed()
                .onParent()
                .onChildren()
                .filterToOne(hasContentDescription(getStringResource(R.string.content_desc_item_cart_decrease_btn)))
                .performClick()

            assertThat(orderedProduct.quantity).isEqualTo(1)

            onNodeWithText(orderedProduct.quantity.toString())
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText(getStringResource(R.string.cart_confirm_purchase_btn))
                .assertExists()
                .assertIsDisplayed()
                .performClick()

            assertThat(purchaseCompletionStatus.value).isTrue()
        }
    }

    @Test
    fun onCartScreen_clickDecreaseQuantityBtn_whenQuantityIs1_verifyCartWasUpdatedCorrectly_verifyPurchaseCompletionStatusIsFalse() {
        assertThat(cartProducts).isEmpty()

        cartViewModel.addToCart(productMensClothing)
        assertThat(cartProducts).isNotEmpty()
        assertThat(cartProducts).hasSize(1)
        assertThat(cartProducts).contains(OrderedProduct(productMensClothing))

        with(composeAndroidRule) {
            onRoot().printToLog("onCartScreen|RemoveProduct")

            val orderedProduct = cartProducts.first()
            assertThat(orderedProduct.quantity).isEqualTo(1)

            onNodeWithText(orderedProduct.product.name)
                .assertExists()
                .assertIsDisplayed()
                .onParent()
                .onChildren()
                .filterToOne(hasContentDescription(getStringResource(R.string.content_desc_item_cart_decrease_btn)))
                .performClick()

            assertThat(cartProducts).isEmpty()

            onNodeWithText(orderedProduct.product.name)
                .assertDoesNotExist()

            onNodeWithText(getStringResource(R.string.cart_confirm_purchase_btn))
                .assertExists()
                .assertIsDisplayed()
                .assertIsNotEnabled()

            assertThat(purchaseCompletionStatus.value).isFalse()
        }
    }
}