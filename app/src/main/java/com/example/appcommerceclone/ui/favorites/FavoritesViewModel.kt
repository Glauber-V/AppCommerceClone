package com.example.appcommerceclone.ui.favorites

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.appcommerceclone.data.product.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor() : ViewModel() {

    private val _favorites = mutableStateListOf<Product>()
    val favorites: List<Product> = _favorites


    fun isFavorite(product: Product): Boolean {
        return _favorites.contains(product)
    }

    fun addToFavorites(product: Product) {
        _favorites.add(product)
    }

    fun removeFromFavorites(product: Product) {
        _favorites.remove(product)
    }
}