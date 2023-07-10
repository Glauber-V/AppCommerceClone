package com.example.appcommerceclone.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.appcommerceclone.R

@Composable
fun OutlinedTextFieldWithValidation(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    errorMessage: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = colorResource(id = R.color.primaryColor),
        focusedLabelColor = colorResource(id = R.color.primaryColor),
        cursorColor = colorResource(id = R.color.black_900_080),
    )
) {
    var showError by rememberSaveable { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
                showError = newValue.isEmpty()
            },
            modifier = modifier,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = true,
            label = label,
            leadingIcon = leadingIcon,
            placeholder = placeholder,
            trailingIcon = {
                if (showError) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = errorMessage,
                        tint = MaterialTheme.colors.error
                    )
                } else {
                    trailingIcon?.let { it() }
                }
            },
            isError = showError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            colors = colors
        )
        if (showError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun UserEmailOutlinedTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    userEmailText: String,
    onEmailTextChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextFieldWithValidation(
        value = userEmailText,
        onValueChange = onEmailTextChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
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
        errorMessage = stringResource(id = R.string.user_error_no_email),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Composable
fun UserNameOutlinedTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    userNameText: String,
    onUserNameTextChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextFieldWithValidation(
        value = userNameText,
        onValueChange = onUserNameTextChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
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
        errorMessage = stringResource(id = R.string.user_error_no_username),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Composable
fun UserPasswordOutlinedTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    userPasswordText: String,
    onUserPasswordChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextFieldWithValidation(
        value = userPasswordText,
        onValueChange = onUserPasswordChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
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
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}