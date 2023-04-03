package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productElectronic
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productJewelery
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productMensClothing
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productWomensClothing
import com.example.appcommerceclone.data.product.ProductsRepository
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.ProductsModule
import com.example.appcommerceclone.ui.product.ProductsAdapter
import com.example.appcommerceclone.ui.product.ProductsFragment
import com.example.appcommerceclone.util.*
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_ELECTRONICS
import com.example.appcommerceclone.viewmodels.ProductViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
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
class ProductsFragmentLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testNavHostControllerRule = TestNavHostControllerRule(R.id.products_fragment)

    @get:Rule(order = 2)
    val testMainDispatcherRule = TestMainDispatcherRule()

    @Inject lateinit var productsRepository: ProductsRepository
    @Inject lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var navHostController: TestNavHostController
    private lateinit var productViewModel: ProductViewModel
    private lateinit var factory: TestFragmentFactory

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
        productViewModel = ProductViewModel(productsRepository, dispatcherProvider)
        factory = TestFragmentFactory(productViewModelTest = productViewModel)
    }

    @Test
    fun launchProductsFragment_verifyLayoutVisibility_verifyListIsNotEmpty() = runTest {

        var products = productViewModel.products.getOrAwaitValue()
        assertThat(products).isEmpty()

        launchFragmentInHiltContainer<ProductsFragment>(navHostController = navHostController, fragmentFactory = factory) {

            onView(withId(R.id.products_shimmer))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

            onView(withId(R.id.products_recycler_view))
                .check(matches(withEffectiveVisibility(Visibility.GONE)))

            advanceUntilIdle()

            val isLoading = productViewModel.isLoading.getOrAwaitValue()
            assertThat(isLoading).isFalse()

            products = productViewModel.products.getOrAwaitValue()
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

        var products = productViewModel.products.getOrAwaitValue()
        assertThat(products).isEmpty()

        launchFragmentInHiltContainer<ProductsFragment>(navHostController = navHostController, fragmentFactory = factory) {

            onView(withId(R.id.products_shimmer))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

            onView(withId(R.id.products_recycler_view))
                .check(matches(withEffectiveVisibility(Visibility.GONE)))

            advanceUntilIdle()

            val isLoading = productViewModel.isLoading.getOrAwaitValue()
            assertThat(isLoading).isFalse()

            products = productViewModel.products.getOrAwaitValue()
            assertThat(products).isNotEmpty()
            assertThat(products).hasSize(4)

            productViewModel.selectCategoryAndUpdateProductsList(CATEGORY_NAME_ELECTRONICS)
            products = productViewModel.products.getOrAwaitValue()
            assertThat(products).isNotEmpty()
            assertThat(products).hasSize(1)
            assertThat(products.first().category).isEqualTo(CATEGORY_NAME_ELECTRONICS)

            // This action won't have any effect while using robolectric:
            // onView(withId(R.id.products_swipe_refresh_layout)).perform(swipeDown())
            // https://stackoverflow.com/questions/55517269/android-espresso-testing-swiperefreshlayout-onrefresh-not-been-triggered-on-swip
            productViewModel.updateProductsList()
            advanceUntilIdle()

            products = productViewModel.products.getOrAwaitValue()
            assertThat(products).isNotEmpty()
            assertThat(products).hasSize(4)
            assertThat(products).contains(productJewelery)
            assertThat(products).contains(productElectronic)
            assertThat(products).contains(productMensClothing)
            assertThat(products).contains(productWomensClothing)
        }
    }

    @Test
    fun launchProductsFragment_clickOnProductFromTheList_navigateToProductDetailFragment() = runTest {

        var products = productViewModel.products.getOrAwaitValue()
        assertThat(products).isEmpty()

        launchFragmentInHiltContainer<ProductsFragment>(navHostController = navHostController, fragmentFactory = factory) {

            onView(withId(R.id.products_shimmer))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

            onView(withId(R.id.products_recycler_view))
                .check(matches(withEffectiveVisibility(Visibility.GONE)))

            advanceUntilIdle()

            val isLoading = productViewModel.isLoading.getOrAwaitValue()
            assertThat(isLoading).isFalse()

            products = productViewModel.products.getOrAwaitValue()
            assertThat(products).isNotEmpty()
            assertThat(products).hasSize(4)

            onView(withId(R.id.products_recycler_view))
                .perform(scrollToPosition<ProductsAdapter.ProductViewHolder>(0))

            onView(withId(R.id.products_recycler_view))
                .perform(actionOnItemAtPosition<ProductsAdapter.ProductViewHolder>(1, click()))

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.products_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.product_detail_fragment)
        }
    }
}