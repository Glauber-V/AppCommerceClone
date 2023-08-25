package com.example.appcommerceclone.util

import android.view.View
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.addOnTextChangedListener() {
    val textInputEditText = editText as TextInputEditText
    textInputEditText.doOnTextChanged { text, _, _, _ ->
        if (text.toString().isNotEmpty()) {
            error = null
            isErrorEnabled = false
        }
    }
}

fun TextInputLayout.validate(errorMessage: String): Boolean {
    var isValid = true
    val textInputEditText = editText as TextInputEditText
    if (textInputEditText.text.toString().isEmpty()) {
        isValid = false
        error = errorMessage
        isErrorEnabled = true
    } else {
        error = null
        isErrorEnabled = false
    }
    return isValid
}

fun showSnackbar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}