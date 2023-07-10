package com.example.appcommerceclone.ui

import androidx.activity.ComponentActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.product.ProductsRepository
import com.example.appcommerceclone.data.product.model.Product
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.ProductsModule
import com.example.appcommerceclone.ui.product.CategoriesScreen
import com.example.appcommerceclone.ui.product.ProductViewModel
import com.example.appcommerceclone.util.TestMainDispatcherRule
import com.example.appcommerceclone.util.getStringResource
import com.example.appcommerceclone.util.productElectronic
import com.example.appcommerceclone.util.productJewelry
import com.example.appcommerceclone.util.productMensClothing
import com.example.appcommerceclone.util.productWomensClothing
import com.example.appcommerceclone.util.showSemanticTreeInConsole
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
class CategoriesScreenLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testMainDispatcherRule = TestMainDispatcherRule()

    @get:Rule(order = 2)
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Inject
    lateinit var productsRepository: ProductsRepository

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var productViewModel: ProductViewModel
    private lateinit var products: State<List<Product>>

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        showSemanticTreeInConsole()
        composeRule.setContent {
            MaterialTheme {
                productViewModel = ProductViewModel(productsRepository, dispatcherProvider)
                products = productViewModel.products.observeAsState(initial = emptyList())
                CategoriesScreen(onProductCategorySelected = { productViewModel.filterProductList(it) })
            }
        }
    }

    @Test
    fun onCategoriesScreen_selectCategory_verifyProductListHasSize1() = runTest {

        productViewModel.updateProductList()
        advanceUntilIdle()

        assertThat(products.value).hasSize(4)
        assertThat(products.value).contains(productJewelry)
        assertThat(products.value).contains(productElectronic)
        assertThat(products.value).contains(productMensClothing)
        assertThat(products.value).contains(productWomensClothing)

        with(composeRule) {
            onRoot().printToLog("onCategoriesScreen")

            onNodeWithText(getStringResource(R.string.category_name_women_s_clothing))
                .assertExists()
                .assertIsDisplayed()
                .performClick()

            assertThat(products.value).hasSize(1)
            assertThat(products.value).contains(productWomensClothing)
        }
    }
}