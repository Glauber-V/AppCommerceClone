package com.example.appcommerceclone.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcommerceclone.databinding.ItemProductBinding
import com.example.appcommerceclone.model.product.Product

class ProductViewHolder(
    private val binding: ItemProductBinding,
    private val listener: ProductClickListener
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup, listener: ProductClickListener): ProductViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
            return ProductViewHolder(binding, listener)
        }
    }

    fun bind(product: Product) {
        binding.product = product
        binding.productListener = listener
        binding.executePendingBindings()
    }
}