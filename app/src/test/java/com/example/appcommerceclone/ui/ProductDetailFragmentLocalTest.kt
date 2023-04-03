package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productElectronic
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productWomensClothing
import com.example.appcommerceclone.data.product.ProductsRepository
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.ProductsModule
import com.example.appcommerceclone.ui.product.ProductDetailFragment
import com.example.appcommerceclone.util.*
import com.example.appcommerceclone.viewmodels.CartViewModel
import com.example.appcommerceclone.viewmodels.FavoritesViewModel
import com.example.appcommerceclone.viewmodels.ProductViewModel
import com.google.common.truth.Truth.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.text.NumberFormat
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

    @Inject lateinit var productsRepository: ProductsRepository
    @Inject lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var navHostController: TestNavHostController
    private lateinit var productViewModel: ProductViewModel
    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var factory: TestFragmentFactory

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
        productViewModel = ProductViewModel(productsRepository, dispatcherProvider)
        favoritesViewModel = FavoritesViewModel()
        cartViewModel = CartViewModel()
        factory = TestFragmentFactory(
            productViewModelTest = productViewModel,
            favoritesViewModelTest = favoritesViewModel,
            cartViewModelTest = cartViewModel
        )
    }

    @Test
    fun launchProductDetailFragment_notInFullMode_verifyProductWasLoaded() {

        val product = productElectronic
        productViewModel.selectProduct(product)

        launchFragmentInHiltContainer<ProductDetailFragment>(navHostController = navHostController, fragmentFactory = factory) {

            onView(withId(R.id.product_detail_name))
                .check(matches(withText(product.name)))

            onView(withId(R.id.product_detail_colors_chip_group))
                .check(matches(withEffectiveVisibility(Visibility.GONE)))

            onView(withId(R.id.product_detail_sizes_chip_group))
                .check(matches(withEffectiveVisibility(Visibility.GONE)))

            val formattedPrice = NumberFormat.getCurrencyInstance().format(product.price)
            onView(withId(R.id.product_detail_price))
                .check(matches(withText(formattedPrice)))

            onView(withId(R.id.product_detail_description))
                .check(matches(withText(product.description)))
        }
    }

    @Test
    fun launchProductDetailFragment_notInFullMode_clickAddToFavorites_navigateToFavoritesFragment() {

        val product = productElectronic
        productViewModel.selectProduct(product)

        launchFragmentInHiltContainer<ProductDetailFragment>(navHostController = navHostController, fragmentFactory = factory) {

            onView(withId(R.id.product_detail_add_to_favorites))
                .perform(scrollTo())
                .perform(click())

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.product_detail_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.favorites_fragment)

            val favorites = favoritesViewModel.favorites.getOrAwaitValue()
            assertThat(favorites).isNotEmpty()
            assertThat(favorites).hasSize(1)
            assertThat(favorites).contains(product)
        }
    }

    @Test
    fun launchProductDetailFragment_notInFullMode_clickAddToCart_navigateToCartFragment() {

        val product = productElectronic
        productViewModel.selectProduct(product)

        launchFragmentInHiltContainer<ProductDetailFragment>(navHostController = navHostController, fragmentFactory = factory) {

            onView(withId(R.id.product_detail_colors_chip_group))
                .check(matches(withEffectiveVisibility(Visibility.GONE)))

            onView(withId(R.id.product_detail_sizes_chip_group))
                .check(matches(withEffectiveVisibility(Visibility.GONE)))

            onView(withId(R.id.product_detail_add_to_cart))
                .perform(scrollTo())
                .perform(click())

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.product_detail_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.cart_fragment)

            val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
            val orderedProduct = cartProducts.first()
            assertThat(cartProducts).isNotEmpty()
            assertThat(cartProducts).hasSize(1)
            assertThat(cartProducts).contains(orderedProduct)
        }
    }

    @Test
    fun launchProductDetailFragment_inFullMode_verifyProductWasLoaded() {

        val product = productWomensClothing
        productViewModel.selectProduct(product)

        launchFragmentInHiltContainer<ProductDetailFragment>(navHostController = navHostController, fragmentFactory = factory) {

            onView(withId(R.id.product_detail_name))
                .check(matches(withText(product.name)))

            onView(withId(R.id.product_detail_colors_chip_group))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

            onView(withId(R.id.product_detail_sizes_chip_group))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

            val formattedPrice = NumberFormat.getCurrencyInstance().format(product.price)
            onView(withId(R.id.product_detail_price))
                .check(matches(withText(formattedPrice)))

            onView(withId(R.id.product_detail_description))
                .check(matches(withText(product.description)))
        }
    }

    @Test
    fun launchProductDetailFragment_inFullMode_clickAddToCart_navigateToCartFragment() {

        val product = productWomensClothing
        productViewModel.selectProduct(product)

        launchFragmentInHiltContainer<ProductDetailFragment>(navHostController = navHostController, fragmentFactory = factory) {

            onView(withId(R.id.product_detail_name))
                .check(matches(withText(product.name)))

            onView(withId(R.id.product_detail_colors_chip_group))
                .perform(scrollTo())
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

            onView(withId(R.id.chip_color_1))
                .perform(scrollTo())
                .perform(click())
                .check(matches(isChecked()))

            onView(withId(R.id.product_detail_sizes_chip_group))
                .perform(scrollTo())
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

            onView(withId(R.id.chip_size_1))
                .perform(scrollTo())
                .perform(click())
                .check(matches(isChecked()))

            onView(withId(R.id.product_detail_add_to_cart))
                .perform(scrollTo())
                .perform(click())

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.product_detail_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.cart_fragment)

            val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
            val orderedProduct = cartProducts.first()
            assertThat(cartProducts).isNotEmpty()
            assertThat(cartProducts).hasSize(1)
            assertThat(cartProducts).contains(orderedProduct)
        }
    }

    @Test
    fun launchProductDetailFragment_inFullMode_clickBuyNow_snackbarShouldBeVisible() {

        val product = productWomensClothing
        productViewModel.selectProduct(product)

        launchFragmentInHiltContainer<ProductDetailFragment>(navHostController = navHostController, fragmentFactory = factory) {

            onView(withId(R.id.product_detail_name))
                .check(matches(withText(product.name)))

            onView(withId(R.id.product_detail_colors_chip_group))
                .perform(scrollTo())
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

            onView(withId(R.id.chip_color_1))
                .perform(scrollTo())
                .perform(click())
                .check(matches(isChecked()))

            onView(withId(R.id.product_detail_sizes_chip_group))
                .perform(scrollTo())
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

            onView(withId(R.id.chip_size_1))
                .perform(scrollTo())
                .perform(click())
                .check(matches(isChecked()))

            onView(withId(R.id.product_detail_buy_now))
                .perform(scrollTo())
                .perform(click())

            onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.product_detail_thanks_for_purchase)))
        }
    }
}