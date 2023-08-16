package com.example.appcommerceclone.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollToIndexAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.swipeDown
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.LoadingState
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.product.ProductsRepository
import com.example.appcommerceclone.data.product.model.Product
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.ProductsModule
import com.example.appcommerceclone.ui.product.ProductViewModel
import com.example.appcommerceclone.ui.product.ProductsScreen
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
@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class ProductsScreenLocalTest {

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

    private lateinit var loadingState: State<LoadingState>
    private lateinit var products: State<List<Product>>

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        showSemanticTreeInConsole()
        composeRule.setContent {
            productViewModel = ProductViewModel(productsRepository, dispatcherProvider)
            loadingState = productViewModel.loadingState.observeAsState(initial = LoadingState.NOT_STARTED)
            products = productViewModel.products.observeAsState(initial = emptyList())
            MaterialTheme {
                ProductsScreen(
                    loadingState = loadingState.value,
                    products = products.value,
                    onProductClicked = {},
                    onRefresh = { productViewModel.updateProductList() }
                )
            }
        }
    }

    @Test
    fun onProductsScreen_noProducts_swipeToRefreshProductList_verifyProductsAreVisible() = runTest {
        with(composeRule) {
            onRoot().printToLog("onProductScreen|NoProducts")

            assertThat(loadingState.value).isEqualTo(LoadingState.NOT_STARTED)
            assertThat(products.value).isEmpty()

            onNodeWithText(productJewelry.name).assertDoesNotExist()
            onNodeWithText(productElectronic.name).assertDoesNotExist()
            onNodeWithText(productMensClothing.name).assertDoesNotExist()
            onNodeWithText(productWomensClothing.name).assertDoesNotExist()

            onNode(hasScrollToIndexAction())
                .assertExists()
                .assertIsDisplayed()
                .performTouchInput { swipeDown(startY = topCenter.y, endY = bottomCenter.y) }

            advanceUntilIdle()

            onRoot().printToLog("onProductScreen|WithProducts")

            assertThat(loadingState.value).isEqualTo(LoadingState.SUCCESS)
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