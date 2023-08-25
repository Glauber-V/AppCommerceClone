package com.example.appcommerceclone.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.data.dispatcher.FakeDispatcherProvider
import com.example.appcommerceclone.data.product.FakeProductsProvider
import com.example.appcommerceclone.data.product.FakeProductsRepository
import com.example.appcommerceclone.ui.product.ProductCategories
import com.example.appcommerceclone.ui.product.ProductViewModel
import com.example.appcommerceclone.util.LoadingState
import com.example.appcommerceclone.util.TestMainDispatcherRule
import com.example.appcommerceclone.util.assertThatLoadingStateIsEqualTo
import com.example.appcommerceclone.util.assertThatProductListHasCorrectSizeAndElements
import com.example.appcommerceclone.util.getOrAwaitValue
import com.example.appcommerceclone.util.productJewelry
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {

    @get:Rule(order = 0)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 1)
    val testMainDispatcherRule = TestMainDispatcherRule()

    private lateinit var productsProvider: FakeProductsProvider
    private lateinit var dispatcherProvider: FakeDispatcherProvider
    private lateinit var productsRepository: FakeProductsRepository
    private lateinit var productViewModel: ProductViewModel

    @Before
    fun setup() {
        productsProvider = FakeProductsProvider()
        dispatcherProvider = FakeDispatcherProvider()
        productsRepository = FakeProductsRepository(productsProvider)
        productViewModel = ProductViewModel(productsRepository, dispatcherProvider)
    }

    @Test
    fun updateProductList_verifyProductListSize_filterListWithElectronicCategory_verifyElementsCategory() = runTest {
        productViewModel.assertThatLoadingStateIsEqualTo(LoadingState.NOT_STARTED)

        productViewModel.updateProductList()
        advanceUntilIdle()

        productViewModel.assertThatLoadingStateIsEqualTo(LoadingState.SUCCESS)
        productViewModel.assertThatProductListHasCorrectSizeAndElements()

        with(ProductCategories.ELECTRONICS) {
            productViewModel.filterProductList(this)
            productViewModel.assertThatProductListHasCorrectSizeAndElements(this)
        }
    }

    @Test
    fun selectAProduct_confirmSelectedProduct_callOnSelectedProductFinish_selectedProductsShouldBeNull() {
        productViewModel.selectProduct(productJewelry)

        var selectedProduct = productViewModel.selectedProduct.getOrAwaitValue()
        assertThat(selectedProduct).isEqualTo(productJewelry)

        productViewModel.onSelectedProductFinish()

        selectedProduct = productViewModel.selectedProduct.getOrAwaitValue()
        assertThat(selectedProduct).isNull()
    }
}