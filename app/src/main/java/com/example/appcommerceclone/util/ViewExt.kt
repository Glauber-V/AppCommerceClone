package com.example.appcommerceclone.util

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object ViewExt {

    // VALIDATE TEXT UTILITIES
    /** Prepare a [TextInputLayout] and [TextInputEditText] to handle errors such as empty rows or wrong inputs */
    fun prepareEditText(textInputLayout: TextInputLayout, textInputEditText: TextInputEditText, errorMessage: String): Boolean {
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

    /** Shows a snackbar with specific message */
    fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    // TEXT INPUT LAYOUT UTILITIES
    /** Use this to make [TextInputLayout] editable */
    fun showTextEditor(fieldA: TextInputLayout, fieldB: TextInputLayout) {
        fieldA.isEnabled = true
        fieldB.isEnabled = true
    }

    /** Use this to make [TextInputLayout] not editable */
    fun hideTextEditor(fieldA: TextInputLayout, fieldB: TextInputLayout) {
        fieldA.isEnabled = false
        fieldB.isEnabled = false
    }

    // SOFT KEYBOARD UTILITIES
    /** Hides the soft input keyboard */
    fun hideInputKeyboard(activity: Activity) {
        activity.currentFocus?.let { view ->
            val inputMethodManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}