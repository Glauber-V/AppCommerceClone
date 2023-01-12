package com.example.appcommerceclone.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appcommerceclone.databinding.ItemProductFavoritesBinding
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.ui.product.ProductDiffCallback
import com.example.appcommerceclone.viewmodels.FavoritesViewModel

class FavoritesAdapter(
    private val favoritesViewModel: FavoritesViewModel
) : ListAdapter<Product, FavoritesAdapter.FavoriteProductViewHolder>(ProductDiffCallback) {

    class FavoriteProductViewHolder(val binding: ItemProductFavoritesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProductFavoritesBinding.inflate(layoutInflater, parent, false)
        return FavoriteProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.binding.product = product
        holder.binding.productFavoriteRemoveBtn.setOnClickListener {
            favoritesViewModel.removeFromFavorites(product)
            notifyItemRemoved(holder.adapterPosition)
        }
    }
}