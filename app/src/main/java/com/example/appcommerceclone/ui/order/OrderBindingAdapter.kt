package com.example.appcommerceclone.ui.order

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.product.model.Order
import com.example.appcommerceclone.util.formatTotalPrice
import com.example.appcommerceclone.util.getProductsNamesAsString
import java.util.*

@BindingAdapter("setOrderId")
fun TextView.setOrderId(order: Order) {
    text = context.getString(R.string.order_item_id, order.id.toString())
}

@BindingAdapter("setOrderDate")
fun TextView.setOrderDate(order: Order) {
    text = context.getString(R.string.order_item_date, order.date)
}

@BindingAdapter("setOrderedProductList")
fun TextView.setOrderedProductList(order: Order) {
    text = context.getString(R.string.order_item_products, order.getProductsNamesAsString())
}

@BindingAdapter("setOrderTotalPrice")
fun TextView.setOrderTotalPrice(order: Order) {
    text = context.getString(R.string.order_item_total_price, order.formatTotalPrice())
}