package com.example.appcommerceclone.model.order

import androidx.recyclerview.widget.DiffUtil

object OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem.date == newItem.date
}