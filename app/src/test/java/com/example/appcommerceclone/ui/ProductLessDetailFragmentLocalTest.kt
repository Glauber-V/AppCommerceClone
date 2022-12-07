package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.appcommerceclone.R
import com.example.appcommerceclone.TestNavHostControllerRule
import com.example.appcommerceclone.di.ProductsModule
import com.example.appcommerceclone.launchFragmentInHiltContainer
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.util.Constants
import com.google.common.truth.Truth.assertThat
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


@UninstallModules(ProductsModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class ProductLessDetailFragmentLocalTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testNavHostControllerRule = TestNavHostControllerRule(R.id.product_less_detail_fragment)

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
            category = Constants.CATEGORY_WOMENS_CLOTHING,
            imageUrl = ""
        )
    }

    @Test
    fun verifyProductWasLoaded_ShouldPass() {
        launchFragmentInHiltContainer<ProductLessDetailFragment>(navHostController = navHostController) {
            productViewModel.selectProduct(product)
        }

        onView(withId(R.id.product_less_detail_name))
            .check(matches(withText(product.name)))

        val formattedPrice = NumberFormat.getCurrencyInstance().format(product.price)
        onView(withId(R.id.product_less_detail_price))
            .check(matches(withText(formattedPrice)))

        onView(withId(R.id.product_less_detail_description))
            .check(matches(withText(product.description)))
    }

    @Test
    fun clickAddToFavorites_shouldNavigateToFavoritesFragment() {
        launchFragmentInHiltContainer<ProductLessDetailFragment>(navHostController = navHostController) {
            productViewModel.selectProduct(product)
        }

        onView(withId(R.id.product_less_detail_add_to_favorites))
            .perform(scrollTo())
            .perform(click())

        assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.favorites_fragment)
    }

    @Test
    fun clickAddToCart_shouldNavigateToCartFragment() {
        launchFragmentInHiltContainer<ProductLessDetailFragment>(navHostController = navHostController) {
            productViewModel.selectProduct(product)
        }

        onView(withId(R.id.product_less_detail_add_to_cart))
            .perform(scrollTo())
            .perform(click())


        assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.cart_fragment)
    }

    @Test
    fun clickBuyNow_snackbarShouldBeVisible() {
        launchFragmentInHiltContainer<ProductLessDetailFragment>(navHostController = navHostController) {
            productViewModel.selectProduct(product)
        }

        onView(withId(R.id.product_less_detail_buy_now))
            .perform(scrollTo())
            .perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.product_detail_thanks_for_purchase)))
    }
}