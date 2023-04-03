package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.product.ProductsRepository
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.ProductsModule
import com.example.appcommerceclone.ui.product.CategoriesFragment
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
class CategoriesFragmentLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testNavHostControllerRule = TestNavHostControllerRule(R.id.categories_fragment)

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
    fun selectCategory_navigateToProductsFragment_verifyTheCategoryOfTheProductMatchesSelectedCategory() = runTest {

        productViewModel.updateProductsList()
        advanceUntilIdle()

        var products = productViewModel.products.getOrAwaitValue()
        assertThat(products).isNotEmpty()
        assertThat(products).hasSize(4)

        launchFragmentInHiltContainer<CategoriesFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.categories_fragment)

            onView(withId(R.id.product_category_electronics)).perform(click())

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.products_fragment)

            products = productViewModel.products.getOrAwaitValue()
            assertThat(products).isNotEmpty()
            assertThat(products).hasSize(1)
            assertThat(products.first().category).isEqualTo(CATEGORY_NAME_ELECTRONICS)
        }
    }
}