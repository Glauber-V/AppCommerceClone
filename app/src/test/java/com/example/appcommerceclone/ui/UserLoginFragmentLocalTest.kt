package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.appcommerceclone.R
import com.example.appcommerceclone.TestNavHostControllerRule
import com.example.appcommerceclone.di.ConnectivityModule
import com.example.appcommerceclone.di.UsersModule
import com.example.appcommerceclone.getOrAwaitValue
import com.example.appcommerceclone.launchFragmentInHiltContainer
import com.example.appcommerceclone.util.noConstraintsClick
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
class UserLoginFragmentLocalTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testNavHostControllerRule = TestNavHostControllerRule(R.id.user_login_fragment)

    private lateinit var navHostController: TestNavHostController

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
    }

    @Test
    fun clickLoginBtn_noUsernameAndPassword_stayInUserLoginFragment() {
        launchFragmentInHiltContainer<UserLoginFragment>(navHostController = navHostController)

        onView(withId(R.id.user_login_username_text))
            .perform(replaceText(""))

        onView(withId(R.id.user_login_password_text))
            .perform(replaceText(""))

        onView(withId(R.id.user_login_btn))
            .perform(noConstraintsClick())

        assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)
    }

    @Test
    fun clickLoginBtn_withUsernameAndPassword_navigateToMainFragment() = runTest {
        launchFragmentInHiltContainer<UserLoginFragment>(navHostController = navHostController) {

            onView(withId(R.id.user_login_username_text))
                .perform(replaceText("Orisa"))

            onView(withId(R.id.user_login_password_text))
                .perform(replaceText("321"))

            onView(withId(R.id.user_login_btn))
                .perform(noConstraintsClick())

            advanceUntilIdle()

            val user = userViewModel.loggedUser.getOrAwaitValue()
            assertThat(user).isNotNull()
            assertThat(user?.username).isEqualTo("Orisa")
            assertThat(user?.password).isEqualTo("321")
        }
    }

    @Test
    fun clickRegisterBtn_navigateToRegisterFragment() {
        launchFragmentInHiltContainer<UserLoginFragment>(navHostController = navHostController)

        onView(withId(R.id.user_login_register_btn))
            .perform(noConstraintsClick())

        assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_register_fragment)
    }
}