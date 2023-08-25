package com.example.appcommerceclone.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.data.dispatcher.FakeDispatcherProvider
import com.example.appcommerceclone.data.user.FakeUserAuthenticator
import com.example.appcommerceclone.data.user.FakeUserProvider
import com.example.appcommerceclone.ui.user.UserViewModel
import com.example.appcommerceclone.util.TestMainDispatcherRule
import com.example.appcommerceclone.util.firstUser
import com.example.appcommerceclone.util.getOrAwaitValue
import com.example.appcommerceclone.util.secondUser
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserViewModelTest {

    @get:Rule(order = 0)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 1)
    val testMainDispatcherRule = TestMainDispatcherRule()

    private lateinit var usersProvider: FakeUserProvider
    private lateinit var dispatcherProvider: FakeDispatcherProvider
    private lateinit var userAuthenticator: FakeUserAuthenticator
    private lateinit var userViewModel: UserViewModel

    @Before
    fun setup() {
        usersProvider = FakeUserProvider()
        dispatcherProvider = FakeDispatcherProvider()
        userAuthenticator = FakeUserAuthenticator(usersProvider)
        userViewModel = UserViewModel(userAuthenticator, dispatcherProvider)
    }

    @Test
    fun loginFailed_wrongUsername_userIsNull() = runTest {
        var user = userViewModel.currentUser.getOrAwaitValue()
        assertThat(user).isNull()

        userViewModel.login(username = "invalid username", password = firstUser.password)
        advanceUntilIdle()

        user = userViewModel.currentUser.getOrAwaitValue()
        assertThat(user).isNull()
    }

    @Test
    fun loginFailed_wrongPassword_userIsNull() = runTest {
        var user = userViewModel.currentUser.getOrAwaitValue()
        assertThat(user).isNull()

        userViewModel.login(username = firstUser.username, password = "invalid password")
        advanceUntilIdle()

        user = userViewModel.currentUser.getOrAwaitValue()
        assertThat(user).isNull()
    }

    @Test
    fun loginSuccess_firstUserFound_userIsNotNull_logout_userIsNull() = runTest {
        var user = userViewModel.currentUser.getOrAwaitValue()
        assertThat(user).isNull()

        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        user = userViewModel.currentUser.getOrAwaitValue()
        assertThat(user).isNotNull()
        assertThat(user?.id).isEqualTo(1)
        assertThat(user?.username).isEqualTo(firstUser.username)
        assertThat(user?.password).isEqualTo(firstUser.password)

        userViewModel.logout()

        user = userViewModel.currentUser.getOrAwaitValue()
        assertThat(user).isNull()
    }

    @Test
    fun loginSuccess_secondUserFound_userIsNotNull_logout_userIsNull() = runTest {
        var user = userViewModel.currentUser.getOrAwaitValue()
        assertThat(user).isNull()

        userViewModel.login(username = secondUser.username, password = secondUser.password)
        advanceUntilIdle()

        user = userViewModel.currentUser.getOrAwaitValue()
        assertThat(user).isNotNull()
        assertThat(user?.id).isEqualTo(2)
        assertThat(user?.username).isEqualTo(secondUser.username)
        assertThat(user?.password).isEqualTo(secondUser.password)

        userViewModel.logout()

        user = userViewModel.currentUser.getOrAwaitValue()
        assertThat(user).isNull()
    }
}