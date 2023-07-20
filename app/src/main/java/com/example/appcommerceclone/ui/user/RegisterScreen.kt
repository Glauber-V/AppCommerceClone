package com.example.appcommerceclone.ui.user

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.appcommerceclone.R
import com.example.appcommerceclone.ui.common.PrimaryActionButton
import com.example.appcommerceclone.ui.common.UserEmailOutlinedTextField
import com.example.appcommerceclone.ui.common.UserNameOutlinedTextField
import com.example.appcommerceclone.ui.common.UserPasswordOutlinedTextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Stable
class RegisterScreenUiState {

    var userEmailText by mutableStateOf("")
        private set

    var usernameText by mutableStateOf("")
        private set

    var userPasswordText by mutableStateOf("")
        private set

    fun onEmailTextChanged(email: String) {
        userEmailText = email
    }

    fun onUsernameTextChanged(username: String) {
        usernameText = username
    }

    fun onPasswordTextChanged(password: String) {
        userPasswordText = password
    }

    fun canRegister(): Boolean {
        return userEmailText.isNotEmpty() && usernameText.isNotEmpty() && userPasswordText.isNotEmpty()
    }
}

@Composable
fun rememberRegisterScreenUiState(): RegisterScreenUiState {
    return remember { RegisterScreenUiState() }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    registerScreenUiState: RegisterScreenUiState = rememberRegisterScreenUiState(),
    onRegisterRequest: () -> Unit,
    context: Context = LocalContext.current,
    focusManager: FocusManager = LocalFocusManager.current,
    snackbarScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(dimensionResource(id = R.dimen.padding_extra_large)),
            verticalArrangement = Arrangement.spacedBy(
                space = dimensionResource(id = R.dimen.padding_medium),
                alignment = Alignment.Top
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserEmailOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                userEmailText = registerScreenUiState.userEmailText,
                onEmailTextChange = { registerScreenUiState.onEmailTextChanged(it) },
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )
            UserNameOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                userNameText = registerScreenUiState.usernameText,
                onUserNameTextChange = { registerScreenUiState.onUsernameTextChanged(it) },
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
                modifier = Modifier.fillMaxWidth(),
                userPasswordText = registerScreenUiState.userPasswordText,
                onUserPasswordChange = { registerScreenUiState.onPasswordTextChanged(it) },
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
                    snackbarScope.launch {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        snackbarHostState.showSnackbar(message = context.getString(R.string.register_success_message))
                        onRegisterRequest()
                    }
                },
                isPrimaryActionEnabled = registerScreenUiState.canRegister(),
                primaryActionContent = {
                    Text(
                        text = stringResource(id = R.string.register_btn),
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
fun PreviewUserRegisterScreen() {
    MaterialTheme {
        RegisterScreen(onRegisterRequest = {})
    }
}