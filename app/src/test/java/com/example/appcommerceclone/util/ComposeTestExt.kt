package com.example.appcommerceclone.util

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.navigation.NavHostController
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.common.truth.Truth.assertThat
import org.robolectric.shadows.ShadowLog

fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.getStringResource(@StringRes resId: Int): String {
    return activity.getString(resId)
}

fun NavHostController.assertCurrentRouteIsEqualTo(route: String) {
    assertThat(currentBackStackEntry?.destination?.route).isEqualTo(route)
}

fun showSemanticTreeInConsole() {
    ShadowLog.stream = System.out
}