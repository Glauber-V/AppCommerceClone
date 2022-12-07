package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.TestNavHostControllerRule
import com.example.appcommerceclone.di.ProductsModule
import com.example.appcommerceclone.di.UsersModule
import com.example.appcommerceclone.getOrAwaitValue
import com.example.appcommerceclone.launchFragmentInHiltContainer
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.util.getOrderedProduct
import com.example.appcommerceclone.util.noConstraintsClick
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
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

@UninstallModules(ProductsModule::class, UsersModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class CartFragmentLocalTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testNavHostControllerRule = TestNavHostControllerRule(R.id.cart_fragment)

    private lateinit var navHostController: TestNavHostController

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
    }

    @Test
    fun clickIncreaseQuantityBtn_orderedProductQuantityShouldBe2() {
        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController) {

            val product = Product()
            cartViewModel.addToCart(product)

            onView(withId(R.id.cart_increase_quantity))
                .perform(noConstraintsClick())

            val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
            val orderedProduct = cartProducts.getOrderedProduct(product)

            assertThat(orderedProduct.quantity).isEqualTo(2)
        }
    }

    @Test
    fun clickDecreaseQuantityBtn_orderedProductShouldBeRemoved() {
        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController) {

            val product = Product()
            cartViewModel.addToCart(product)

            onView(withId(R.id.cart_decrease_quantity))
                .perform(noConstraintsClick())

            val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
            assertThat(cartProducts).isEmpty()
        }
    }

    @Test
    fun clickAbandonCart_alertDialogShouldBeVisible() {
        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController)

        onView(withId(R.id.cart_cancel_purchase_btn))
            .perform(click())

        val dialog = ShadowDialog.getLatestDialog()
        assertTrue(dialog.isShowing)

        onView(withText(R.string.cart_dialog_title))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickConfirm_navigateToOrdersFragment() = runTest {
        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController) {
            launch { userViewModel.login("Orisa", "321") }
            advanceUntilIdle()

            val result = userViewModel.loggedUser.getOrAwaitValue()
            assertThat(result).isNotNull()
        }

        onView(withId(R.id.cart_confirm_purchase_btn))
            .perform(click())

        assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.orders_fragment)
    }
}