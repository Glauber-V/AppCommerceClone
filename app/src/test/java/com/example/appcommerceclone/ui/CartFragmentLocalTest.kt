package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.product.FakeProductsRepository.Companion.productJewelery
import com.example.appcommerceclone.data.user.FakeUserAuthenticator.Companion.firstUser
import com.example.appcommerceclone.ui.cart.CartFragment
import com.example.appcommerceclone.util.*
import com.example.appcommerceclone.viewmodels.CartViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
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
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testNavHostControllerRule = TestNavHostControllerRule(R.id.cart_fragment)

    @get:Rule(order = 2)
    var testFragmentFactoryRule = TestFragmentFactoryRule()

    private lateinit var navHostController: TestNavHostController
    private lateinit var cartViewModel: CartViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var factory: TestFragmentFactory

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
        cartViewModel = testFragmentFactoryRule.cartViewModel!!
        userViewModel = testFragmentFactoryRule.userViewModel!!
        factory = testFragmentFactoryRule.factory!!
    }

    @Test
    fun clickIncreaseQuantityBtn_orderedProductQuantityAndPriceShouldReflectThisChange() {
        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {

            cartViewModel.addToCart(productJewelery)

            onView(withId(R.id.cart_increase_quantity)).perform(click())

            val orderedProduct = cartViewModel.getOrderedProduct(productJewelery)

            onView(withId(R.id.ordered_product_name)).check(matches(withText(orderedProduct.product.name)))
            onView(withId(R.id.ordered_product_quantity)).check(matches(withText(orderedProduct.quantity.toString())))
            onView(withId(R.id.ordered_product_price)).check(matches(withText(getFormattedPrice(orderedProduct))))
        }
    }

    @Test
    fun clickDecreaseQuantityBtn_orderedProductQuantityAndPriceShouldReflectThisChange() {
        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {

            cartViewModel.addToCart(productJewelery)

            var orderedProduct = cartViewModel.getOrderedProduct(productJewelery)

            onView(withId(R.id.cart_increase_quantity)).perform(click())

            onView(withId(R.id.ordered_product_name)).check(matches(withText(orderedProduct.product.name)))
            onView(withId(R.id.ordered_product_quantity)).check(matches(withText(orderedProduct.quantity.toString())))
            onView(withId(R.id.ordered_product_price)).check(matches(withText(getFormattedPrice(orderedProduct))))

            onView(withId(R.id.cart_decrease_quantity)).perform(click())

            orderedProduct = cartViewModel.getOrderedProduct(productJewelery)

            onView(withId(R.id.ordered_product_name)).check(matches(withText(orderedProduct.product.name)))
            onView(withId(R.id.ordered_product_quantity)).check(matches(withText(orderedProduct.quantity.toString())))
            onView(withId(R.id.ordered_product_price)).check(matches(withText(getFormattedPrice(orderedProduct))))
        }
    }

    @Test
    fun clickDecreaseQuantityBtn_whenQuantityIs1_orderedProductShouldBeRemoved() {
        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {

            cartViewModel.addToCart(productJewelery)

            onView(withId(R.id.cart_decrease_quantity)).perform(click())

            val cartProducts = cartViewModel.getOrderedProducts()
            assertThat(cartProducts).isEmpty()
        }
    }

    @Test
    fun clickAbandonCart_alertDialogShouldBeVisible() {
        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {

            onView(withId(R.id.cart_cancel_purchase_btn)).perform(click())

            val dialog = ShadowDialog.getLatestDialog()
            assertTrue(dialog.isShowing)

            onView(withText(R.string.cart_dialog_title))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun clickConfirmPurchase_navigateToOrdersFragment() = runTest {

        launch { userViewModel.login(username = firstUser.username, password = firstUser.password) }
        advanceUntilIdle()

        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {

            val result = userViewModel.loggedUser.getOrAwaitValue()
            assertThat(result).isNotNull()

            onView(withId(R.id.cart_confirm_purchase_btn)).perform(click())

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.orders_fragment)
        }
    }
}