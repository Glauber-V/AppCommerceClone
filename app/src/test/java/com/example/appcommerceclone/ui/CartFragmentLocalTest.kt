package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.user.UserAuthenticator
import com.example.appcommerceclone.ui.cart.CartFragment
import com.example.appcommerceclone.ui.cart.CartViewModel
import com.example.appcommerceclone.ui.order.UserOrdersViewModel
import com.example.appcommerceclone.ui.user.UserViewModel
import com.example.appcommerceclone.util.TestMainDispatcherRule
import com.example.appcommerceclone.util.TestNavHostControllerRule
import com.example.appcommerceclone.util.assertThatCurrentDestinationIsEqualTo
import com.example.appcommerceclone.util.assertThatCurrentDestinationIsNotEqualTo
import com.example.appcommerceclone.util.atPosition
import com.example.appcommerceclone.util.firstUser
import com.example.appcommerceclone.util.formatPrice
import com.example.appcommerceclone.util.getOrAwaitValue
import com.example.appcommerceclone.util.launchFragmentInHiltContainer
import com.example.appcommerceclone.util.productJewelry
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowDialog
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class CartFragmentLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testNavHostControllerRule = TestNavHostControllerRule(R.id.cart_fragment)

    @get:Rule(order = 2)
    val testMainDispatcherRule = TestMainDispatcherRule()

    @Inject
    lateinit var userAuthenticator: UserAuthenticator

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var navHostController: TestNavHostController
    private lateinit var cartViewModel: CartViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var userOrdersViewModel: UserOrdersViewModel
    private lateinit var factory: TestFragmentFactory

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
        cartViewModel = CartViewModel()
        userViewModel = UserViewModel(userAuthenticator, dispatcherProvider)
        userOrdersViewModel = UserOrdersViewModel()
        factory = TestFragmentFactory(
            cartViewModelTest = cartViewModel,
            userViewModelTest = userViewModel,
            userOrdersViewModelTest = userOrdersViewModel
        )
    }

    @Test
    fun launchCartFragment_clickIncreaseQuantityBtn_verifyCartWasUpdatedCorrectly() {
        cartViewModel.addToCart(productJewelry)

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
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.formatPrice())))))
        }
    }

    @Test
    fun launchCartFragment_clickDecreaseQuantityBtn_verifyCartWasUpdatedCorrectly() {
        cartViewModel.addToCart(productJewelry)

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
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.formatPrice())))))

            onView(withId(R.id.item_product_in_cart_decrease_quantity_btn)).perform(click())
            assertThat(orderedProduct.quantity).isEqualTo(1)

            onView(withId(R.id.cart_recycler_view))
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.product.name)))))
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.quantity.toString())))))
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.formatPrice())))))
        }
    }

    @Test
    fun launchCartFragment_clickDecreaseQuantityBtn_whenQuantityIs1_verifyCartWasUpdatedCorrectly() {
        cartViewModel.addToCart(productJewelry)

        var cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isNotEmpty()
        assertThat(cartProducts).hasSize(1)

        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {
            val orderedProduct = cartViewModel.cartProducts.getOrAwaitValue().first()
            assertThat(orderedProduct.quantity).isEqualTo(1)

            onView(withId(R.id.cart_recycler_view))
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.product.name)))))
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.quantity.toString())))))
                .check(matches(atPosition(0, hasDescendant(withText(orderedProduct.formatPrice())))))

            onView(withId(R.id.item_product_in_cart_decrease_quantity_btn)).perform(click())

            cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
            assertThat(cartProducts).isEmpty()

            onView(withId(R.id.cart_recycler_view))
                .check(matches(not(atPosition(0, hasDescendant(withText(orderedProduct.product.name))))))
                .check(matches(not(atPosition(0, hasDescendant(withText(orderedProduct.quantity.toString()))))))
                .check(matches(not(atPosition(0, hasDescendant(withText(orderedProduct.formatPrice()))))))
        }
    }

    @Test
    fun launchCartFragment_clickConfirmPurchase_whenCartIsNotEmpty_withUser_navigateToOrdersFragment() = runTest {
        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        val user = userViewModel.currentUser.getOrAwaitValue()
        assertThat(user).isNotNull()
        assertThat(user).isEqualTo(firstUser)

        cartViewModel.addToCart(productJewelry)

        val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isNotEmpty()
        assertThat(cartProducts).hasSize(1)

        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.cart_fragment)

            onView(withId(R.id.cart_confirm_purchase_btn)).perform(click())

            navHostController.assertThatCurrentDestinationIsNotEqualTo(R.id.cart_fragment)
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.orders_fragment)
        }
    }

    @Test
    fun launchCartFragment_clickConfirmPurchase_whenCartIsNotEmpty_withoutUser_navigateToOrdersFragment() {
        val user = userViewModel.currentUser.getOrAwaitValue()
        assertThat(user).isNull()

        cartViewModel.addToCart(productJewelry)

        val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isNotEmpty()
        assertThat(cartProducts).hasSize(1)

        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.cart_fragment)

            onView(withId(R.id.cart_confirm_purchase_btn)).perform(click())

            navHostController.assertThatCurrentDestinationIsNotEqualTo(R.id.cart_fragment)
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.login_fragment)
        }
    }

    @Test
    fun launchCartFragment_clickAbandonCart_whenCartIsNotEmpty_alertDialogShouldBeVisible() {
        cartViewModel.addToCart(productJewelry)

        var cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isNotEmpty()
        assertThat(cartProducts).hasSize(1)

        launchFragmentInHiltContainer<CartFragment>(navHostController = navHostController, fragmentFactory = factory) {
            onView(withId(R.id.cart_cancel_purchase_btn)).perform(click())

            val dialog = ShadowDialog.getLatestDialog()
            assertThat(dialog.isShowing).isTrue()

            onView(withText(R.string.cart_dialog_title))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))

            onView(withId(R.id.cart_dialog_positive)).perform(click())

            cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
            assertThat(cartProducts).isEmpty()
        }
    }
}