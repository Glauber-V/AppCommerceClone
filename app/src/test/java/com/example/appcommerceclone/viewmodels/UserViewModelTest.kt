package com.example.appcommerceclone.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.appcommerceclone.data.dispatcher.FakeDispatcherProvider
import com.example.appcommerceclone.data.user.*
import com.example.appcommerceclone.data.user.FakeUserProvider.Companion.firstUser
import com.example.appcommerceclone.data.user.FakeUserProvider.Companion.secondUser
import com.example.appcommerceclone.data.user.UserPreferencesKeys.USER_PREF_ID_KEY
import com.example.appcommerceclone.util.TestMainDispatcherRule
import com.example.appcommerceclone.util.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class UserViewModelTest {

    @get:Rule(order = 0)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 1)
    val testMainDispatcherRule = TestMainDispatcherRule()

    private lateinit var usersProvider: FakeUserProvider
    private lateinit var dispatcherProvider: FakeDispatcherProvider
    private lateinit var userAuthenticator: FakeUserAuthenticator
    private lateinit var userPreferences: FakeUserPreferences
    private lateinit var userViewModel: UserViewModel

    @Before
    fun setup() {
        usersProvider = FakeUserProvider()
        dispatcherProvider = FakeDispatcherProvider()
        userAuthenticator = FakeUserAuthenticator(usersProvider, dispatcherProvider)
        userPreferences = FakeUserPreferences(getApplicationContext(), dispatcherProvider)
        userViewModel = UserViewModel(userAuthenticator, userPreferences, dispatcherProvider)
    }

    @Test
    fun loginFailed_wrongUsername_userIsNull() = runTest {
        var user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNull()

        userViewModel.login(username = "invalid username", password = firstUser.password)
        advanceUntilIdle()

        user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNull()
    }

    @Test
    fun loginFailed_wrongPassword_userIsNull() = runTest {
        var user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNull()

        userViewModel.login(username = firstUser.username, password = "invalid password")
        advanceUntilIdle()

        user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNull()
    }

    @Test
    fun loginSuccess_userFound_1stUser_userIsNotNull() = runTest {
        var user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNull()

        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNotNull()
        assertThat(user?.id).isEqualTo(1)
        assertThat(user?.username).isEqualTo(firstUser.username)
        assertThat(user?.password).isEqualTo(firstUser.password)

        val userKey = userPreferences.getIntValueFromKey(USER_PREF_ID_KEY)
        advanceUntilIdle()
        assertThat(userKey).isEqualTo(user?.id)
    }

    @Test
    fun loginSuccess_userFound_2ndUser_userIsNotNull() = runTest {
        var user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNull()

        userViewModel.login(username = secondUser.username, password = secondUser.password)
        advanceUntilIdle()

        user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNotNull()
        assertThat(user?.id).isEqualTo(2)
        assertThat(user?.username).isEqualTo(secondUser.username)
        assertThat(user?.password).isEqualTo(secondUser.password)

        val userKey = userPreferences.getIntValueFromKey(USER_PREF_ID_KEY)
        advanceUntilIdle()
        assertThat(userKey).isEqualTo(user?.id)
    }

    @Test
    fun loginSuccess_userFound_withSavedUser() = runTest {
        var user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNull()

        userPreferences.saveIntValueToKey(USER_PREF_ID_KEY, firstUser.id)
        advanceUntilIdle()

        userViewModel.loadSavedUser()
        advanceUntilIdle()

        user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNotNull()
        assertThat(user?.username).isEqualTo(firstUser.username)
        assertThat(user?.password).isEqualTo(firstUser.password)

        val userKey = userPreferences.getIntValueFromKey(USER_PREF_ID_KEY)
        advanceUntilIdle()
        assertThat(userKey).isEqualTo(user?.id)
    }

    @Test
    fun loginSuccess_userFound_logout_userIsNull() = runTest {
        var user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNull()

        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNotNull()
        assertThat(user).isEqualTo(firstUser)

        userViewModel.logout()
        advanceUntilIdle()

        user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNull()

        // This test succeeds but causes an IOException when running in local JVM
        // https://github.com/googlecodelabs/android-datastore/issues/48
    }
}