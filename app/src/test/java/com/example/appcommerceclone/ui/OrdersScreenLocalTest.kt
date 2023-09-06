package com.example.appcommerceclone.ui

import androidx.activity.ComponentActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.product.model.Order
import com.example.appcommerceclone.data.user.FakeUserProvider.Companion.firstUser
import com.example.appcommerceclone.ui.order.OrdersScreen
import com.example.appcommerceclone.ui.order.UserOrdersViewModel
import com.example.appcommerceclone.util.getProductsNamesAsString
import com.example.appcommerceclone.util.getStringResource
import com.example.appcommerceclone.util.orderedProductList
import com.example.appcommerceclone.util.showSemanticTreeInConsole
import com.google.common.truth.Truth.assertThat
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
class OrdersScreenLocalTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var userOrdersViewModel: UserOrdersViewModel

    private lateinit var orders: List<Order>

    @Before
    fun setUp() {
        showSemanticTreeInConsole()
        composeRule.setContent {
            userOrdersViewModel = UserOrdersViewModel().also {
                it.createOrder(
                    userId = firstUser.id,
                    orderedProductList = orderedProductList
                )
            }
            orders = userOrdersViewModel.orders
            MaterialTheme {
                OrdersScreen(orders = orders)
            }
        }
    }

    @Test
    fun onOrderScreen_verifyOrderListIsDisplayed() {
        assertThat(orders).isNotEmpty()
        assertThat(orders).hasSize(1)

        val order = orders.first()

        with(composeRule) {
            onRoot().printToLog("onOderScreen")

            onNodeWithText(getStringResource(R.string.order_item_id, order.id))
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText(getStringResource(R.string.order_item_products, order.getProductsNamesAsString()))
                .assertExists()
                .assertIsDisplayed()
        }
    }
}