package com.example.appcommerceclone.ui.user

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.user.model.User
import com.example.appcommerceclone.ui.common.PrimaryActionButton
import com.example.appcommerceclone.ui.common.UserNameOutlinedTextField
import com.example.appcommerceclone.ui.common.UserPasswordOutlinedTextField
import com.google.android.material.progressindicator.BaseProgressIndicator.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Stable
class UserLoginState {

    var usernameText by mutableStateOf("")
        private set

    var passwordText by mutableStateOf("")
        private set

    fun onUsernameChanged(username: String) {
        usernameText = username
    }

    fun onPasswordChanged(password: String) {
        passwordText = password
    }

    fun canLogin(): Boolean {
        return usernameText.isNotEmpty() && passwordText.isNotEmpty()
    }
}

@Composable
fun rememberUserLoginState(): UserLoginState {
    return remember { UserLoginState() }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserLoginScreen(
    modifier: Modifier = Modifier,
    user: User?,
    userLoginState: UserLoginState = rememberUserLoginState(),
    isLoading: Boolean,
    isDataLoaded: Boolean,
    onLoginRequest: (username: String, password: String) -> Unit,
    onLoginRequestComplete: () -> Unit,
    onRegisterRequest: () -> Unit,
    context: Context = LocalContext.current,
    focusManager: FocusManager = LocalFocusManager.current,
    snackbarScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
) {
    LaunchedEffect(isDataLoaded) {
        if (isDataLoaded) {
            snackbarScope.launch {
                with(snackbarHostState) {
                    if (user != null) {
                        showSnackbar(message = context.getString(R.string.user_profile_welcome_message, user.username))
                    } else {
                        showSnackbar(message = context.getString(R.string.user_error_not_found))
                    }
                    onLoginRequestComplete()
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(dimensionResource(id = R.dimen.padding_extra_large)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_large)),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(150.dp),
                        strokeWidth = 4.dp,
                        color = colorResource(id = R.color.progress_indicator_color),
                        backgroundColor = colorResource(id = R.color.progress_track_color)
                    )
                }
                Icon(
                    modifier = Modifier.size(125.dp),
                    imageVector = Icons.Filled.Person,
                    contentDescription = null
                )
            }
            UserNameOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.padding_extra_large)),
                userNameText = userLoginState.usernameText,
                onUserNameTextChange = { userLoginState.onUsernameChanged(it) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )
            UserPasswordOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.padding_large)),
                userPasswordText = userLoginState.passwordText,
                onUserPasswordChange = { userLoginState.onPasswordChanged(it) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                )
            )
            PrimaryActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.padding_extra_large)),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_size_small)),
                onPrimaryAction = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onLoginRequest(userLoginState.usernameText, userLoginState.passwordText)
                },
                isPrimaryActionEnabled = userLoginState.canLogin(),
                primaryActionContent = {
                    Text(
                        text = stringResource(id = R.string.user_login_btn),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.button
                    )
                }
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.padding_extra_large)),
                text = stringResource(id = R.string.user_no_account_warning),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.button
            )
            TextButton(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_large)),
                onClick = onRegisterRequest,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = colorResource(id = R.color.primaryColor)
                ),
                content = {
                    Text(
                        text = stringResource(id = R.string.user_register_btn),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.button
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewUserLoginScreen() {
    MaterialTheme {
        UserLoginScreen(
            user = null,
            isLoading = true,
            isDataLoaded = false,
            onLoginRequest = { _, _ -> },
            onLoginRequestComplete = {},
            onRegisterRequest = {}
        )
    }
}