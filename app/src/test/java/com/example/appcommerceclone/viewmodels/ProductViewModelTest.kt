package com.example.appcommerceclone.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.data.product.FakeProductsRepository
import com.example.appcommerceclone.data.product.FakeProductsRepository.Companion.productJewelery
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_ELECTRONICS
import com.example.appcommerceclone.util.getOrAwaitValue
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
    private lateinit var product: Product

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        fakeProductsRepository = FakeProductsRepository()
        productViewModel = ProductViewModel(fakeProductsRepository)
        product = productJewelery
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun selectCategory_verifyAllProductsHaveTheSameCategory() = runTest {
        productViewModel.updateProductsList(CATEGORY_NAME_ELECTRONICS)
        advanceUntilIdle()

        val products = productViewModel.products.getOrAwaitValue()

        assertThat(products.size).isEqualTo(1)
        assertThat(products.first().category).isEqualTo(CATEGORY_NAME_ELECTRONICS)
    }

    @Test
    fun selectProduct_confirmSelectedProduct() {
        productViewModel.selectProduct(product)

        val selectedProduct = productViewModel.selectedProduct.getOrAwaitValue()

        assertThat(selectedProduct).isEqualTo(product)
    }

    @Test
    fun selectProduct_verifyOnSelectedProductFinishCompletesWithNullValue() {
        productViewModel.selectProduct(product)
        val firstCheck = productViewModel.selectedProduct.getOrAwaitValue()
        assertThat(firstCheck).isEqualTo(product)

        productViewModel.onSelectedProductFinish()
        val secondCheck = productViewModel.selectedProduct.getOrAwaitValue()
        assertThat(secondCheck).isNull()
    }
}