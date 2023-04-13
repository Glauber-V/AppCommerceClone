package com.example.appcommerceclone.util

import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


fun noConstraintsClick(): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> = ViewMatchers.isEnabled()

        override fun getDescription(): String = ""

        override fun perform(uiController: UiController, view: View) {
            view.performClick()
        }
    }
}

fun hasTextInputLayoutErrorText(expectedErrorText: String): Matcher<View?> {
    return object : TypeSafeMatcher<View>() {

        override fun describeTo(description: Description?) {}

        override fun matchesSafely(view: View?): Boolean {
            if (view !is TextInputLayout) return false
            val error = view.error ?: return false
            val hint = error.toString()
            return expectedErrorText == hint
        }
    }
}

fun atPosition(position: Int, @NonNull itemMatcher: Matcher<View?>): Matcher<View> {
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

        override fun describeTo(description: Description) {
            description.appendText("has item at position $position: ")
            itemMatcher.describeTo(description)
        }

        override fun matchesSafely(view: RecyclerView): Boolean {
            val viewHolder: RecyclerView.ViewHolder = view.findViewHolderForAdapterPosition(position) ?: return false
            return itemMatcher.matches(viewHolder.itemView)
        }
    }
}