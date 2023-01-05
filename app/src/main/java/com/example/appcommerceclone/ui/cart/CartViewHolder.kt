package com.example.appcommerceclone.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcommerceclone.databinding.ItemProductInCartBinding
import com.example.appcommerceclone.model.order.OrderedProduct

class CartViewHolder(
    private val binding: ItemProductInCartBinding,
    private val listener: CartListener
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup, listener: CartListener): CartViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemProductInCartBinding.inflate(layoutInflater, parent, false)
            return CartViewHolder(binding, listener)
        }
    }

    fun bind(orderedProduct: OrderedProduct, position: Int) {
        binding.orderedProduct = orderedProduct
        binding.cartListener = listener
        binding.position = position
        binding.executePendingBindings()
    }
}