package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.TestNavHostControllerRule
import com.example.appcommerceclone.di.ProductsModule
import com.example.appcommerceclone.getOrAwaitValue
import com.example.appcommerceclone.launchFragmentInHiltContainer
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.ui.product.ProductDetailFragment
import com.example.appcommerceclone.util.Constants
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_WOMENS_CLOTHING
import com.example.appcommerceclone.util.getFormattedPrice
import com.example.appcommerceclone.util.getOrderedProduct
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

@UninstallModules(ProductsModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class ProductDetailFragmentLocalTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testNavHostControllerRule = TestNavHostControllerRule(R.id.product_detail_fragment)

    private lateinit var navHostController: TestNavHostController
    private lateinit var product: Product

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
        product = Product(
            id = 4,
            name = "Product D4",
            price = 20.0,
            description = "DDD",
            category = CATEGORY_NAME_WOMENS_CLOTHING,
            imageUrl = ""
        )
    }

    @Test
    fun verifyProductWasLoaded_ShouldPass() {
        launchFragmentInHiltContainer<ProductDetailFragment>(navHostController = navHostController) {
            productViewModel.selectProduct(product)
        }

        onView(withId(R.id.product_detail_name))
            .check(matches(withText(product.name)))

        onView(withId(R.id.product_detail_price))
            .check(matches(withText(getFormattedPrice(product))))

        onView(withId(R.id.product_detail_description))
            .check(matches(withText(product.description)))
    }

    @Test
    fun clickAddToFavorites_shouldNavigateToFavoritesFragment() {
        launchFragmentInHiltContainer<ProductDetailFragment>(navHostController = navHostController) {
            productViewModel.selectProduct(product)

            onView(withId(R.id.product_detail_add_to_favorites))
                .perform(scrollTo())
                .perform(click())

            val favorites = favoritesViewModel.favorites.getOrAwaitValue()
            assertThat(favorites).contains(product)

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.favorites_fragment)
        }
    }

    @Test
    fun clickAddToCart_whenNotInFullMode_shouldNavigateToCartFragment() {
        launchFragmentInHiltContainer<ProductDetailFragment>(navHostController = navHostController) {
            val productFromDifferentCategory = product.copy(category = Constants.CATEGORY_NAME_JEWELRY)
            productViewModel.selectProduct(productFromDifferentCategory)

            onView(withId(R.id.product_detail_colors_chip_group))
                .check(matches(withEffectiveVisibility(Visibility.GONE)))

            onView(withId(R.id.product_detail_sizes_chip_group))
                .check(matches(withEffectiveVisibility(Visibility.GONE)))

            onView(withId(R.id.product_detail_add_to_cart))
                .perform(scrollTo())
                .perform(click())

            val orderedProducts = cartViewModel.cartProducts.getOrAwaitValue()
            val orderedProduct = orderedProducts.getOrderedProduct(productFromDifferentCategory)
            assertThat(orderedProducts).contains(orderedProduct)

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.cart_fragment)
        }
    }

    @Test
    fun clickAddToCart_whenInFullMode_shouldNavigateToCartFragment() {
        launchFragmentInHiltContainer<ProductDetailFragment>(navHostController = navHostController) {
            productViewModel.selectProduct(product)
        }

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

        assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.cart_fragment)
    }

    @Test
    fun clickBuyNow_whenInFullMode_snackbarShouldBeVisible() {
        launchFragmentInHiltContainer<ProductDetailFragment>(navHostController = navHostController) {
            productViewModel.selectProduct(product)
        }

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