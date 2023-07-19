package com.example.appcommerceclone.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appcommerceclone.LoadingState
import com.example.appcommerceclone.R
import com.example.appcommerceclone.ui.common.NoConnectionPlaceHolder
import com.example.appcommerceclone.ui.common.PrimaryActionButton
import com.example.appcommerceclone.ui.common.UserNameOutlinedTextField
import com.example.appcommerceclone.ui.common.UserPasswordOutlinedTextField
import com.google.android.material.progressindicator.BaseProgressIndicator.*

@Stable
class LoginScreenUiState {

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
fun rememberLoginScreenUiState(): LoginScreenUiState {
    return remember { LoginScreenUiState() }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    isConnected: Boolean,
    loadingState: LoadingState,
    loginScreenUiState: LoginScreenUiState = rememberLoginScreenUiState(),
    onLoginRequest: (username: String, password: String) -> Unit,
    onRegisterRequest: () -> Unit,
    focusManager: FocusManager = LocalFocusManager.current,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
) {
    if (isConnected) {
        Column(
            modifier = modifier.padding(dimensionResource(id = R.dimen.padding_extra_large)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_large)),
                contentAlignment = Alignment.Center
            ) {
                if (loadingState == LoadingState.LOADING) {
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
                enabled = loadingState != LoadingState.LOADING,
                userNameText = loginScreenUiState.usernameText,
                onUserNameTextChange = { loginScreenUiState.onUsernameChanged(it) },
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
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
                enabled = loadingState != LoadingState.LOADING,
                userPasswordText = loginScreenUiState.passwordText,
                onUserPasswordChange = { loginScreenUiState.onPasswordChanged(it) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
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
                    onLoginRequest(loginScreenUiState.usernameText, loginScreenUiState.passwordText)
                },
                isPrimaryActionEnabled = loginScreenUiState.canLogin() && loadingState != LoadingState.LOADING,
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
    } else {
        NoConnectionPlaceHolder(modifier.fillMaxSize())
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewUserLoginScreen() {
    MaterialTheme {
        LoginScreen(
            isConnected = true,
            loadingState = LoadingState.NOT_STARTED,
            onLoginRequest = { _, _ -> },
            onRegisterRequest = {}
        )
    }
}