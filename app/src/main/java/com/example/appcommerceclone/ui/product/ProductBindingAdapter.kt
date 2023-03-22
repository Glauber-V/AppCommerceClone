package com.example.appcommerceclone.ui.product

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.appcommerceclone.R
import com.example.appcommerceclone.model.order.OrderedProduct
import java.text.NumberFormat

@BindingAdapter("setProductImageFromUrl")
fun ImageView.setProductImageFromUrl(imageUrl: String?) {
    Glide.with(context)
        .load(imageUrl)
        .fitCenter()
        .placeholder(R.drawable.place_holder)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(this)
}

@BindingAdapter("setProductPrice")
fun TextView.setProductPrice(price: Double) {
    text = NumberFormat.getCurrencyInstance().format(price)
}

@BindingAdapter("setProductQuantity")
fun TextView.setProductQuantity(quantity: Int) {
    text = quantity.toString()
}

@BindingAdapter("setOrderedProductPrice")
fun TextView.setOrderedProductPrice(orderedProduct: OrderedProduct) {
    val finalPrice = orderedProduct.product.price * orderedProduct.quantity
    text = NumberFormat.getCurrencyInstance().format(finalPrice)
}