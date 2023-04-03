package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productJewelery
import com.example.appcommerceclone.data.user.UserOrders
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.UsersModule
import com.example.appcommerceclone.model.order.OrderedProduct
import com.example.appcommerceclone.ui.cart.CartFragment
import com.example.appcommerceclone.util.*
import com.example.appcommerceclone.viewmodels.CartViewModel
import com.example.appcommerceclone.viewmodels.UserOrdersViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowDialog
import java.text.NumberFormat
import javax.inject.Inject

@UninstallModules(UsersModule::class, DispatcherModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class CartFragmentLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testNavHostControllerRule = TestNavHostControllerRule(R.id.cart_fragment)

    @Inject lateinit var userOrders: UserOrders
    @Inject lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var navHostController: TestNavHostController
    private lateinit var cartViewModel: CartViewModel
    private lateinit var userOrdersViewModel: UserOrdersViewModel
    private lateinit var factory: TestFragmentFactory

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
        cartViewModel = CartViewModel()
        userOrdersViewModel = UserOrdersViewModel(userOrders, dispatcherProvider)
        factory = TestFragmentFactory(
            cartViewModelTest = cartViewModel,
            userOrdersViewModelTest = userOrdersViewModel
        )
    }

    @Test
    fun clickIncreaseQuantityBtn_verifyCartWasUpdatedCorrectly() {

        cartViewModel.addToCart(productJewelery)

        val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isNotEmpty()
        assertThat(cartProducts).hasSize(1)

        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {

            val orderedProduct = cartProducts.first()
            assertThat(orderedProduct.quantity).isEqualTo(1)

            onView(withId(R.id.cart_increase_quantity)).perform(click())
            assertThat(orderedProduct.quantity).isEqualTo(2)

            onView(withId(R.id.ordered_product_name)).check(matches(withText(orderedProduct.product.name)))
            onView(withId(R.id.ordered_product_quantity)).check(matches(withText(orderedProduct.quantity.toString())))
            onView(withId(R.id.ordered_product_price)).check(matches(withText(getFormattedPrice(orderedProduct))))
        }
    }

    @Test
    fun clickDecreaseQuantityBtn_verifyCartWasUpdatedCorrectly() {

        cartViewModel.addToCart(productJewelery)

        val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isNotEmpty()
        assertThat(cartProducts).hasSize(1)

        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {

            val orderedProduct = cartViewModel.cartProducts.getOrAwaitValue().first()
            assertThat(orderedProduct.quantity).isEqualTo(1)

            onView(withId(R.id.cart_increase_quantity)).perform(click())
            assertThat(orderedProduct.quantity).isEqualTo(2)

            onView(withId(R.id.ordered_product_name)).check(matches(withText(orderedProduct.product.name)))
            onView(withId(R.id.ordered_product_quantity)).check(matches(withText(orderedProduct.quantity.toString())))
            onView(withId(R.id.ordered_product_price)).check(matches(withText(getFormattedPrice(orderedProduct))))

            onView(withId(R.id.cart_decrease_quantity)).perform(click())
            assertThat(orderedProduct.quantity).isEqualTo(1)

            onView(withId(R.id.ordered_product_name)).check(matches(withText(orderedProduct.product.name)))
            onView(withId(R.id.ordered_product_quantity)).check(matches(withText(orderedProduct.quantity.toString())))
            onView(withId(R.id.ordered_product_price)).check(matches(withText(getFormattedPrice(orderedProduct))))
        }
    }

    @Test
    fun clickDecreaseQuantityBtn_whenQuantityIs1_verifyCartWasUpdatedCorrectly() {

        cartViewModel.addToCart(productJewelery)

        var cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isNotEmpty()
        assertThat(cartProducts).hasSize(1)

        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {

            val orderedProduct = cartViewModel.cartProducts.getOrAwaitValue().first()
            assertThat(orderedProduct.quantity).isEqualTo(1)

            onView(withId(R.id.cart_decrease_quantity)).perform(click())

            cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
            assertThat(cartProducts).isEmpty()
        }
    }

    @Test
    fun clickAbandonCart_whenCartIsNotEmpty_alertDialogShouldBeVisible() {

        cartViewModel.addToCart(productJewelery)

        var cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isNotEmpty()
        assertThat(cartProducts).hasSize(1)

        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {

            cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
            assertThat(cartProducts).isNotEmpty()

            onView(withId(R.id.cart_cancel_purchase_btn)).perform(click())

            val dialog = ShadowDialog.getLatestDialog()
            assertTrue(dialog.isShowing)

            onView(withText(R.string.cart_dialog_title))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))

            onView(withId(R.id.cart_dialog_positive)).perform(click())

            cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
            assertThat(cartProducts).isEmpty()
        }
    }

    @Test
    fun clickAbandonCart_whenCartIsEmpty_navigateUp() {

        val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isEmpty()

        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.cart_fragment)

            onView(withId(R.id.cart_cancel_purchase_btn)).perform(click())

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.cart_fragment)
        }
    }

    @Test
    fun clickConfirmPurchase_whenCartIsNotEmpty_navigateToOrdersFragment() {

        cartViewModel.addToCart(productJewelery)

        val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isNotEmpty()
        assertThat(cartProducts).hasSize(1)

        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.cart_fragment)

            onView(withId(R.id.cart_confirm_purchase_btn)).perform(click())

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.cart_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.orders_fragment)
        }
    }

    @Test
    fun clickConfirmPurchase_whenCartIsEmpty_navigateToOrdersFragment() {

        val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isEmpty()

        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.cart_fragment)

            onView(withId(R.id.cart_confirm_purchase_btn)).perform(click())

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.cart_fragment)
        }
    }


    private fun getFormattedPrice(orderedProduct: OrderedProduct): String {
        return NumberFormat.getCurrencyInstance().format(orderedProduct.product.price * orderedProduct.quantity)
    }
}