package com.example.appcommerceclone.ui.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcommerceclone.databinding.ItemOrderBinding
import com.example.appcommerceclone.model.order.Order

class OrderViewHolder(
    private val binding: ItemOrderBinding,
    private val listener: OrderClickListener
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup, listener: OrderClickListener): OrderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemOrderBinding.inflate(layoutInflater, parent, false)
            return OrderViewHolder(binding, listener)
        }
    }

    fun bind(order: Order) {
        binding.order = order
        binding.orderClickListener = listener
        binding.executePendingBindings()
    }
}