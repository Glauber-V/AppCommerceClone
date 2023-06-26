package com.example.appcommerceclone.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.appcommerceclone.R
import com.example.appcommerceclone.ui.common.OutlinedTextFieldWithValidation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserRegisterFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                UserRegisterScreen(
                    onRegister = {
                        findNavController().navigateUp()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserRegisterScreen(
    modifier: Modifier = Modifier,
    onRegister: () -> Unit
) {
    val context = LocalContext.current
    val snackbarScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var userEmailText by rememberSaveable { mutableStateOf("") }
    var userNameText by rememberSaveable { mutableStateOf("") }
    var userPasswordText by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { contentPadding ->
        Column(
            modifier = modifier
                .padding(contentPadding)
                .padding(dimensionResource(id = R.dimen.padding_extra_large)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextFieldWithValidation(
                value = userEmailText,
                onValueChange = { userEmailText = it },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = null,
                        tint = colorResource(id = R.color.icon_color_black)
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.user_hint_email),
                        style = MaterialTheme.typography.subtitle1
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.user_hint_email),
                        style = MaterialTheme.typography.subtitle1
                    )
                },
                errorMessage = stringResource(id = R.string.user_error_no_email)
            )
            OutlinedTextFieldWithValidation(
                value = userNameText,
                onValueChange = { userNameText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.padding_large)),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = colorResource(id = R.color.icon_color_black)
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.user_hint_username),
                        style = MaterialTheme.typography.subtitle1
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.user_hint_username),
                        style = MaterialTheme.typography.subtitle1
                    )
                },
                errorMessage = stringResource(id = R.string.user_error_no_username)
            )
            OutlinedTextFieldWithValidation(
                value = userPasswordText,
                onValueChange = { userPasswordText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.padding_large)),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = colorResource(id = R.color.icon_color_black)
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.user_hint_password),
                        style = MaterialTheme.typography.subtitle1
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.user_hint_password),
                        style = MaterialTheme.typography.subtitle1
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            isPasswordVisible = !isPasswordVisible
                        },
                        content = {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Filled.VisibilityOff
                                else Icons.Filled.Visibility,
                                contentDescription = if (isPasswordVisible) stringResource(id = R.string.content_desc_hide_password)
                                else stringResource(id = R.string.content_desc_show_password),
                                tint = colorResource(id = R.color.icon_color_black)
                            )
                        }
                    )
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                errorMessage = stringResource(id = R.string.user_error_no_password),
            )
            TextButton(
                onClick = {
                    snackbarScope.launch {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        snackbarHostState.showSnackbar(message = context.getString(R.string.user_register_successfully_message))
                        onRegister()
                    }
                },
                enabled = userEmailText.isNotEmpty() && userNameText.isNotEmpty() && userPasswordText.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.btn_min_height_size))
                    .padding(top = dimensionResource(id = R.dimen.padding_large)),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.primaryColor),
                    contentColor = colorResource(id = R.color.white_100)
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewUserRegisterScreen() {
    MaterialTheme {
        UserRegisterScreen(onRegister = {})
    }
}