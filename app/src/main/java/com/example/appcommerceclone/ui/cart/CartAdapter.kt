package com.example.appcommerceclone.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appcommerceclone.data.product.model.OrderedProduct
import com.example.appcommerceclone.databinding.ItemProductInCartBinding

interface CartClickHandler {

    fun onIncreaseQuantity(orderedProduct: OrderedProduct)

    fun onDecreaseQuantity(orderedProduct: OrderedProduct)
}

class CartViewHolder(val binding: ItemProductInCartBinding) : RecyclerView.ViewHolder(binding.root)

object OrderedProductDiffCallback : DiffUtil.ItemCallback<OrderedProduct>() {
    override fun areItemsTheSame(oldItem: OrderedProduct, newItem: OrderedProduct): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: OrderedProduct, newItem: OrderedProduct): Boolean =
        oldItem.id == newItem.id && oldItem.quantity == newItem.quantity
}

class CartAdapter(private val cartClickHandler: CartClickHandler) : ListAdapter<OrderedProduct, CartViewHolder>(OrderedProductDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProductInCartBinding.inflate(layoutInflater, parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val orderedProduct = getItem(position)
        holder.binding.orderedProduct = orderedProduct
        holder.binding.itemProductInCartIncreaseQuantityBtn.setOnClickListener {
            cartClickHandler.onIncreaseQuantity(orderedProduct)
            notifyItemChanged(holder.adapterPosition)
        }
        holder.binding.itemProductInCartDecreaseQuantityBtn.setOnClickListener {
            cartClickHandler.onDecreaseQuantity(orderedProduct)
            if (orderedProduct.quantity >= 1) notifyItemChanged(holder.adapterPosition)
            else notifyItemRemoved(holder.adapterPosition)
        }
    }
}