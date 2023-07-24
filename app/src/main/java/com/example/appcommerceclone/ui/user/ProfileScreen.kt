package com.example.appcommerceclone.ui.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.user.model.Address
import com.example.appcommerceclone.data.user.model.Name
import com.example.appcommerceclone.data.user.model.User
import com.example.appcommerceclone.ui.common.DoubleActionButton
import com.example.appcommerceclone.ui.common.OutlinedTextFieldWithValidation
import com.example.appcommerceclone.ui.common.PrimaryActionButton
import com.example.appcommerceclone.ui.common.SecondaryActionButton
import com.example.appcommerceclone.ui.common.UserEmailOutlinedTextField
import com.example.appcommerceclone.ui.common.UserNameOutlinedTextField
import com.example.appcommerceclone.ui.common.UserPasswordOutlinedTextField

@Stable
class ProfileScreenUiState(private val user: User) {

    var isEditMode: Boolean by mutableStateOf(false)
        private set

    var emailText: String by mutableStateOf(user.email)
        private set

    var usernameText: String by mutableStateOf(user.username)
        private set

    var firstNameText: String by mutableStateOf(user.name.firstname)
        private set

    var lastNameText: String by mutableStateOf(user.name.lastname)
        private set

    var passwordText: String by mutableStateOf(user.password)
        private set

    var phoneText: String by mutableStateOf(user.phone)
        private set

    var zipcodeText: String by mutableStateOf(user.address.zipcode)
        private set

    var cityText: String by mutableStateOf(user.address.city)
        private set

    var streetText: String by mutableStateOf(user.address.street)
        private set

    var streetNumber: Int by mutableStateOf(user.address.number)
        private set

    fun onEditModeChange() {
        isEditMode = !isEditMode
    }

    fun onEmailTextChange(value: String) {
        emailText = value
    }

    fun onUsernameTextChange(value: String) {
        usernameText = value
    }

    fun onFirstNameTextChange(value: String) {
        firstNameText = value
    }

    fun onLastNameTextChange(value: String) {
        lastNameText = value
    }

    fun onPasswordTextChange(value: String) {
        passwordText = value
    }

    fun onPhoneTextChange(value: String) {
        phoneText = value
    }

    fun onZipcodeTextChange(value: String) {
        zipcodeText = value
    }

    fun onCityTextChange(value: String) {
        cityText = value
    }

    fun onStreetTextChange(value: String) {
        streetText = value
    }

    fun onNumberTextChange(value: String) {
        streetNumber = value.toIntOrNull() ?: 0
    }

    fun restoreUserState() {
        emailText = user.email
        usernameText = user.username
        firstNameText = user.name.firstname
        lastNameText = user.name.lastname
        passwordText = user.password
        phoneText = user.phone
        zipcodeText = user.address.zipcode
        cityText = user.address.city
        streetText = user.address.street
        streetNumber = user.address.number
    }

    fun validateUpdate(): Boolean {
        return emailText.isNotEmpty()
                && usernameText.isNotEmpty()
                && passwordText.isNotEmpty()
    }

    fun createUpdatedUser(): User {
        return user.copy(
            name = Name(
                firstname = firstNameText,
                lastname = lastNameText
            ),
            username = usernameText,
            password = passwordText,
            email = emailText,
            phone = phoneText,
            address = Address(
                city = cityText,
                number = streetNumber,
                street = streetText,
                zipcode = zipcodeText
            )
        )
    }
}

