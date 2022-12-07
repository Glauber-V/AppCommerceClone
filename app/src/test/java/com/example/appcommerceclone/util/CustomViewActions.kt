package com.example.appcommerceclone.util

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

/** Disables the 90% constraint on view clicks. This is a workaround to the
 *  java.lang.RuntimeException: Action will not be performed because the target view
 *  does not match one or more of the following constraints. */
fun noConstraintsClick(): ViewAction = object : ViewAction {
    override fun getConstraints(): Matcher<View> = ViewMatchers.isEnabled()

    override fun getDescription(): String = ""

    override fun perform(uiController: UiController, view: View) {
        view.performClick()
    }
}

/** Disables the 90% constraint on long view clicks. This is a workaround to the
 *  java.lang.RuntimeException: Action will not be performed because the target view
 *  does not match one or more of the following constraints. */
fun noConstraintsLongClick(): ViewAction = object : ViewAction {
    override fun getConstraints(): Matcher<View> = ViewMatchers.isEnabled()

    override fun getDescription(): String = ""

    override fun perform(uiController: UiController, view: View) {
        view.performLongClick()
    }
}