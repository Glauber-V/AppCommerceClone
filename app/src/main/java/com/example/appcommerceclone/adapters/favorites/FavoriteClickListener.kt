package com.example.appcommerceclone.adapters.favorites

import com.example.appcommerceclone.model.product.Product

interface FavoriteClickListener {

    fun onFavoriteRemoved(product: Product)
}