@Composable
fun rememberProfileScreenUiState(user: User): ProfileScreenUiState {
    return remember(user) { ProfileScreenUiState(user) }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    user: User,
    profileScreenUiState: ProfileScreenUiState = rememberProfileScreenUiState(user),
    onPictureRequest: () -> Unit,
    onUpdateUserProfile: (updatedUser: User) -> Unit,
    onLogout: () -> Unit,
    focusManager: FocusManager = LocalFocusManager.current,
    scrollState: ScrollState = rememberScrollState(),
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
) {
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(dimensionResource(id = R.dimen.padding_large)),
        verticalArrangement = Arrangement.spacedBy(
            space = dimensionResource(id = R.dimen.padding_medium),
            alignment = Alignment.Top
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(200.dp)
                .border(
                    border = BorderStroke(
                        width = dimensionResource(id = R.dimen.stroke_size_small),
                        color = colorResource(id = R.color.stroke_color_dark)
                    ),
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_size_small))
                ),
            imageVector = Icons.Filled.Person,
            contentDescription = null,
            tint = colorResource(id = R.color.icon_color_black)
        )
        if (profileScreenUiState.isEditMode) {
            PrimaryActionButton(
                onPrimaryAction = onPictureRequest,
                primaryActionContent = {
                    Text(
                        modifier = Modifier.width(180.dp),
                        text = stringResource(id = R.string.profile_update_profile_pic),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.button
                    )
                }
            )
        }
        UserEmailOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            readOnly = !profileScreenUiState.isEditMode,
            userEmailText = profileScreenUiState.emailText,
            onEmailTextChange = { profileScreenUiState.onEmailTextChange(it) },
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
            readOnly = !profileScreenUiState.isEditMode,
            userNameText = profileScreenUiState.usernameText,
            onUserNameTextChange = { profileScreenUiState.onUsernameTextChange(it) },
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextFieldWithValidation(
                modifier = Modifier.fillMaxWidth(0.5f),
                readOnly = !profileScreenUiState.isEditMode,
                label = {
                    Text(
                        text = stringResource(id = R.string.hint_name_first_name),
                        style = MaterialTheme.typography.subtitle1
                    )
                },
                value = profileScreenUiState.firstNameText,
                onValueChange = { profileScreenUiState.onFirstNameTextChange(it) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Right)
                    }
                )
            )
            OutlinedTextFieldWithValidation(
                modifier = Modifier.fillMaxWidth(),
                readOnly = !profileScreenUiState.isEditMode,
                label = {
                    Text(
                        text = stringResource(id = R.string.hint_name_last_name),
                        style = MaterialTheme.typography.subtitle1
                    )
                },
                value = profileScreenUiState.lastNameText,
                onValueChange = { profileScreenUiState.onLastNameTextChange(it) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                )
            )
        }
        UserPasswordOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            readOnly = !profileScreenUiState.isEditMode,
            userPasswordText = profileScreenUiState.passwordText,
            onUserPasswordChange = { profileScreenUiState.onPasswordTextChange(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )
        OutlinedTextFieldWithValidation(
            modifier = Modifier.fillMaxWidth(),
            readOnly = !profileScreenUiState.isEditMode,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = null,
                    tint = colorResource(id = R.color.icon_color_black)
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.hint_phone),
                    style = MaterialTheme.typography.subtitle1
                )
            },
            value = profileScreenUiState.phoneText,
            onValueChange = { profileScreenUiState.onPhoneTextChange(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )
        OutlinedTextFieldWithValidation(
            modifier = Modifier.fillMaxWidth(),
            readOnly = !profileScreenUiState.isEditMode,
            label = {
                Text(
                    text = stringResource(id = R.string.hint_address_zipcode),
                    style = MaterialTheme.typography.subtitle1
                )
            },
            value = profileScreenUiState.zipcodeText,
            onValueChange = { profileScreenUiState.onZipcodeTextChange(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )
        OutlinedTextFieldWithValidation(
            modifier = Modifier.fillMaxWidth(),
            readOnly = !profileScreenUiState.isEditMode,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.LocationCity,
                    contentDescription = null,
                    tint = colorResource(id = R.color.icon_color_black)
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.hint_address_city),
                    style = MaterialTheme.typography.subtitle1
                )
            },
            value = profileScreenUiState.cityText,
            onValueChange = { profileScreenUiState.onCityTextChange(it) },
            keyboardOptions = KeyboardOptions(
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextFieldWithValidation(
                modifier = Modifier.fillMaxWidth(0.6f),
                readOnly = !profileScreenUiState.isEditMode,
                label = {
                    Text(
                        text = stringResource(id = R.string.hint_address_street),
                        style = MaterialTheme.typography.subtitle1
                    )
                },
                value = profileScreenUiState.streetText,
                onValueChange = { profileScreenUiState.onStreetTextChange(it) },
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Right)
                    }
                )
            )
            OutlinedTextFieldWithValidation(
                modifier = Modifier.fillMaxWidth(),
                readOnly = !profileScreenUiState.isEditMode,
                label = {
                    Text(
                        text = stringResource(id = R.string.hint_address_number),
                        style = MaterialTheme.typography.subtitle1
                    )
                },
                value = profileScreenUiState.streetNumber.toString(),
                onValueChange = { profileScreenUiState.onNumberTextChange(it) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        if (profileScreenUiState.isEditMode) profileScreenUiState.onEditModeChange()
                    }
                )
            )
        }
        if (!profileScreenUiState.isEditMode) {
            PrimaryActionButton(
                modifier = Modifier.fillMaxWidth(),
                onPrimaryAction = { profileScreenUiState.onEditModeChange() },
                primaryActionContent = {
                    Text(
                        text = stringResource(id = R.string.profile_start_update_btn),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.button
                    )
                }
            )
        } else {
            DoubleActionButton(
                modifier = Modifier.fillMaxWidth(),
                onPrimaryAction = {
                    onUpdateUserProfile(profileScreenUiState.createUpdatedUser())
                    profileScreenUiState.onEditModeChange()
                },
                isPrimaryActionEnabled = profileScreenUiState.validateUpdate(),
                primaryActionContent = {
                    Text(
                        text = stringResource(id = R.string.profile_save_update_btn),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.button
                    )
                },
                onSecondaryAction = {
                    profileScreenUiState.restoreUserState()
                    profileScreenUiState.onEditModeChange()
                },
                secondaryActionContent = {
                    Text(
                        text = stringResource(id = R.string.profile_cancel_update_btn),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.button
                    )
                }
            )
        }
        SecondaryActionButton(
            modifier = Modifier.fillMaxWidth(),
            onSecondaryAction = onLogout,
            secondaryActionContent = {
                Text(
                    text = stringResource(id = R.string.profile_logout_btn),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.button
                )
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewUserProfileScreenWithUser() {
    MaterialTheme {
        ProfileScreen(
            user = User(
                id = 3708,
                name = Name(
                    firstname = "User firstname",
                    lastname = "User lastname"
                ),
                username = "RegularUser",
                password = "123456",
                email = "regularUser@example.com",
                phone = "(555) 715-9696",
                address = Address(
                    city = "City",
                    number = 8211,
                    street = "City street",
                    zipcode = "57314"
                )
            ),
            onPictureRequest = {},
            onUpdateUserProfile = {},
            onLogout = {}
        )
    }
}