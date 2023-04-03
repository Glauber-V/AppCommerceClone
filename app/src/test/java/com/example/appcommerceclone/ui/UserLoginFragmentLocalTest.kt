package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.user.FakeUserProvider.Companion.firstUser
import com.example.appcommerceclone.data.user.UserAuthenticator
import com.example.appcommerceclone.data.user.UserPreferences
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.UsersModule
import com.example.appcommerceclone.ui.user.UserLoginFragment
import com.example.appcommerceclone.util.*
import com.example.appcommerceclone.viewmodels.UserViewModel
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
import javax.inject.Inject

@UninstallModules(UsersModule::class, DispatcherModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class UserLoginFragmentLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testNavHostControllerRule = TestNavHostControllerRule(R.id.user_login_fragment)

    @get:Rule(order = 2)
    val testMainDispatcherRule = TestMainDispatcherRule()

    @Inject lateinit var userAuthenticator: UserAuthenticator
    @Inject lateinit var userPreferences: UserPreferences
    @Inject lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var navHostController: TestNavHostController
    private lateinit var userViewModel: UserViewModel
    private lateinit var factory: TestFragmentFactory

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
        userViewModel = UserViewModel(userAuthenticator, userPreferences, dispatcherProvider)
        factory = TestFragmentFactory(userViewModelTest = userViewModel)
    }

    @Test
    fun launchUserLoginFragment_clickLoginBtn_noUsernameAndPassword_stayInUserLoginFragment() = runTest {
        launchFragmentInHiltContainer<UserLoginFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)

            onView(withId(R.id.user_login_username_text))
                .perform(replaceText(""))

            onView(withId(R.id.user_login_password_text))
                .perform(replaceText(""))

            onView(withId(R.id.user_login_btn))
                .perform(noConstraintsClick())

            advanceUntilIdle()

            onView(withId(R.id.user_login_username))
                .check(matches(hasTextInputLayoutErrorText(getString(R.string.user_error_no_username))))

            onView(withId(R.id.user_login_password))
                .check(matches(hasTextInputLayoutErrorText(getString(R.string.user_error_no_password))))

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)
        }
    }

    @Test
    fun launchUserLoginFragment_clickLoginBtn_wrongUsernameAndPassword_stayInUserLoginFragment() = runTest {
        launchFragmentInHiltContainer<UserLoginFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)

            onView(withId(R.id.user_login_username_text))
                .perform(replaceText("Robert"))

            onView(withId(R.id.user_login_password_text))
                .perform(replaceText("45687"))

            onView(withId(R.id.user_login_btn))
                .perform(noConstraintsClick())

            advanceUntilIdle()

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)
        }
    }

    @Test
    fun launchUserLoginFragment_clickLoginBtn_withUsernameAndPassword_navigateToMainFragment() = runTest {
        launchFragmentInHiltContainer<UserLoginFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)

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

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.user_login_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.products_fragment)
        }
    }

    @Test
    fun launchUserLoginFragment_clickRegisterBtn_navigateToRegisterFragment() {
        launchFragmentInHiltContainer<UserLoginFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)

            onView(withId(R.id.user_login_register_btn)).perform(noConstraintsClick())

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.user_login_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_register_fragment)
        }
    }
}