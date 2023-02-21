package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.user.FakeUserAuthenticator.Companion.firstUser
import com.example.appcommerceclone.di.ConnectivityModule
import com.example.appcommerceclone.di.UsersModule
import com.example.appcommerceclone.ui.user.UserProfileFragment
import com.example.appcommerceclone.util.TestNavHostControllerRule
import com.example.appcommerceclone.util.launchFragmentInHiltContainer
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    UsersModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class UserProfileFragmentLocalTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testNavHostControllerRule = TestNavHostControllerRule(R.id.user_profile_fragment)

    private lateinit var navHostController: TestNavHostController

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
    }

    @Test
    fun launchUserProfileFragment_withUser_scrollToUpdateBtn() = runTest {
        launchFragmentInHiltContainer<UserProfileFragment>(navHostController = navHostController) {
            userViewModel.login(username = firstUser.username, password = firstUser.password)
            advanceUntilIdle()

            onView(withId(R.id.user_profile_logout_btn))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun launchUserProfileFragment_noUser_navigateToLoginFragment() {
        launchFragmentInHiltContainer<UserProfileFragment>(navHostController = navHostController) {
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)
        }
    }
}