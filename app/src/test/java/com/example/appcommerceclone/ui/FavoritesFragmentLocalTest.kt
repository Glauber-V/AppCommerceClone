package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.appcommerceclone.R
import com.example.appcommerceclone.TestNavHostControllerRule
import com.example.appcommerceclone.di.ConnectivityModule
import com.example.appcommerceclone.di.ProductsModule
import com.example.appcommerceclone.di.UsersModule
import com.example.appcommerceclone.launchFragmentInHiltContainer
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@UninstallModules(
    ConnectivityModule::class,
    ProductsModule::class,
    UsersModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class FavoritesFragmentLocalTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testNavHostControllerRule = TestNavHostControllerRule(R.id.favorites_fragment)

    private lateinit var navHostController: TestNavHostController

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
    }

    @Test
    fun launchFavoritesFragment_noUser_shouldNavigateToUserLoginFragment() {
        launchFragmentInHiltContainer<FavoritesFragment>(navHostController = navHostController)

        assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)
    }

    @Test
    fun launchFavoritesFragment_withUser_recyclerViewShouldBeVisible() = runTest {
        launchFragmentInHiltContainer<FavoritesFragment>(navHostController = navHostController) {
            userViewModel.login(username = "Orisa", password = "321")
            advanceUntilIdle()
        }

        onView(withId(R.id.favorites_recycler_view))
            .check(matches(isDisplayed()))
    }
}