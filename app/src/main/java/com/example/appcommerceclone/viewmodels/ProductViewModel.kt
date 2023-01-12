package com.example.appcommerceclone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcommerceclone.data.product.ProductsRepository
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val repository: ProductsRepository) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>(mutableListOf())
    val products: LiveData<List<Product>> = _products

    private val _selectedProduct = MutableLiveData<Product?>()
    val selectedProduct: LiveData<Product?> = _selectedProduct

    private val _selectedCategory = MutableLiveData<String>()
    val selectedCategory: LiveData<String> = _selectedCategory


    fun refreshProducts() {
        viewModelScope.launch {
            val products = repository.loadProductsList()
            val category = _selectedCategory.value ?: ""
            _products.value = prepareProductsList(products, category)
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


    fun selectCategory(categoryName: String) {
        _selectedCategory.value = categoryName
    }

    fun checkDisplayMethodByProductCategory(product: Product): Boolean {
        return !(product.category == Constants.CATEGORY_ELECTRONICS || product.category == Constants.CATEGORY_JEWELRY)
    }
}