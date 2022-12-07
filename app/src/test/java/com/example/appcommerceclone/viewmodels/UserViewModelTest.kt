package com.example.appcommerceclone.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.appcommerceclone.data.dispatcher.FakeDispatcherProvider
import com.example.appcommerceclone.data.user.FakeUserAuthenticator
import com.example.appcommerceclone.data.user.FakeUserPreferences
import com.example.appcommerceclone.data.user.UserPreferencesKeys.USER_PREF_ID_KEY
import com.example.appcommerceclone.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
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

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeUserAuthenticator: FakeUserAuthenticator
    private lateinit var fakeUserPreferences: FakeUserPreferences
    private lateinit var userViewModel: UserViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        fakeUserAuthenticator = FakeUserAuthenticator()
        fakeUserPreferences = FakeUserPreferences(getApplicationContext())
        userViewModel = UserViewModel(fakeUserAuthenticator, fakeUserPreferences)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `failed login with wrong username`() = runTest{
        userViewModel.login("Oris", "321")
        advanceUntilIdle()

        val currentUser = userViewModel.loggedUser.getOrAwaitValue()

        assertThat(currentUser).isNull()
    }

    @Test
    fun `failed login with wrong password`() = runTest {
        userViewModel.login("Orisa", "3")
        advanceUntilIdle()

        val currentUser = userViewModel.loggedUser.getOrAwaitValue()

        assertThat(currentUser).isNull()
    }

    @Test
    fun `successfully logged in`() = runTest {
        userViewModel.login("Orisa", "321")
        advanceUntilIdle()

        val currentUser = userViewModel.loggedUser.getOrAwaitValue()

        assertThat(currentUser).isNotNull()
        assertThat(currentUser?.username).isEqualTo("Orisa")
        assertThat(currentUser?.password).isEqualTo("321")
    }

    @Test
    fun `successfully log in by loading saved user id in the datastore`() = runTest {
        fakeUserPreferences.saveIntValueToKey(USER_PREF_ID_KEY, 1)

        userViewModel.loadSavedUser()
        advanceUntilIdle()

        val currentUser = userViewModel.loggedUser.getOrAwaitValue()

        assertThat(currentUser).isNotNull()
        assertThat(currentUser?.username).isEqualTo("Orisa")
        assertThat(currentUser?.password).isEqualTo("321")
    }

    @Test
    fun `successfully logout`() = runTest {
        // Can't use this here right now: userViewModel.login("Orisa", "321")
        // Apparently there is an issue with datastore if a value is updated
        // more than once in unit tests:
        // https://github.com/googlecodelabs/android-datastore/issues/48

        userViewModel.logout()

        val currentUser = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(currentUser).isNull()

        val userIdPref = fakeUserPreferences.getIntValueFromKey(USER_PREF_ID_KEY)
        assertThat(userIdPref).isEqualTo(0)
    }
}