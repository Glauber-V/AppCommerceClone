package com.example.appcommerceclone.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.data.dispatcher.FakeDispatcherProvider
import com.example.appcommerceclone.data.user.FakeUserAuthenticator
import com.example.appcommerceclone.data.user.FakeUserProvider
import com.example.appcommerceclone.data.user.FakeUserProvider.Companion.firstUser
import com.example.appcommerceclone.data.user.FakeUserProvider.Companion.secondUser
import com.example.appcommerceclone.util.TestMainDispatcherRule
import com.example.appcommerceclone.util.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
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
        userAuthenticator = FakeUserAuthenticator(usersProvider, dispatcherProvider)
        userViewModel = UserViewModel(userAuthenticator, dispatcherProvider)
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

        user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNull()
    }
}