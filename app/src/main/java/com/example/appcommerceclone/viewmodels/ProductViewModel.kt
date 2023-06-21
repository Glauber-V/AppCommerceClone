package com.example.appcommerceclone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.product.ProductsRepository
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_ELECTRONICS
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_JEWELRY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductsRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private var _allProducts: List<Product> = emptyList()

    private val _isDataLoaded = MutableLiveData(false)
    val isDataLoaded: LiveData<Boolean> = _isDataLoaded

    private val _products = MutableLiveData<List<Product>>(mutableListOf())
    val products: LiveData<List<Product>> = _products

    private val _selectedProduct = MutableLiveData<Product?>(null)
    val selectedProduct: LiveData<Product?> = _selectedProduct

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading


    fun updateProductsList(categoryName: String = "") {
        viewModelScope.launch(dispatcherProvider.main) {
            _isLoading.value = true
            _allProducts = repository.loadProductsList()
            _products.value = prepareProductsList(_allProducts, categoryName)
            _isDataLoaded.value = true
            _isLoading.value = false
        }
    }


    private fun prepareProductsList(productList: List<Product>, categoryName: String): List<Product> {
        return if (categoryName.isEmpty()) productList
        else productList.filter { it.category == categoryName }
    }


    fun selectProduct(product: Product) {
        _selectedProduct.value = product
    }

    fun onSelectedProductFinish() {
        _selectedProduct.value = null
    }


    fun selectCategoryAndUpdateProductsList(categoryName: String) {
        _products.value = prepareProductsList(_allProducts, categoryName)
    }

    fun showMoreOptions(product: Product): Boolean {
        return when (product.category) {
            CATEGORY_NAME_ELECTRONICS -> false
            CATEGORY_NAME_JEWELRY -> false
            else -> true
        }
    }
}