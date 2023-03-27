package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.ui.product.ProductsAdapter
import com.example.appcommerceclone.ui.product.ProductsFragment
import com.example.appcommerceclone.util.*
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_ELECTRONICS
import com.example.appcommerceclone.viewmodels.ProductViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class ProductsFragmentLocalTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testNavHostControllerRule = TestNavHostControllerRule(R.id.products_fragment)

    @get:Rule(order = 2)
    var testFragmentFactoryRule = TestFragmentFactoryRule()

    private lateinit var navHostController: TestNavHostController
    private lateinit var productViewModel: ProductViewModel
    private lateinit var factory: TestFragmentFactory

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
        productViewModel = testFragmentFactoryRule.productViewModel!!
        factory = testFragmentFactoryRule.factory!!
    }

    @Test
    fun launchProductsFragment_shimmerVisibilityGone_recyclerviewVisibilityVisible() {
        launchFragmentInHiltContainer<ProductsFragment>(navHostController = navHostController, fragmentFactory = factory) {

            val products = productViewModel.products.getOrAwaitValue()
            assertThat(products).isNotEmpty()
            assertThat(products).hasSize(4)

            onView(withId(R.id.products_shimmer))
                .check(matches(withEffectiveVisibility(Visibility.GONE)))

            onView(withId(R.id.products_recycler_view))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        }
    }

    @Test
    fun launchProductsFragment_reloadListWithSelectedCategory_verifySwipeToRefreshResetsList() = runTest {
        launchFragmentInHiltContainer<ProductsFragment>(navHostController = navHostController, fragmentFactory = factory) {

            val selectedCategory = CATEGORY_NAME_ELECTRONICS

            var products = productViewModel.products.getOrAwaitValue()
            assertThat(products).isNotEmpty()
            assertThat(products).hasSize(4)

            productViewModel.selectCategoryAndUpdateProductsList(selectedCategory)
            products = productViewModel.products.getOrAwaitValue()
            assertThat(products).isNotEmpty()
            assertThat(products).hasSize(1)
            assertThat(products.first().category).isEqualTo(selectedCategory)

            // This action won't have any effect while using robolectric: onView(withId(R.id.products_swipe_refresh_layout)).perform(swipeDown())
            // https://stackoverflow.com/questions/55517269/android-espresso-testing-swiperefreshlayout-onrefresh-not-been-triggered-on-swip
            productViewModel.updateProductsList()
            advanceUntilIdle()

            products = productViewModel.products.getOrAwaitValue()
            assertThat(products).hasSize(4)
        }
    }

    @Test
    fun clickOnProductFromTheList_navigateToProductDetailFragment() {
        launchFragmentInHiltContainer<ProductsFragment>(navHostController = navHostController, fragmentFactory = factory) {

            onView(withId(R.id.products_recycler_view))
                .perform(scrollToPosition<ProductsAdapter.ProductViewHolder>(0))

            onView(withId(R.id.products_recycler_view))
                .perform(actionOnItemAtPosition<ProductsAdapter.ProductViewHolder>(1, click()))

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.product_detail_fragment)
        }
    }
}