package com.example.appcommerceclone.adapters.cart

import androidx.recyclerview.widget.DiffUtil
import com.example.appcommerceclone.model.order.OrderedProduct

object OrderedProductDiffCallback : DiffUtil.ItemCallback<OrderedProduct>() {
    override fun areItemsTheSame(oldItem: OrderedProduct, newItem: OrderedProduct): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: OrderedProduct, newItem: OrderedProduct): Boolean =
        oldItem.id == newItem.id && oldItem.quantity == newItem.quantity
}