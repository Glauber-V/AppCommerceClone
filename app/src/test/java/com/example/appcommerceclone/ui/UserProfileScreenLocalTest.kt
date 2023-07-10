package com.example.appcommerceclone.ui

import androidx.activity.ComponentActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.printToLog
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.user.FakeUserProvider.Companion.firstUser
import com.example.appcommerceclone.ui.user.UserProfileScreen
import com.example.appcommerceclone.util.getStringResource
import com.example.appcommerceclone.util.showSemanticTreeInConsole
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalComposeUiApi::class)
@RunWith(RobolectricTestRunner::class)
class UserProfileScreenLocalTest {

    @get:Rule(order = 0)
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        showSemanticTreeInConsole()
        composeRule.setContent {
            MaterialTheme {
                UserProfileScreen(
                    user = firstUser,
                    onPictureRequest = {},
                    onUpdateUserProfile = {},
                    onLogout = {}
                )
            }
        }
    }

    @Test
    fun onUserProfileScreen_cancelProfileUpdate_userStateRestored() {

        with(composeRule) {

            onRoot().printToLog("onUserProfileScreen|CancelProfileUpdate")

            onNodeWithText(getStringResource(R.string.user_profile_start_update_btn))
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()

            onNodeWithText(firstUser.username)
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .performTextReplacement("new username")

            onNodeWithText(firstUser.username).assertDoesNotExist()

            onNodeWithText("new username")
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText(getStringResource(R.string.user_profile_cancel_update_btn))
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()

            onNodeWithText("new username").assertDoesNotExist()

            onNodeWithText(firstUser.username)
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
        }
    }

    @Test
    fun onUserProfileScreen_finishProfileUpdate_userStateUpdated() {

        with(composeRule) {

            onRoot().printToLog("onUserProfileScreen|FinishProfileUpdate")

            onNodeWithText(getStringResource(R.string.user_profile_cancel_update_btn))
                .assertDoesNotExist()

            onNodeWithText(getStringResource(R.string.user_profile_save_update_btn))
                .assertDoesNotExist()

            onNodeWithText(getStringResource(R.string.user_profile_start_update_btn))
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()

            onNodeWithText(getStringResource(R.string.user_profile_cancel_update_btn))
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()

            onNodeWithText(getStringResource(R.string.user_profile_save_update_btn))
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()

            onNodeWithText(getStringResource(R.string.user_profile_start_update_btn))
                .assertDoesNotExist()

            onNodeWithText(firstUser.username)
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .performTextReplacement("new username")

            onNodeWithText(firstUser.username).assertDoesNotExist()

            onNodeWithText("new username")
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText(getStringResource(R.string.user_profile_save_update_btn))
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()

            onNodeWithText(firstUser.username).assertDoesNotExist()

            onNodeWithText("new username")
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()

            onNodeWithText(getStringResource(R.string.user_profile_cancel_update_btn))
                .assertDoesNotExist()

            onNodeWithText(getStringResource(R.string.user_profile_save_update_btn))
                .assertDoesNotExist()

            onNodeWithText(getStringResource(R.string.user_profile_start_update_btn))
                .assertExists()
        }
    }
}