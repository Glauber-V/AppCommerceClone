package com.example.appcommerceclone.adapters.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("userProfileChangeEditMode")
fun TextView.userProfileChangeEditMode(isEditMode: Boolean) {
    isEnabled = isEditMode
}