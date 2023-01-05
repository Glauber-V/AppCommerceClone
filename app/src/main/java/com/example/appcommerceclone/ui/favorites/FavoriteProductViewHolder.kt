package com.example.appcommerceclone.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcommerceclone.databinding.ItemProductFavoritesBinding
import com.example.appcommerceclone.model.product.Product

class FavoriteProductViewHolder(
    private val binding: ItemProductFavoritesBinding,
    private val listener: FavoriteClickListener
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup, listener: FavoriteClickListener): FavoriteProductViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemProductFavoritesBinding.inflate(layoutInflater, parent, false)
            return FavoriteProductViewHolder(binding, listener)
        }
    }

    fun bind(product: Product, adapterPosition: Int) {
        binding.product = product
        binding.favoriteListener = listener
        binding.adapterPosition = adapterPosition
        binding.executePendingBindings()
    }
}