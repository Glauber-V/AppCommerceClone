package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.product.ProductsRepository
import com.example.appcommerceclone.data.product.model.Product
import com.example.appcommerceclone.data.user.UserAuthenticator
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.ProductsModule
import com.example.appcommerceclone.ui.cart.CartViewModel
import com.example.appcommerceclone.ui.favorites.FavoritesViewModel
import com.example.appcommerceclone.ui.order.UserOrdersViewModel
import com.example.appcommerceclone.ui.product.ProductDetailFragment
import com.example.appcommerceclone.ui.product.ProductViewModel
import com.example.appcommerceclone.ui.user.UserViewModel
import com.example.appcommerceclone.util.TestMainDispatcherRule
import com.example.appcommerceclone.util.TestNavHostControllerRule
import com.example.appcommerceclone.util.assertThatCurrentDestinationIsEqualTo
import com.example.appcommerceclone.util.assertThatCurrentDestinationIsNotEqualTo
import com.example.appcommerceclone.util.assertThatThereIsNoCurrentUser
import com.example.appcommerceclone.util.assertThatUserWasLoadedCorrectly
import com.example.appcommerceclone.util.firstUser
import com.example.appcommerceclone.util.formatPrice
import com.example.appcommerceclone.util.launchFragmentInHiltContainer
import com.example.appcommerceclone.util.productElectronic
import com.example.appcommerceclone.util.productJewelry
import com.example.appcommerceclone.util.productMensClothing
import com.example.appcommerceclone.util.productWomensClothing
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
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
import javax.inject.Inject

