package com.example.appcommerceclone.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
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
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productElectronic
import com.example.appcommerceclone.data.product.ProductsRepository
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.ProductsModule
import com.example.appcommerceclone.ui.product.ProductDetailScreen
import com.example.appcommerceclone.util.*
import com.example.appcommerceclone.viewmodels.ProductViewModel
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
class ProductDetailFragmentLocalTest {

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
    fun onProductDetailScreen_notInFullDetail_verifyTransaction() {
        val product = productElectronic
        productViewModel.selectProduct(product)

        val isShowInFullMode = productViewModel.checkIfShouldDisplayInFullDetailMode(product)
        assertThat(isShowInFullMode).isFalse()

        var transactionStatus = false

        with(composeRule) {
            setContent {
                ProductDetailScreen(
                    product = product,
                    isShowFullDetail = isShowInFullMode,
                    onAddToFavorites = { },
                    onBuyNow = { transactionStatus = true },
                    onAddToCart = { }
                )
            }

            onRoot().printToLog("onProductDetailScreen|NotInFullMode")

            onNodeWithText(getStringResource(R.string.product_detail_buy_now))
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()

            onNodeWithText(getStringResource(R.string.product_detail_chip_color_and_size_warning))
                .assertDoesNotExist()

            assertThat(transactionStatus).isTrue()
        }
    }
}