package com.example.appcommerceclone.ui

import androidx.activity.ComponentActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.printToLog
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.product.ProductsRepository
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.ProductsModule
import com.example.appcommerceclone.ui.product.ProductColors
import com.example.appcommerceclone.ui.product.ProductDetailScreen
import com.example.appcommerceclone.ui.product.ProductSizes
import com.example.appcommerceclone.ui.product.ProductViewModel
import com.example.appcommerceclone.util.*
import com.google.common.truth.Truth.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@UninstallModules(ProductsModule::class, DispatcherModule::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class ProductDetailScreenLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Inject
    lateinit var productsRepository: ProductsRepository

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var productViewModel: ProductViewModel

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        showSemanticTreeInConsole()
        productViewModel = ProductViewModel(productsRepository, dispatcherProvider)
    }

    @Test
    fun onProductDetailScreen_noOptions_noOptionNotSelectedSnackbar() {

        with(composeRule) {
            setContent {
                MaterialTheme {
                    ProductDetailScreen(
                        product = productElectronic,
                        onAddToFavorites = {},
                        onBuyNow = {},
                        onAddToCart = {}
                    )
                }
            }

            onRoot().printToLog("onProductDetailScreen|NoOptions")

            onNodeWithText(getStringResource(R.string.product_detail_buy_now))
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()

            onNodeWithText(getStringResource(R.string.product_detail_chip_color_and_size_warning))
                .assertDoesNotExist()
        }
    }

    @Test
    fun onProductDetailScreen_withOptions_noOptionNotSelectedSnackbar() {

        val snackbar = SnackbarHostState()

        with(composeRule) {
            setContent {
                MaterialTheme {
                    ProductDetailScreen(
                        product = productMensClothing,
                        onAddToFavorites = {},
                        onBuyNow = {},
                        onAddToCart = {},
                        snackbarHostState = snackbar
                    )
                }
            }

            onRoot().printToLog("onProductDetailScreen|WithOptions")

            onNodeWithText(getStringResource(R.string.product_detail_buy_now))
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()

            onNodeWithText(getStringResource(R.string.product_detail_chip_color_and_size_warning))
                .assertExists()

            onRoot().printToLog("onProductDetailScreen|WithOptions|NoOptionSelected")

            onNodeWithTag(ProductColors.BLUE.name)
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()

            onNodeWithTag(ProductSizes.P.name)
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()

            snackbar.currentSnackbarData?.dismiss()
            onNodeWithText(getStringResource(R.string.product_detail_buy_now))
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()

            onNodeWithText(getStringResource(R.string.product_detail_chip_color_and_size_warning))
                .assertDoesNotExist()

            onNodeWithText(getStringResource(R.string.product_detail_chip_color_warning))
                .assertDoesNotExist()

            onNodeWithText(getStringResource(R.string.product_detail_chip_size_warning))
                .assertDoesNotExist()
        }
    }
}