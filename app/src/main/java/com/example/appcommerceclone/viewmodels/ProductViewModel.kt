package com.example.appcommerceclone.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.product.ProductsRepository
import com.example.appcommerceclone.model.product.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ProductCategories(val categoryName: String) {
    NONE(""),
    JEWELERY("jewelery"),
    ELECTRONICS("electronics"),
    MENS_CLOTHING("men's clothing"),
    WOMENS_CLOTHING("women's clothing")
}

enum class ProductColors(val color: Color) {
    NONE(Color.Transparent),
    YELLOW(Color(0xFFFFC107)),
    BLUE(Color(0xFF03A9F4)),
    GREEN(Color(0xFF8BC34A))
}

enum class ProductSizes(val size: String) {
    NONE(""),
    P("P"),
    M("M"),
    G("G"),
    GG("GG")
}

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


    fun updateProductList() {
        viewModelScope.launch(dispatcherProvider.main) {
            _isDataLoaded.value = false
            _isLoading.value = true
            _allProducts = repository.loadProductsList()
            _products.value = _allProducts
            _isDataLoaded.value = true
            _isLoading.value = false
        }
    }

    fun filterProductList(category: ProductCategories) {
        _products.value = _allProducts.filter { it.category == category.categoryName }
    }

    fun showMoreOptions(product: Product): Boolean {
        return when (product.category) {
            ProductCategories.JEWELERY.categoryName -> false
            ProductCategories.ELECTRONICS.categoryName -> false
            else -> true
        }
    }

    fun selectProduct(product: Product) {
        _selectedProduct.value = product
    }

    fun onSelectedProductFinish() {
        _selectedProduct.value = null
    }
}