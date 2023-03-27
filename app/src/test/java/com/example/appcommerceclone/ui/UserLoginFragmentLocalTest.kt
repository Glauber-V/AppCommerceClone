package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.user.FakeUserAuthenticator.Companion.firstUser
import com.example.appcommerceclone.ui.user.UserLoginFragment
import com.example.appcommerceclone.util.*
import com.example.appcommerceclone.viewmodels.UserViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class UserLoginFragmentLocalTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testNavHostControllerRule = TestNavHostControllerRule(R.id.user_login_fragment)

    @get:Rule(order = 2)
    var testFragmentFactoryRule = TestFragmentFactoryRule()

    private lateinit var navHostController: TestNavHostController
    private lateinit var userViewModel: UserViewModel
    private lateinit var factory: TestFragmentFactory

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
        userViewModel = testFragmentFactoryRule.userViewModel!!
        factory = testFragmentFactoryRule.factory!!
    }

    @Test
    fun clickLoginBtn_noUsernameAndPassword_stayInUserLoginFragment() {
        launchFragmentInHiltContainer<UserLoginFragment>(navHostController = navHostController, fragmentFactory = factory) {

            onView(withId(R.id.user_login_username_text))
                .perform(replaceText(""))

            onView(withId(R.id.user_login_password_text))
                .perform(replaceText(""))

            onView(withId(R.id.user_login_btn))
                .perform(noConstraintsClick())

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)
        }
    }

    @Test
    fun clickLoginBtn_wrongUsernameAndPassword_stayInUserLoginFragment() {
        launchFragmentInHiltContainer<UserLoginFragment>(navHostController = navHostController, fragmentFactory = factory) {

            onView(withId(R.id.user_login_username_text))
                .perform(replaceText("Robert"))

            onView(withId(R.id.user_login_password_text))
                .perform(replaceText("45687"))

            onView(withId(R.id.user_login_btn))
                .perform(noConstraintsClick())

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)
        }
    }

    @Test
    fun clickLoginBtn_withUsernameAndPassword_navigateToMainFragment() = runTest {
        launchFragmentInHiltContainer<UserLoginFragment>(navHostController = navHostController, fragmentFactory = factory) {

            onView(withId(R.id.user_login_username_text))
                .perform(replaceText(firstUser.username))

            onView(withId(R.id.user_login_password_text))
                .perform(replaceText(firstUser.password))

            onView(withId(R.id.user_login_btn))
                .perform(noConstraintsClick())

            advanceUntilIdle()

            val user = userViewModel.loggedUser.getOrAwaitValue()
            assertThat(user).isNotNull()
            assertThat(user?.username).isEqualTo(firstUser.username)
            assertThat(user?.password).isEqualTo(firstUser.password)
        }
    }

    @Test
    fun clickRegisterBtn_navigateToRegisterFragment() {
        launchFragmentInHiltContainer<UserLoginFragment>(navHostController = navHostController, fragmentFactory = factory) {

            onView(withId(R.id.user_login_register_btn)).perform(noConstraintsClick())

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_register_fragment)
        }
    }
}