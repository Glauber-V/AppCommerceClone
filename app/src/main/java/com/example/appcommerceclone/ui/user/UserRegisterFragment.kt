package com.example.appcommerceclone.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.appcommerceclone.R
import com.example.appcommerceclone.ui.common.PrimaryActionButton
import com.example.appcommerceclone.ui.common.UserEmailOutlinedTextField
import com.example.appcommerceclone.ui.common.UserNameOutlinedTextField
import com.example.appcommerceclone.ui.common.UserPasswordOutlinedTextField
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserRegisterFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                UserRegisterScreen(
                    onRegisterRequest = {
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
    onRegisterRequest: () -> Unit
) {
    val context = LocalContext.current
    val snackbarScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var userEmailText by rememberSaveable { mutableStateOf("") }
    var userNameText by rememberSaveable { mutableStateOf("") }
    var userPasswordText by rememberSaveable { mutableStateOf("") }

    val canRegister = userEmailText.isNotEmpty()
            && userNameText.isNotEmpty()
            && userPasswordText.isNotEmpty()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(dimensionResource(id = R.dimen.padding_extra_large)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserEmailOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                userEmailText = userEmailText,
                onEmailTextChange = { userEmailText = it }
            )
            UserNameOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.padding_large)),
                userNameText = userNameText,
                onUserNameTextChange = { userNameText = it }
            )
            UserPasswordOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.padding_large)),
                userPasswordText = userPasswordText,
                onUserPasswordChange = { userPasswordText = it }
            )
            PrimaryActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.padding_extra_large)),
                onPrimaryAction = {
                    snackbarScope.launch {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        snackbarHostState.showSnackbar(message = context.getString(R.string.user_register_successfully_message))
                        onRegisterRequest()
                    }
                },
                isPrimaryActionEnabled = canRegister,
                primaryContent = {
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
        UserRegisterScreen(onRegisterRequest = {})
    }
}