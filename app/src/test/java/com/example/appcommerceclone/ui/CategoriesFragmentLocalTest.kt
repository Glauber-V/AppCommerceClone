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
import com.example.appcommerceclone.ui.product.ProductCategories
import com.example.appcommerceclone.ui.product.ProductViewModel
import com.example.appcommerceclone.util.LoadingState
import com.example.appcommerceclone.util.TestMainDispatcherRule
import com.example.appcommerceclone.util.TestNavHostControllerRule
import com.example.appcommerceclone.util.assertThatCurrentDestinationIsEqualTo
import com.example.appcommerceclone.util.assertThatCurrentDestinationIsNotEqualTo
import com.example.appcommerceclone.util.assertThatLoadingStateIsEqualTo
import com.example.appcommerceclone.util.assertThatProductListHasCorrectSizeAndElements
import com.example.appcommerceclone.util.launchFragmentInHiltContainer
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
    fun launchCategoriesFragment_selectCategoryElectronics_navigateToProductsFragment_verifyProductListItemCategory() = runTest {
        productViewModel.updateProductList()
        advanceUntilIdle()

        productViewModel.assertThatLoadingStateIsEqualTo(LoadingState.SUCCESS)
        productViewModel.assertThatProductListHasCorrectSizeAndElements()

        launchFragmentInHiltContainer<CategoriesFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.categories_fragment)

            onView(withId(R.id.product_category_electronics)).perform(click())

            navHostController.assertThatCurrentDestinationIsNotEqualTo(R.id.categories_fragment)
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.products_fragment)

            productViewModel.assertThatProductListHasCorrectSizeAndElements(ProductCategories.ELECTRONICS)
        }
    }
}