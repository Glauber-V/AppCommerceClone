package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.user.UserAuthenticator
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.UsersModule
import com.example.appcommerceclone.ui.user.LoginFragment
import com.example.appcommerceclone.ui.user.UserViewModel
import com.example.appcommerceclone.util.LoadingState
import com.example.appcommerceclone.util.TestMainDispatcherRule
import com.example.appcommerceclone.util.TestNavHostControllerRule
import com.example.appcommerceclone.util.assertThatCurrentDestinationIsEqualTo
import com.example.appcommerceclone.util.assertThatCurrentDestinationIsNotEqualTo
import com.example.appcommerceclone.util.assertThatLoadingStateIsEqualTo
import com.example.appcommerceclone.util.firstUser
import com.example.appcommerceclone.util.getOrAwaitValue
import com.example.appcommerceclone.util.hasTextInputLayoutErrorText
import com.example.appcommerceclone.util.launchFragmentInHiltContainer
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
import javax.inject.Inject

@UninstallModules(UsersModule::class, DispatcherModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class LoginFragmentLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testNavHostControllerRule = TestNavHostControllerRule(R.id.login_fragment)

    @get:Rule(order = 2)
    val testMainDispatcherRule = TestMainDispatcherRule()

    @Inject
    lateinit var userAuthenticator: UserAuthenticator

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var navHostController: TestNavHostController
    private lateinit var userViewModel: UserViewModel
    private lateinit var factory: TestFragmentFactory

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
        userViewModel = UserViewModel(userAuthenticator, dispatcherProvider)
        factory = TestFragmentFactory(userViewModelTest = userViewModel)
    }

    @Test
    fun launchUserLoginFragment_loginWithNoUsernameAndPassword_stayInLoginFragment() = runTest {
        launchFragmentInHiltContainer<LoginFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.login_fragment)
            userViewModel.assertThatLoadingStateIsEqualTo(LoadingState.NOT_STARTED)

            onView(withId(R.id.login_username_text))
                .perform(replaceText(""))

            onView(withId(R.id.login_password_text))
                .perform(replaceText(""))

            onView(withId(R.id.login_btn))
                .perform(noConstraintsClick())

            advanceUntilIdle()

            userViewModel.assertThatLoadingStateIsEqualTo(LoadingState.NOT_STARTED)

            onView(withId(R.id.login_username))
                .check(matches(hasTextInputLayoutErrorText(getString(R.string.error_no_username))))

            onView(withId(R.id.login_password))
                .check(matches(hasTextInputLayoutErrorText(getString(R.string.error_no_password))))

            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.login_fragment)
        }
    }

    @Test
    fun launchUserLoginFragment_loginWithWrongUsernameAndPassword_stayInLoginFragment() = runTest {
        launchFragmentInHiltContainer<LoginFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.login_fragment)
            userViewModel.assertThatLoadingStateIsEqualTo(LoadingState.NOT_STARTED)

            onView(withId(R.id.login_username_text))
                .perform(replaceText("wrong username"))

            onView(withId(R.id.login_password_text))
                .perform(replaceText("wrong password"))

            onView(withId(R.id.login_btn))
                .perform(noConstraintsClick())

            advanceUntilIdle()

            userViewModel.assertThatLoadingStateIsEqualTo(LoadingState.FAILURE)

            onView(withText(getString(R.string.login_failure_message)))
                .check(matches(isDisplayed()))

            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.login_fragment)
        }
    }

    @Test
    fun launchUserLoginFragment_loginWithCorrectUsernameAndPassword_leaveLoginFragment() = runTest {
        launchFragmentInHiltContainer<LoginFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.login_fragment)
            userViewModel.assertThatLoadingStateIsEqualTo(LoadingState.NOT_STARTED)

            onView(withId(R.id.login_username_text))
                .perform(replaceText(firstUser.username))

            onView(withId(R.id.login_password_text))
                .perform(replaceText(firstUser.password))

            onView(withId(R.id.login_btn))
                .perform(noConstraintsClick())

            advanceUntilIdle()

            userViewModel.assertThatLoadingStateIsEqualTo(LoadingState.SUCCESS)
            val user = userViewModel.currentUser.getOrAwaitValue()
            assertThat(user).isNotNull()
            assertThat(user?.username).isEqualTo(firstUser.username)
            assertThat(user?.password).isEqualTo(firstUser.password)

            onView(withText(getString(R.string.login_success_message, user!!.username)))
                .check(matches(isDisplayed()))

            navHostController.assertThatCurrentDestinationIsNotEqualTo(R.id.login_fragment)
        }
    }

    @Test
    fun launchUserLoginFragment_clickRegisterBtn_navigateToRegisterFragment() {
        launchFragmentInHiltContainer<LoginFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.login_fragment)

            onView(withId(R.id.login_register_btn)).perform(noConstraintsClick())

            navHostController.assertThatCurrentDestinationIsNotEqualTo(R.id.login_fragment)
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.register_fragment)
        }
    }
}