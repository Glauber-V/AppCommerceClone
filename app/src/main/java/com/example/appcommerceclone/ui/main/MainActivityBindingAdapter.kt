package com.example.appcommerceclone.ui.main

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("setVisibility")
fun TextView.setVisibility(hasConnection: Boolean) {
    visibility = if (hasConnection) View.VISIBLE else View.GONE
}