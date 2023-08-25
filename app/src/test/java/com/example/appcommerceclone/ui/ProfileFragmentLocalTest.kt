package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.user.UserAuthenticator
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.UsersModule
import com.example.appcommerceclone.ui.user.ProfileFragment
import com.example.appcommerceclone.ui.user.UserViewModel
import com.example.appcommerceclone.util.LoadingState
import com.example.appcommerceclone.util.TestMainDispatcherRule
import com.example.appcommerceclone.util.TestNavHostControllerRule
import com.example.appcommerceclone.util.assertThatCurrentDestinationIsEqualTo
import com.example.appcommerceclone.util.assertThatCurrentDestinationIsNotEqualTo
import com.example.appcommerceclone.util.assertThatLoadingStateIsEqualTo
import com.example.appcommerceclone.util.assertThatUserWasLoadedCorrectly
import com.example.appcommerceclone.util.firstUser
import com.example.appcommerceclone.util.getOrAwaitValue
import com.example.appcommerceclone.util.hasTextInputLayoutErrorText
import com.example.appcommerceclone.util.launchFragmentInHiltContainer
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.core.IsNot.not
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
class ProfileFragmentLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testNavHostControllerRule = TestNavHostControllerRule(R.id.profile_fragment)

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
    fun launchUserProfileFragment_noUser_navigateToLoginFragment() = runTest {
        val user = userViewModel.currentUser.getOrAwaitValue()
        assertThat(user).isNull()

        userViewModel.assertThatLoadingStateIsEqualTo(LoadingState.NOT_STARTED)

        launchFragmentInHiltContainer<ProfileFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsNotEqualTo(R.id.profile_fragment)
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.login_fragment)
        }
    }

    @Test
    fun launchUserProfileFragment_withUser_updateProfile_cancel_restoreUserState() = runTest {
        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        userViewModel.assertThatUserWasLoadedCorrectly(firstUser)

        launchFragmentInHiltContainer<ProfileFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.profile_fragment)

            onView(withId(R.id.profile_save_or_update_btn))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click())

            onView(withId(R.id.profile_first_name_text))
                .perform(scrollTo())
                .perform(replaceText("Temp_Name"))
                .check(matches(withText("Temp_Name")))

            onView(withText(getString(R.string.profile_cancel_update_btn)))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click())

            onView(withId(R.id.profile_first_name_text))
                .perform(scrollTo())
                .check(matches(not(withText("Temp_Name"))))
                .check(matches(withText(firstUser.name.firstname)))
        }
    }

    @Test
    fun launchUserProfileFragment_withUser_updateProfile_success_saveUserState() = runTest {
        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        userViewModel.assertThatUserWasLoadedCorrectly(firstUser)

        launchFragmentInHiltContainer<ProfileFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.profile_fragment)

            onView(withId(R.id.profile_save_or_update_btn))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click())

            onView(withId(R.id.profile_first_name_text))
                .perform(scrollTo())
                .perform(replaceText("Temp_Name"))
                .check(matches(withText("Temp_Name")))

            onView(withText(getString(R.string.profile_save_update_btn)))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click())

            onView(withId(R.id.profile_first_name_text))
                .perform(scrollTo())
                .check(matches(not(withText(firstUser.name.firstname))))
                .check(matches(withText("Temp_Name")))
        }
    }

    @Test
    fun launchUserProfileFragment_withUser_updateProfile_failure_missingRequiredFields() = runTest {
        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        userViewModel.assertThatUserWasLoadedCorrectly(firstUser)

        launchFragmentInHiltContainer<ProfileFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.profile_fragment)

            onView(withId(R.id.profile_save_or_update_btn))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click())

            onView(withId(R.id.profile_email_text))
                .perform(scrollTo())
                .perform(replaceText(""))
                .check(matches(withText("")))

            onView(withId(R.id.profile_username_text))
                .perform(scrollTo())
                .perform(replaceText(""))
                .check(matches(withText("")))

            onView(withId(R.id.profile_password_text))
                .perform(scrollTo())
                .perform(replaceText(""))
                .check(matches(withText("")))

            onView(withText(getString(R.string.profile_save_update_btn)))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click())

            onView(withId(R.id.profile_email))
                .perform(scrollTo())
                .check(matches(hasTextInputLayoutErrorText(getString(R.string.error_no_email))))

            onView(withId(R.id.profile_username))
                .perform(scrollTo())
                .check(matches(hasTextInputLayoutErrorText(getString(R.string.error_no_username))))

            onView(withId(R.id.profile_password))
                .perform(scrollTo())
                .check(matches(hasTextInputLayoutErrorText(getString(R.string.error_no_password))))
        }
    }

    @Test
    fun launchUserProfileFragment_logout_navigateToUserLoginFragment() = runTest {
        var user = userViewModel.currentUser.getOrAwaitValue()
        assertThat(user).isNull()

        userViewModel.assertThatLoadingStateIsEqualTo(LoadingState.NOT_STARTED)

        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        userViewModel.assertThatLoadingStateIsEqualTo(LoadingState.SUCCESS)

        user = userViewModel.currentUser.getOrAwaitValue()
        assertThat(user).isNotNull()
        assertThat(user?.username).isEqualTo(firstUser.username)
        assertThat(user?.password).isEqualTo(firstUser.password)

        launchFragmentInHiltContainer<ProfileFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.profile_fragment)

            onView(withId(R.id.profile_logout_btn))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click())

            advanceUntilIdle()

            user = userViewModel.currentUser.getOrAwaitValue()
            assertThat(user).isNull()

            navHostController.assertThatCurrentDestinationIsNotEqualTo(R.id.profile_fragment)
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.login_fragment)

            userViewModel.assertThatLoadingStateIsEqualTo(LoadingState.NOT_STARTED)
        }
    }
}