package com.example.appcommerceclone.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.data.product.FakeProductsRepository
import com.example.appcommerceclone.getOrAwaitValue
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_ELECTRONICS
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_JEWELRY
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeProductsRepository: FakeProductsRepository
    private lateinit var productViewModel: ProductViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        fakeProductsRepository = FakeProductsRepository()
        productViewModel = ProductViewModel(fakeProductsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `select a category and verify if all products in the list belongs to the same category`() = runTest {
        productViewModel.updateProductsList(CATEGORY_NAME_ELECTRONICS)
        advanceUntilIdle()

        val products = productViewModel.products.getOrAwaitValue()

        assertThat(products.size).isEqualTo(1)
        assertThat(products.first().category).isEqualTo(CATEGORY_NAME_ELECTRONICS)
    }

    @Test
    fun `verify product was selected and returns true`() {
        val product = Product(id = 1, name = "A1", price = 5.0, description = "AAA", category = CATEGORY_NAME_JEWELRY, imageUrl = "")
        productViewModel.selectProduct(product)

        val result = productViewModel.selectedProduct.getOrAwaitValue()

        assertThat(result).isEqualTo(product)
    }

    @Test
    fun `verify selected product is not needed and returns null`() {
        val product = Product(id = 1, name = "A1", price = 5.0, description = "AAA", category = CATEGORY_NAME_JEWELRY, imageUrl = "")
        productViewModel.selectProduct(product)
        productViewModel.onSelectedProductFinish()

        val result = productViewModel.selectedProduct.getOrAwaitValue()

        assertThat(result).isNull()
    }
}