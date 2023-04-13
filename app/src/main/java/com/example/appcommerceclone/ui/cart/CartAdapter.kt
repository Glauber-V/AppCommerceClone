package com.example.appcommerceclone.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appcommerceclone.databinding.ItemProductInCartBinding
import com.example.appcommerceclone.model.order.OrderedProduct
import com.example.appcommerceclone.viewmodels.CartViewModel

class CartAdapter(
    private val cartViewModel: CartViewModel
) : ListAdapter<OrderedProduct, CartAdapter.CartViewHolder>(OrderedProductDiffCallback) {

    class CartViewHolder(val binding: ItemProductInCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProductInCartBinding.inflate(layoutInflater, parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val orderedProduct = getItem(position)
        holder.binding.orderedProduct = orderedProduct
        holder.binding.itemProductInCartIncreaseQuantityBtn.setOnClickListener {
            cartViewModel.increaseQuantity(orderedProduct)
            notifyItemChanged(holder.adapterPosition)
        }
        holder.binding.itemProductInCartDecreaseQuantityBtn.setOnClickListener {
            cartViewModel.decreaseQuantity(orderedProduct)
            if (orderedProduct.quantity >= 1) notifyItemChanged(holder.adapterPosition)
            else notifyItemRemoved(holder.adapterPosition)
        }
    }
}