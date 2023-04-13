package com.example.appcommerceclone.ui.product

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.appcommerceclone.R
import com.example.appcommerceclone.model.order.OrderedProduct
import com.example.appcommerceclone.model.product.Product

@BindingAdapter("setProductImageFromUrl")
fun ImageView.setProductImageFromUrl(product: Product) {
    Glide.with(context)
        .load(product.imageUrl)
        .fitCenter()
        .placeholder(R.drawable.place_holder)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(this)
}

@BindingAdapter("setProductPrice")
fun TextView.setProductPrice(product: Product) {
    text = product.getFormattedPrice()
}

@BindingAdapter("setProductQuantity")
fun TextView.setProductQuantity(orderedProduct: OrderedProduct) {
    text = orderedProduct.quantity.toString()
}

@BindingAdapter("setOrderedProductPrice")
fun TextView.setOrderedProductPrice(orderedProduct: OrderedProduct) {
    text = orderedProduct.getFormattedPrice()
}