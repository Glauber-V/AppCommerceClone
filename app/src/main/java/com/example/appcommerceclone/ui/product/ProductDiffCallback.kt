package com.example.appcommerceclone.ui.product

import androidx.recyclerview.widget.DiffUtil
import com.example.appcommerceclone.model.product.Product

object ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem == newItem
}