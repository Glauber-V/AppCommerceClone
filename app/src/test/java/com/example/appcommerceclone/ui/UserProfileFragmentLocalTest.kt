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
import com.example.appcommerceclone.ui.user.UserProfileFragment
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
class UserProfileFragmentLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testNavHostControllerRule = TestNavHostControllerRule(R.id.user_profile_fragment)

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
    fun launchUserProfileFragment_noUser_navigateToLoginFragment() = runTest {

        val user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNull()

        launchFragmentInHiltContainer<UserProfileFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.user_profile_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)
        }
    }

    @Test
    fun launchUserProfileFragment_withUser_updateProfile_success() = runTest {

        var user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNull()

        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNotNull()
        assertThat(user?.username).isEqualTo(firstUser.username)
        assertThat(user?.password).isEqualTo(firstUser.password)

        launchFragmentInHiltContainer<UserProfileFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_profile_fragment)

            onView(withId(R.id.user_profile_save_or_update_btn))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click())

            onView(withId(R.id.user_profile_first_name_text))
                .perform(scrollTo())
                .perform(replaceText("Temp_Name"))

            onView(withText(getString(R.string.user_profile_save_update_btn)))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click())

            onView(withId(R.id.user_profile_first_name_text))
                .perform(scrollTo())
                .check(matches(withText("Temp_Name")))

            onView(withId(R.id.user_profile_save_or_update_btn))
                .perform(scrollTo())
                .check(matches(withText(getString(R.string.user_profile_start_update_btn))))
        }
    }

    @Test
    fun launchUserProfileFragment_withUser_updateProfile_failed() = runTest {

        var user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNull()

        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNotNull()
        assertThat(user?.username).isEqualTo(firstUser.username)
        assertThat(user?.password).isEqualTo(firstUser.password)

        launchFragmentInHiltContainer<UserProfileFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_profile_fragment)

            onView(withText(getString(R.string.user_profile_start_update_btn)))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click())

            onView(withId(R.id.user_profile_email_text))
                .perform(scrollTo())
                .perform(replaceText(""))

            onView(withText(getString(R.string.user_profile_save_update_btn)))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click())

            onView(withId(R.id.user_profile_email))
                .perform(scrollTo())
                .check(matches(hasTextInputLayoutErrorText(getString(R.string.user_error_no_email))))

            onView(withId(R.id.user_profile_save_or_update_btn))
                .perform(scrollTo())
                .check(matches(withText(getString(R.string.user_profile_save_update_btn))))
        }
    }

    @Test
    fun launchUserProfileFragment_logout_navigateToUserLoginFragment() = runTest {

        var user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNull()

        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNotNull()
        assertThat(user?.username).isEqualTo(firstUser.username)
        assertThat(user?.password).isEqualTo(firstUser.password)

        launchFragmentInHiltContainer<UserProfileFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_profile_fragment)

            onView(withId(R.id.user_profile_logout_btn))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click())

            advanceUntilIdle()

            user = userViewModel.loggedUser.getOrAwaitValue()
            assertThat(user).isNull()

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.user_profile_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)
        }
    }
}