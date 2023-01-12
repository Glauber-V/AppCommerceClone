package com.example.appcommerceclone.ui.product

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("changeVisibility")
fun View.changeVisibility(isInFullMode: Boolean) {
    visibility =
        if (isInFullMode) View.VISIBLE
        else View.GONE
}