package com.example.appcommerceclone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appcommerceclone.databinding.ItemProductInCartBinding
import com.example.appcommerceclone.model.order.OrderedProduct
import com.example.appcommerceclone.model.order.OrderedProductDiffCallback
import com.example.appcommerceclone.viewmodels.CartViewModel

class CartAdapter(
    private val cardViewModel: CartViewModel
) : ListAdapter<OrderedProduct, CartAdapter.CartViewHolder>(OrderedProductDiffCallback) {

    class CartViewHolder(val binding: ItemProductInCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(orderedProduct: OrderedProduct) {
            binding.orderedProduct = orderedProduct
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProductInCartBinding.inflate(layoutInflater, parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val orderedProduct = getItem(position)
        holder.bind(orderedProduct)

        holder.binding.cartIncreaseQuantity.setOnClickListener {
            cardViewModel.increaseQuantity(orderedProduct)
            updateItem(holder, position, orderedProduct)
        }

        holder.binding.cartDecreaseQuantity.setOnClickListener {
            cardViewModel.decreaseQuantity(orderedProduct)
            updateItem(holder, position, orderedProduct)
        }
    }

    private fun updateItem(holder: CartViewHolder, position: Int, orderedProduct: OrderedProduct) {
        holder.binding.productQuantity.text = orderedProduct.quantity.toString()
        holder.binding.productPrice.text = orderedProduct.getFormattedPrice()
        if (orderedProduct.quantity < 1) notifyItemRemoved(position)
    }
}