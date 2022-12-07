package com.example.appcommerceclone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appcommerceclone.databinding.ItemProductFavoritesBinding
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.model.product.ProductDiffCallback
import com.example.appcommerceclone.viewmodels.FavoritesViewModel

class FavoritesAdapter(
    private val favoritesViewModel: FavoritesViewModel
) : ListAdapter<Product, FavoritesAdapter.FavoriteProductViewHolder>(ProductDiffCallback) {

    class FavoriteProductViewHolder(val binding: ItemProductFavoritesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.product = product
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProductFavoritesBinding.inflate(layoutInflater, parent, false)
        return FavoriteProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteProductViewHolder, position: Int) {
        val favoriteProduct = getItem(position)
        holder.bind(favoriteProduct)
        holder.binding.productFavoriteRemoveBtn.setOnClickListener {
            favoritesViewModel.removeFromFavorites(favoriteProduct)
            notifyItemRemoved(position)
        }
    }
}