package com.example.appcommerceclone.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.product.ProductsRepository
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.ProductsModule
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.ui.product.ProductsScreen
import com.example.appcommerceclone.util.*
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
@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class ProductsFragmentLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testMainDispatcherRule = TestMainDispatcherRule()

    @get:Rule(order = 2)
    val composeRule = createComposeRule()

    @Inject
    lateinit var productsRepository: ProductsRepository

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var productViewModel: ProductViewModel
    private lateinit var isDataLoaded: State<Boolean>
    private lateinit var isLoading: State<Boolean>
    private lateinit var products: State<List<Product>>

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        showSemanticTreeInConsole()
        composeRule.setContent {
            productViewModel = ProductViewModel(productsRepository, dispatcherProvider)
            isLoading = productViewModel.isLoading.observeAsState(initial = false)
            isDataLoaded = productViewModel.isDataLoaded.observeAsState(initial = false)
            products = productViewModel.products.observeAsState(initial = emptyList())
            MaterialTheme {
                ProductsScreen(
                    isLoading = isLoading.value,
                    products = products.value,
                    onProductClicked = {},
                    onRefresh = {}
                )
            }
        }
    }

    @Test
    fun onProductsScreen_verifyProductsAreVisible() = runTest {

        with(composeRule) {

            productViewModel.updateProductList()
            advanceUntilIdle()

            onRoot().printToLog("onProductScreen")

            assertThat(isLoading.value).isFalse()
            assertThat(isDataLoaded.value).isTrue()
            assertThat(products.value).isNotEmpty()
            assertThat(products.value).hasSize(4)
            assertThat(products.value).contains(productJewelry)
            assertThat(products.value).contains(productElectronic)
            assertThat(products.value).contains(productMensClothing)
            assertThat(products.value).contains(productWomensClothing)

            onNodeWithText(productJewelry.name)
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText(productElectronic.name)
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText(productMensClothing.name)
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText(productWomensClothing.name)
                .assertExists()
                .assertIsDisplayed()
        }
    }
}