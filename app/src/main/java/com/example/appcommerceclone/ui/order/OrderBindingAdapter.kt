package com.example.appcommerceclone.ui.order

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.appcommerceclone.R
import com.example.appcommerceclone.model.order.Order
import java.util.*

@BindingAdapter("setOrderId")
fun TextView.setOrderId(order: Order) {
    text = context.getString(R.string.order_item_id, order.id.toString())
}

@BindingAdapter("setOrderDate")
fun TextView.setOrderDate(order: Order) {
    text = context.getString(R.string.order_item_date, order.date)
}

@BindingAdapter("setOrderProductsList")
fun TextView.setOrderProductsList(order: Order) {
    val listOfProductNames = order.orderedProducts.map { it.product.name }
    text = context.getString(R.string.order_item_products, listOfProductNames.joinToString())
}

@BindingAdapter("setOrderTotalPrice")
fun TextView.setOrderTotalPrice(order: Order) {
    text = context.getString(R.string.order_item_total_price, order.getFormattedPrice())
}