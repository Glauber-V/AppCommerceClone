package com.example.appcommerceclone.util

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


object ViewExt {

    fun validateEditText(textInputLayout: TextInputLayout, textInputEditText: TextInputEditText, errorMessage: String): Boolean {
        var isValid = true
        textInputEditText.apply {
            if (text.toString().isEmpty()) {
                textInputLayout.isErrorEnabled = true
                textInputLayout.error = errorMessage
                isValid = false
            } else {
                textInputLayout.error = null
                textInputLayout.isErrorEnabled = false
            }
            doOnTextChanged { text, _, _, _ ->
                if (text.toString().isNotEmpty()) {
                    textInputLayout.error = null
                    textInputLayout.isErrorEnabled = false
                }
            }
        }
        return isValid
    }

    fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    fun showTextEditor(fieldA: TextInputLayout, fieldB: TextInputLayout) {
        fieldA.isEnabled = true
        fieldB.isEnabled = true
    }

    fun hideTextEditor(fieldA: TextInputLayout, fieldB: TextInputLayout) {
        fieldA.isEnabled = false
        fieldB.isEnabled = false
    }

    @Suppress("DEPRECATION")
    fun Fragment.hideKeyboard() {
        ViewCompat.getWindowInsetsController(requireView())?.hide(WindowInsetsCompat.Type.ime())
    }
}