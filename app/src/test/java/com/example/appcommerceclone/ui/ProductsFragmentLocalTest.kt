package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.product.ProductsRepository
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.ProductsModule
import com.example.appcommerceclone.ui.product.ProductCategories
import com.example.appcommerceclone.ui.product.ProductViewHolder
import com.example.appcommerceclone.ui.product.ProductViewModel
import com.example.appcommerceclone.ui.product.ProductsFragment
import com.example.appcommerceclone.util.*
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

    @Inject
    lateinit var productsRepository: ProductsRepository

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

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
    fun launchProductsFragment_filterListWithCategoryElectronics_undoFilterWithSwipeToRefreshAction() = runTest {
        val products = productViewModel.products.getOrAwaitValue()
        assertThat(products).isEmpty()

        launchFragmentInHiltContainer<ProductsFragment>(navHostController = navHostController, fragmentFactory = factory) {
            productViewModel.assertThatLoadingStateIsEqualTo(LoadingState.LOADING)
            productViewModel.assertThatShimmerVisibilityIsInSyncWithLoadingState()
            advanceUntilIdle()

            productViewModel.assertThatLoadingStateIsEqualTo(LoadingState.SUCCESS)
            productViewModel.assertThatShimmerVisibilityIsInSyncWithLoadingState()
            productViewModel.assertThatProductListHasCorrectSizeAndElements()

            with(ProductCategories.ELECTRONICS) {
                productViewModel.filterProductList(this)
                productViewModel.assertThatProductListHasCorrectSizeAndElements(this)
            }

            (this as SwipeRefreshLayout.OnRefreshListener).onRefresh()

            productViewModel.assertThatProductListHasCorrectSizeAndElements()
        }
    }

    @Test
    fun launchProductsFragment_clickOnProductFromTheList_navigateToProductDetailFragment() = runTest {
        launchFragmentInHiltContainer<ProductsFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.products_fragment)
            productViewModel.assertThatLoadingStateIsEqualTo(LoadingState.LOADING)
            productViewModel.assertThatShimmerVisibilityIsInSyncWithLoadingState()
            advanceUntilIdle()

            productViewModel.assertThatLoadingStateIsEqualTo(LoadingState.SUCCESS)
            productViewModel.assertThatProductListHasCorrectSizeAndElements()

            onView(withId(R.id.products_recycler_view))
                .perform(scrollToPosition<ProductViewHolder>(0))

            onView(withId(R.id.products_recycler_view))
                .perform(actionOnItemAtPosition<ProductViewHolder>(0, click()))

            navHostController.assertThatCurrentDestinationIsNotEqualTo(R.id.products_fragment)
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.product_detail_fragment)
        }
    }
}