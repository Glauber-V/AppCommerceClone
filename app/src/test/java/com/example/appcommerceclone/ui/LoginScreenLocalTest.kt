package com.example.appcommerceclone.ui

import androidx.activity.ComponentActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.printToLog
import com.example.appcommerceclone.LoadingState
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.user.FakeUserProvider.Companion.firstUser
import com.example.appcommerceclone.data.user.UserAuthenticator
import com.example.appcommerceclone.data.user.model.User
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.UsersModule
import com.example.appcommerceclone.ui.user.LoginScreen
import com.example.appcommerceclone.ui.user.UserViewModel
import com.example.appcommerceclone.util.TestMainDispatcherRule
import com.example.appcommerceclone.util.getStringResource
import com.example.appcommerceclone.util.showSemanticTreeInConsole
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
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalComposeUiApi::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class LoginScreenLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testMainDispatcherRule = TestMainDispatcherRule()

    @get:Rule(order = 2)
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Inject
    lateinit var userAuthenticator: UserAuthenticator

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var userViewModel: UserViewModel

    private lateinit var loadingState: State<LoadingState>
    private lateinit var user: State<User?>

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        showSemanticTreeInConsole()
        composeRule.setContent {
            MaterialTheme {
                userViewModel = UserViewModel(userAuthenticator, dispatcherProvider)
                loadingState = userViewModel.loadingState.observeAsState(initial = LoadingState.NOT_STARTED)
                user = userViewModel.loggedUser.observeAsState(initial = null)
                LoginScreen(
                    loadingState = loadingState.value,
                    onLoginRequest = { username: String, password: String ->
                        userViewModel.login(username, password)
                    },
                    onRegisterRequest = {}
                )
            }
        }
    }

    @Test
    fun onUserLoginScreen_wrongCredentials_failedLogin() = runTest {
        with(composeRule) {
            onRoot().printToLog("onUserLoginScreen|FailedLogin")

            assertThat(loadingState.value).isEqualTo(LoadingState.NOT_STARTED)
            assertThat(user.value).isNull()

            onNodeWithText(getStringResource(R.string.hint_username))
                .assertExists()
                .assertIsDisplayed()
                .performTextReplacement("wrong username")

            onNodeWithText(getStringResource(R.string.hint_password))
                .assertExists()
                .assertIsDisplayed()
                .performTextReplacement("wrong password")

            onNodeWithText(getStringResource(R.string.login_btn))
                .assertExists()
                .assertIsDisplayed()
                .performClick()

            advanceUntilIdle()

            assertThat(loadingState.value).isEqualTo(LoadingState.FAILURE)
            assertThat(user.value).isNull()
        }
    }

    @Test
    fun onUserLoginScreen_correctCredentials_successfulLogin() = runTest {
        with(composeRule) {
            onRoot().printToLog("onUserLoginScreen|SuccessfulLogin")

            assertThat(loadingState.value).isEqualTo(LoadingState.NOT_STARTED)
            assertThat(user.value).isNull()

            onNodeWithText(getStringResource(R.string.hint_username))
                .assertExists()
                .assertIsDisplayed()
                .performTextReplacement(firstUser.username)

            onNodeWithText(getStringResource(R.string.hint_password))
                .assertExists()
                .assertIsDisplayed()
                .performTextReplacement(firstUser.password)

            onNodeWithText(getStringResource(R.string.login_btn))
                .assertExists()
                .assertIsDisplayed()
                .performClick()

            advanceUntilIdle()

            assertThat(loadingState.value).isEqualTo(LoadingState.SUCCESS)
            assertThat(user.value).isNotNull()
            assertThat(user.value).isEqualTo(firstUser)
        }
    }
}