@UninstallModules(ProductsModule::class, DispatcherModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class ProductDetailFragmentLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testNavHostControllerRule = TestNavHostControllerRule(R.id.product_detail_fragment)

    @get:Rule(order = 2)
    val testMainDispatcherRule = TestMainDispatcherRule()

    @Inject
    lateinit var productsRepository: ProductsRepository

    @Inject
    lateinit var userAuthenticator: UserAuthenticator

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var navHostController: TestNavHostController
    private lateinit var productViewModel: ProductViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var userOrdersViewModel: UserOrdersViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var factory: TestFragmentFactory

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
        productViewModel = ProductViewModel(productsRepository, dispatcherProvider)
        userViewModel = UserViewModel(userAuthenticator, dispatcherProvider)
        favoritesViewModel = FavoritesViewModel()
        userOrdersViewModel = UserOrdersViewModel()
        cartViewModel = CartViewModel()
        factory = TestFragmentFactory(
            productViewModelTest = productViewModel,
            userViewModelTest = userViewModel,
            favoritesViewModelTest = favoritesViewModel,
            userOrdersViewModelTest = userOrdersViewModel,
            cartViewModelTest = cartViewModel
        )
    }

    @Test
    fun launchProductDetailFragment_withUser_withSelectedOptions_clickBuyNow_thankSnackbarIsVisible_navigateToOrdersFragment() = runTest {
        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        userViewModel.assertThatUserWasLoadedCorrectly(firstUser)

        val product = productMensClothing
        productViewModel.selectProduct(product)

        launchFragmentInHiltContainer<ProductDetailFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.product_detail_fragment)

            assertThatProductWasCorrectlyLoaded(product)
            assertThatOptionsAreVisibleAndSelected()

            onView(withId(R.id.product_detail_buy_now))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click())

            onView(withText(getString(R.string.product_detail_thanks_for_purchase)))
                .check(matches(isDisplayed()))

            navHostController.assertThatCurrentDestinationIsNotEqualTo(R.id.product_detail_fragment)
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.orders_fragment)
        }
    }

    @Test
    fun launchProductDetailFragment_withUser_withOptionsNotSelected_clickBuNow_warningSnackbarIsVisible() = runTest {
        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        userViewModel.assertThatUserWasLoadedCorrectly(firstUser)

        val product = productWomensClothing
        productViewModel.selectProduct(product)

        launchFragmentInHiltContainer<ProductDetailFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.product_detail_fragment)

            assertThatProductWasCorrectlyLoaded(product)
            assertThatOptionsAreVisibleButNotSelected()

            onView(withId(R.id.product_detail_buy_now))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click())

            onView(withText(getString(R.string.error_no_color_and_size_selected)))
                .check(matches(isDisplayed()))

            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.product_detail_fragment)
        }
    }

    @Test
    fun launchProductDetailFragment_withUser_noOptionsAvailable_clickBuNow_thankSnackbarIsVisible_navigateToOrdersFragment() = runTest {
        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        userViewModel.assertThatUserWasLoadedCorrectly(firstUser)

        val product = productJewelry
        productViewModel.selectProduct(product)

        launchFragmentInHiltContainer<ProductDetailFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.product_detail_fragment)

            assertThatProductWasCorrectlyLoaded(product)
            assertThatOptionsAreNotVisible()

            onView(withId(R.id.product_detail_buy_now))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click())

            onView(withText(getString(R.string.error_no_color_and_size_selected)))
                .check(doesNotExist())

            onView(withText(getString(R.string.product_detail_thanks_for_purchase)))
                .check(matches(isDisplayed()))

            navHostController.assertThatCurrentDestinationIsNotEqualTo(R.id.product_detail_fragment)
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.orders_fragment)
        }
    }

    @Test
    fun launchProductDetailFragment_withUser_clickAddToFavorites_navigateToFavoritesFragment() = runTest {
        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        userViewModel.assertThatUserWasLoadedCorrectly(firstUser)

        val product = productElectronic
        productViewModel.selectProduct(product)

        launchFragmentInHiltContainer<ProductDetailFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.product_detail_fragment)

            assertThatProductWasCorrectlyLoaded(product)

            onView(withId(R.id.product_detail_add_to_favorites))
                .perform(scrollTo())
                .perform(click())

            navHostController.assertThatCurrentDestinationIsNotEqualTo(R.id.product_detail_fragment)
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.favorites_fragment)
        }
    }

    @Test
    fun launchProductDetailFragment_withoutUser_clickAddToFavorites_navigateToLoginFragment() = runTest {
        userViewModel.assertThatThereIsNoCurrentUser()

        val product = productElectronic
        productViewModel.selectProduct(product)

        launchFragmentInHiltContainer<ProductDetailFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.product_detail_fragment)

            assertThatProductWasCorrectlyLoaded(product)

            onView(withId(R.id.product_detail_add_to_favorites))
                .perform(scrollTo())
                .perform(click())

            navHostController.assertThatCurrentDestinationIsNotEqualTo(R.id.product_detail_fragment)
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.login_fragment)
        }
    }

    @Test
    fun launchProductDetailFragment_clickAddToCart_withSelectedOptions_navigateToCartFragment() {
        val product = productWomensClothing
        productViewModel.selectProduct(product)

        launchFragmentInHiltContainer<ProductDetailFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.product_detail_fragment)

            assertThatProductWasCorrectlyLoaded(product)
            assertThatOptionsAreVisibleAndSelected()

            onView(withId(R.id.product_detail_add_to_cart))
                .perform(scrollTo())
                .perform(click())

            navHostController.assertThatCurrentDestinationIsNotEqualTo(R.id.product_detail_fragment)
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.cart_fragment)
        }
    }

    @Test
    fun launchProductDetailFragment_clickAddToCart_withOptionsNotSelected_warningSnackBarIsVisible() {
        val product = productWomensClothing
        productViewModel.selectProduct(product)

        launchFragmentInHiltContainer<ProductDetailFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.product_detail_fragment)

            assertThatProductWasCorrectlyLoaded(product)
            assertThatOptionsAreVisibleButNotSelected()

            onView(withId(R.id.product_detail_add_to_cart))
                .perform(scrollTo())
                .perform(click())

            onView(withText(R.string.error_no_color_and_size_selected))
                .check(matches(isDisplayed()))

            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.product_detail_fragment)
        }
    }

    @Test
    fun launchProductDetailFragment_clickAddToCart_noOptionsAvailable_navigateToCartFragment() {
        val product = productElectronic
        productViewModel.selectProduct(product)

        launchFragmentInHiltContainer<ProductDetailFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.product_detail_fragment)

            assertThatProductWasCorrectlyLoaded(product)
            assertThatOptionsAreNotVisible()

            onView(withId(R.id.product_detail_add_to_cart))
                .perform(scrollTo())
                .perform(click())

            onView(withText(getString(R.string.error_no_color_and_size_selected)))
                .check(doesNotExist())

            navHostController.assertThatCurrentDestinationIsNotEqualTo(R.id.product_detail_fragment)
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.cart_fragment)
        }
    }

    private fun assertThatProductWasCorrectlyLoaded(product: Product) {
        onView(withId(R.id.product_detail_name))
            .check(matches(withText(product.name)))

        onView(withId(R.id.product_detail_price))
            .check(matches(withText(product.formatPrice())))

        onView(withId(R.id.product_detail_description))
            .check(matches(withText(product.description)))
    }

    private fun assertThatOptionsAreNotVisible() {
        onView(withId(R.id.product_detail_colors_chip_group))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.product_detail_sizes_chip_group))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    private fun assertThatOptionsAreVisibleAndSelected() {
        onView(withId(R.id.product_detail_colors_chip_group))
            .perform(scrollTo())
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.chip_color_1))
            .perform(scrollTo())
            .perform(click())
            .check(matches(isChecked()))

        onView(withId(R.id.chip_color_2))
            .perform(scrollTo())
            .check(matches(not(isChecked())))

        onView(withId(R.id.chip_color_3))
            .perform(scrollTo())
            .check(matches(not(isChecked())))

        onView(withId(R.id.product_detail_sizes_chip_group))
            .perform(scrollTo())
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.chip_size_1))
            .perform(scrollTo())
            .perform(click())
            .check(matches(isChecked()))

        onView(withId(R.id.chip_size_2))
            .perform(scrollTo())
            .check(matches(not(isChecked())))

        onView(withId(R.id.chip_size_3))
            .perform(scrollTo())
            .check(matches(not(isChecked())))
    }

    private fun assertThatOptionsAreVisibleButNotSelected() {
        onView(withId(R.id.product_detail_colors_chip_group))
            .perform(scrollTo())
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.chip_color_1))
            .perform(scrollTo())
            .check(matches(not(isChecked())))

        onView(withId(R.id.chip_color_2))
            .perform(scrollTo())
            .check(matches(not(isChecked())))

        onView(withId(R.id.chip_color_3))
            .perform(scrollTo())
            .check(matches(not(isChecked())))

        onView(withId(R.id.product_detail_sizes_chip_group))
            .perform(scrollTo())
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.chip_size_1))
            .perform(scrollTo())
            .check(matches(not(isChecked())))

        onView(withId(R.id.chip_size_2))
            .perform(scrollTo())
            .check(matches(not(isChecked())))

        onView(withId(R.id.chip_size_3))
            .perform(scrollTo())
            .check(matches(not(isChecked())))
    }
}