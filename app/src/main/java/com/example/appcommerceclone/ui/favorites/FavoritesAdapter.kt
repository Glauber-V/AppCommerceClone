package com.example.appcommerceclone.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appcommerceclone.data.product.model.Product
import com.example.appcommerceclone.databinding.ItemProductFavoritesBinding
import com.example.appcommerceclone.ui.product.ProductDiffCallback

interface FavoriteClickHandler {

    fun onRemoveFromFavorites(product: Product)
}

class FavoriteProductViewHolder(val binding: ItemProductFavoritesBinding) : RecyclerView.ViewHolder(binding.root)

class FavoritesAdapter(private val favoriteClickHandler: FavoriteClickHandler) :
    ListAdapter<Product, FavoriteProductViewHolder>(ProductDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProductFavoritesBinding.inflate(layoutInflater, parent, false)
        return FavoriteProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.binding.product = product
        holder.binding.itemProductFavoriteRemoveBtn.setOnClickListener {
            favoriteClickHandler.onRemoveFromFavorites(product)
            notifyItemRemoved(holder.adapterPosition)
        }
    }
}