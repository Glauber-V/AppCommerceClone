package com.example.appcommerceclone.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appcommerceclone.data.product.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor() : ViewModel() {

    private val _favorites = MutableLiveData<MutableList<Product>>(mutableListOf())
    val favorites: LiveData<MutableList<Product>> = _favorites


    fun addToFavorites(product: Product): Boolean {
        return if (!_favorites.value!!.contains(product)) {
            _favorites.value?.add(product)
            true
        } else {
            false
        }
    }

    fun removeFromFavorites(product: Product) {
        _favorites.value?.remove(product)
    }
}