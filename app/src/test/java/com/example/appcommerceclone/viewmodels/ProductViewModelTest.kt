package com.example.appcommerceclone.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.data.dispatcher.FakeDispatcherProvider
import com.example.appcommerceclone.data.product.FakeProductsProvider
import com.example.appcommerceclone.data.product.FakeProductsRepository
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_ELECTRONICS
import com.example.appcommerceclone.util.TestMainDispatcherRule
import com.example.appcommerceclone.util.getOrAwaitValue
import com.example.appcommerceclone.util.productElectronic
import com.example.appcommerceclone.util.productJewelry
import com.example.appcommerceclone.util.productMensClothing
import com.example.appcommerceclone.util.productWomensClothing
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
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
        productsRepository = FakeProductsRepository(productsProvider, dispatcherProvider)
        productViewModel = ProductViewModel(productsRepository, dispatcherProvider)
    }

    @Test
    fun updateProductsList_withCategorySelected_verifyAllProductsHaveSameCategory() = runTest {
        productViewModel.updateProductsList(CATEGORY_NAME_ELECTRONICS)
        advanceUntilIdle()

        val products = productViewModel.products.getOrAwaitValue()
        assertThat(products.size).isEqualTo(1)
        assertThat(products.first().category).isEqualTo(CATEGORY_NAME_ELECTRONICS)
    }

    @Test
    fun updateProductsList_noCategorySelected_verifyAllProductsHaveSameCategory() = runTest {
        productViewModel.updateProductsList()
        advanceUntilIdle()

        val products = productViewModel.products.getOrAwaitValue()
        assertThat(products.size).isEqualTo(4)
        assertThat(products).contains(productJewelry)
        assertThat(products).contains(productElectronic)
        assertThat(products).contains(productMensClothing)
        assertThat(products).contains(productWomensClothing)
    }

    @Test
    fun selectProduct_confirmSelectedProduct() {
        productViewModel.selectProduct(productJewelry)

        val selectedProduct = productViewModel.selectedProduct.getOrAwaitValue()
        assertThat(selectedProduct).isEqualTo(productJewelry)
    }

    @Test
    fun selectProduct_onSelectedProductFinish_selectedProductsIsNull() {
        productViewModel.selectProduct(productJewelry)

        var selectedProduct = productViewModel.selectedProduct.getOrAwaitValue()
        assertThat(selectedProduct).isEqualTo(productJewelry)

        productViewModel.onSelectedProductFinish()

        selectedProduct = productViewModel.selectedProduct.getOrAwaitValue()
        assertThat(selectedProduct).isNull()
    }
}