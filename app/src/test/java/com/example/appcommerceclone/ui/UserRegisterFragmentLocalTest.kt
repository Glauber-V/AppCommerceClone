package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.appcommerceclone.R
import com.example.appcommerceclone.ui.user.UserRegisterFragment
import com.example.appcommerceclone.util.TestFragmentFactory
import com.example.appcommerceclone.util.TestNavHostControllerRule
import com.example.appcommerceclone.util.hasTextInputLayoutErrorText
import com.example.appcommerceclone.util.launchFragmentInHiltContainer
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class UserRegisterFragmentLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testNavHostControllerRule = TestNavHostControllerRule(R.id.user_register_fragment)

    private lateinit var navHostController: TestNavHostController
    private lateinit var factory: TestFragmentFactory

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
        factory = TestFragmentFactory()
    }

    @Test
    fun launchUserRegisterFragment_clickRegisterBtn_noCredentials_stayInRegisterFragment() {
        launchFragmentInHiltContainer<UserRegisterFragment>(navHostController = navHostController, fragmentFactory = factory) {

            onView(withId(R.id.user_register_email_text))
                .perform(replaceText(""))

            onView(withId(R.id.user_register_username_text))
                .perform(replaceText(""))

            onView(withId(R.id.user_register_password_text))
                .perform(replaceText(""))

            onView(withId(R.id.user_register_btn))
                .perform(click())

            onView(withId(R.id.user_register_email))
                .check(matches(hasTextInputLayoutErrorText(getString(R.string.user_error_no_email))))

            onView(withId(R.id.user_register_username))
                .check(matches(hasTextInputLayoutErrorText(getString(R.string.user_error_no_username))))

            onView(withId(R.id.user_register_password))
                .check(matches(hasTextInputLayoutErrorText(getString(R.string.user_error_no_password))))

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_register_fragment)
        }
    }

    @Test
    fun launchUserRegisterFragment_clickRegisterBtn_withCredentials_navigateBackToProductsFragments() {
        launchFragmentInHiltContainer<UserRegisterFragment>(navHostController = navHostController, fragmentFactory = factory) {

            onView(withId(R.id.user_register_email_text))
                .perform(replaceText("random_2022@hotmail.com"))

            onView(withId(R.id.user_register_username_text))
                .perform(replaceText("RandomUser22"))

            onView(withId(R.id.user_register_password_text))
                .perform(replaceText("12322"))

            onView(withId(R.id.user_register_btn))
                .perform(click())

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.user_register_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.products_fragment)
        }
    }
}