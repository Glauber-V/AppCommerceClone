package com.example.appcommerceclone.adapters.favorites

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.appcommerceclone.adapters.product.ProductDiffCallback
import com.example.appcommerceclone.model.product.Product

class FavoritesAdapter(
    private val listener: FavoriteClickListener
) : ListAdapter<Product, FavoriteProductViewHolder>(ProductDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteProductViewHolder {
        return FavoriteProductViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: FavoriteProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}