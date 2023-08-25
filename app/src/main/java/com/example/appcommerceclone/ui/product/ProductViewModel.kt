package com.example.appcommerceclone.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.product.ProductsRepository
import com.example.appcommerceclone.data.product.model.Product
import com.example.appcommerceclone.util.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ProductCategories(val categoryName: String) {
    NONE(""),
    JEWELERY("jewelery"),
    ELECTRONICS("electronics"),
    MENS_CLOTHING("men's clothing"),
    WOMENS_CLOTHING("women's clothing")
}

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductsRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private var _allProducts: List<Product> = emptyList()

    private val _loadingState = MutableLiveData(LoadingState.NOT_STARTED)
    val loadingState: LiveData<LoadingState> = _loadingState

    private val _products = MutableLiveData<List<Product>>(listOf())
    val products: LiveData<List<Product>> = _products

    private val _selectedProduct = MutableLiveData<Product?>(null)
    val selectedProduct: LiveData<Product?> = _selectedProduct

    fun updateProductList() {
        _loadingState.value = LoadingState.LOADING
        viewModelScope.launch(dispatcherProvider.main) {
            _allProducts = try {
                repository.loadProductsList()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                emptyList()
            }
            _products.value = _allProducts
            _loadingState.value = if (_allProducts.isNotEmpty()) LoadingState.SUCCESS else LoadingState.FAILURE
        }
    }

    fun filterProductList(category: ProductCategories) {
        _products.value = if (category == ProductCategories.NONE) _allProducts
        else _allProducts.filter { it.category == category.categoryName }
    }

    fun selectProduct(product: Product) {
        _selectedProduct.value = product
    }

    fun onSelectedProductFinish() {
        _selectedProduct.value = null
    }
}