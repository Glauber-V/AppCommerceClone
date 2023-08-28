package com.example.appcommerceclone.ui.user

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("setHouseNumber")
fun TextView.setHouseNumber(number: Int) {
    text = number.toString()
}


