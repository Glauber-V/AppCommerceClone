package com.example.appcommerceclone.ui.order

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.appcommerceclone.model.order.Order

class OrdersAdapter(
    private val listener: OrderClickListener
) : ListAdapter<Order, OrderViewHolder>(OrderDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}