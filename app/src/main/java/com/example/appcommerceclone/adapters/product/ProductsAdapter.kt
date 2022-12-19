package com.example.appcommerceclone.adapters.product

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.appcommerceclone.model.product.Product

class ProductsAdapter(
    private val listener: ProductClickListener
) : ListAdapter<Product, ProductViewHolder>(ProductDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}