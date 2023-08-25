package com.example.appcommerceclone.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appcommerceclone.data.product.model.Product
import com.example.appcommerceclone.databinding.ItemProductBinding

interface ProductClickHandler {

    fun onProductClicked(product: Product)
}

class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

object ProductDiffCallback : DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem == newItem
}

class ProductsAdapter(private val productClickHandler: ProductClickHandler) :
    ListAdapter<Product, ProductViewHolder>(ProductDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.binding.product = product
        holder.binding.itemProductCard.setOnClickListener { productClickHandler.onProductClicked(product) }
    }
}