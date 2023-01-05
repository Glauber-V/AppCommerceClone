package com.example.appcommerceclone.ui.favorites

import com.example.appcommerceclone.model.product.Product

interface FavoriteClickListener {

    fun onFavoriteRemoved(product: Product, adapterPosition: Int)
}
