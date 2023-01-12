package com.example.appcommerceclone.ui.order

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.appcommerceclone.R
import com.example.appcommerceclone.model.order.Order
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("setOrderId")
fun TextView.setOrderId(order: Order) {
    text = context.getString(R.string.order_item_id, order.id.toString())
}

@BindingAdapter("setOrderDate")
fun TextView.setOrderDate(order: Order) {
    text = if (order.id >= 10) {
        context.getString(R.string.order_item_date, order.date)
    } else {
        val oldPattern = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val newPattern = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = oldPattern.parse(order.date)
        val formattedDate = date?.let { newPattern.format(it) }

        context.getString(R.string.order_item_date, formattedDate)
    }
}

@BindingAdapter("setOrderProductsList")
fun TextView.setOrderProductsList(order: Order) {
    if (order.id >= 10) {
        visibility = View.VISIBLE

        val productsNameList = mutableListOf<String>()
        order.orderedProducts.forEach { orderedProduct ->
            productsNameList.add(orderedProduct.product.name)
        }

        text = context.getString(R.string.order_item_products, productsNameList.joinToString())
    } else {
        visibility = View.GONE
    }
}

@BindingAdapter("setOrderTotalPrice")
fun TextView.setOrderTotalPrice(order: Order) {
    if (order.total > 0.0) {
        visibility = View.VISIBLE

        val formattedPrice = NumberFormat.getCurrencyInstance().format(order.total)
        text = context.getString(R.string.order_item_total_price, formattedPrice)
    } else {
        visibility = View.GONE
    }
}