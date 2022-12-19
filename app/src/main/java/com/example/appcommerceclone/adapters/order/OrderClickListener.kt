package com.example.appcommerceclone.adapters.order

import com.example.appcommerceclone.model.order.Order

interface OrderClickListener {

    fun onOrderClicked(order: Order)
}