package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productJewelery
import com.example.appcommerceclone.ui.cart.CartFragment
import com.example.appcommerceclone.util.*
import com.example.appcommerceclone.viewmodels.CartViewModel
import com.example.appcommerceclone.viewmodels.UserOrdersViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.*
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowDialog

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class CartFragmentLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testNavHostControllerRule = TestNavHostControllerRule(R.id.cart_fragment)

    private lateinit var navHostController: TestNavHostController
    private lateinit var cartViewModel: CartViewModel
    private lateinit var userOrdersViewModel: UserOrdersViewModel
    private lateinit var factory: TestFragmentFactory

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
        cartViewModel = CartViewModel()
        userOrdersViewModel = UserOrdersViewModel()
        factory = TestFragmentFactory(
            cartViewModelTest = cartViewModel,
            userOrdersViewModelTest = userOrdersViewModel
        )
    }

    @Test
    fun launchCartFragment_clickIncreaseQuantityBtn_verifyCartWasUpdatedCorrectly() {

        cartViewModel.addToCart(productJewelery)

        val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isNotEmpty()
        assertThat(cartProducts).hasSize(1)

        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {

            val orderedProduct = cartProducts.first()
            assertThat(orderedProduct.quantity).isEqualTo(1)

            onView(withId(R.id.item_product_in_cart_increase_quantity_btn)).perform(click())
            assertThat(orderedProduct.quantity).isEqualTo(2)

            onView(withId(R.id.cart_recycler_view))
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.product.name)))))
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.quantity.toString())))))
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.getFormattedPrice())))))
        }
    }

    @Test
    fun launchCartFragment_clickDecreaseQuantityBtn_verifyCartWasUpdatedCorrectly() {

        cartViewModel.addToCart(productJewelery)

        val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isNotEmpty()
        assertThat(cartProducts).hasSize(1)

        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {

            val orderedProduct = cartViewModel.cartProducts.getOrAwaitValue().first()
            assertThat(orderedProduct.quantity).isEqualTo(1)

            onView(withId(R.id.item_product_in_cart_increase_quantity_btn)).perform(click())
            assertThat(orderedProduct.quantity).isEqualTo(2)

            onView(withId(R.id.cart_recycler_view))
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.product.name)))))
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.quantity.toString())))))
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.getFormattedPrice())))))

            onView(withId(R.id.item_product_in_cart_decrease_quantity_btn)).perform(click())
            assertThat(orderedProduct.quantity).isEqualTo(1)

            onView(withId(R.id.cart_recycler_view))
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.product.name)))))
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.quantity.toString())))))
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.getFormattedPrice())))))
        }
    }

    @Test
    fun launchCartFragment_clickDecreaseQuantityBtn_whenQuantityIs1_verifyCartWasUpdatedCorrectly() {

        cartViewModel.addToCart(productJewelery)

        var cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isNotEmpty()
        assertThat(cartProducts).hasSize(1)

        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {

            val orderedProduct = cartViewModel.cartProducts.getOrAwaitValue().first()
            assertThat(orderedProduct.quantity).isEqualTo(1)

            onView(withId(R.id.cart_recycler_view))
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.product.name)))))
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.quantity.toString())))))
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.getFormattedPrice())))))

            onView(withId(R.id.item_product_in_cart_decrease_quantity_btn)).perform(click())

            cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
            assertThat(cartProducts).isEmpty()

            onView(withId(R.id.cart_recycler_view))
                .check(matches(not(atPosition(0, hasDescendant(withText(orderedProduct.product.name))))))
                .check(matches(not(atPosition(0, hasDescendant(withText(orderedProduct.quantity.toString()))))))
                .check(matches(not(atPosition(0, hasDescendant(withText(orderedProduct.getFormattedPrice()))))))
        }
    }

    @Test
    fun launchCartFragment_clickAbandonCart_whenCartIsEmpty_navigateUp() {

        val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isEmpty()

        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.cart_fragment)

            onView(withId(R.id.cart_cancel_purchase_btn)).perform(click())

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.cart_fragment)
        }
    }

    @Test
    fun launchCartFragment_clickConfirmPurchase_whenCartIsEmpty_navigateToOrdersFragment() {

        val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isEmpty()

        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.cart_fragment)

            onView(withId(R.id.cart_confirm_purchase_btn)).perform(click())

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.cart_fragment)
        }
    }

    @Test
    fun launchCartFragment_clickConfirmPurchase_whenCartIsNotEmpty_navigateToOrdersFragment() {

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
    fun launchCartFragment_clickAbandonCart_whenCartIsNotEmpty_alertDialogShouldBeVisible() {

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
}