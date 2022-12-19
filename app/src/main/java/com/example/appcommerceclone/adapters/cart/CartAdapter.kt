package com.example.appcommerceclone.adapters.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.appcommerceclone.model.order.OrderedProduct

class CartAdapter(
    private val listener: CartListener
) : ListAdapter<OrderedProduct, CartViewHolder>(OrderedProductDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val orderedProduct = getItem(position)
        val adapterPosition = holder.adapterPosition
        holder.bind(orderedProduct, adapterPosition)
    }
}