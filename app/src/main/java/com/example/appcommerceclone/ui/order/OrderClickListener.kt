package com.example.appcommerceclone.ui.order

import com.example.appcommerceclone.model.order.Order

interface OrderClickListener {

    fun onOrderClicked(order: Order)
}