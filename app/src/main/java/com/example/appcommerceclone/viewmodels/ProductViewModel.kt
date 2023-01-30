package com.example.appcommerceclone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcommerceclone.data.product.ProductsRepository
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_ELECTRONICS
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_JEWELRY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val repository: ProductsRepository) : ViewModel() {

    private var _allProducts: List<Product> = emptyList()

    private val _products = MutableLiveData<List<Product>>(mutableListOf())
    val products: LiveData<List<Product>> = _products

    private val _selectedProduct = MutableLiveData<Product?>()
    val selectedProduct: LiveData<Product?> = _selectedProduct


    fun updateProductsList(categoryName: String = "") {
        viewModelScope.launch {
            _allProducts = repository.loadProductsList()
            _products.value = prepareProductsList(_allProducts, categoryName)
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

    fun checkIfShouldDisplayInFullDetailMode(product: Product): Boolean {
        return !(product.category == CATEGORY_NAME_ELECTRONICS || product.category == CATEGORY_NAME_JEWELRY)
    }
}