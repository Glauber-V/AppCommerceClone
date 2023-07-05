package com.example.appcommerceclone.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.appcommerceclone.R
import com.example.appcommerceclone.model.user.Address
import com.example.appcommerceclone.model.user.Name
import com.example.appcommerceclone.model.user.User
import com.example.appcommerceclone.ui.common.DoubleActionButton
import com.example.appcommerceclone.ui.common.OutlinedTextFieldWithValidation
import com.example.appcommerceclone.ui.common.PrimaryActionButton
import com.example.appcommerceclone.ui.common.SecondaryActionButton
import com.example.appcommerceclone.ui.common.UserEmailOutlinedTextField
import com.example.appcommerceclone.ui.common.UserNameOutlinedTextField
import com.example.appcommerceclone.ui.common.UserPasswordOutlinedTextField
import com.example.appcommerceclone.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@Stable
class UserProfileState(private val user: User) {

    val scrollState: ScrollState
        @Composable get() = rememberScrollState()

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
fun rememberUserProfileState(user: User): UserProfileState {
    return remember(user) { UserProfileState(user) }
}

@AndroidEntryPoint
class UserProfileFragment(private val userViewModel: UserViewModel) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val user by userViewModel.loggedUser.observeAsState(initial = null)
                if (user != null) {
                    UserProfileScreen(
                        user = user!!,
                        onUpdateUserProfile = {
                            userViewModel.updateUser(it)
                        },
                        onPictureRequest = {},
                        onLogout = {
                            val navController = findNavController()
                            val startDestination = navController.graph.startDestinationId
                            val navOptions = NavOptions.Builder().setPopUpTo(startDestination, true).build()
                            navController.navigate(startDestination, null, navOptions)
                            userViewModel.logout()
                        }
                    )
                } else {
                    findNavController().navigate(
                        UserProfileFragmentDirections.actionGlobalUserLoginFragment()
                    )
                }
            }
        }
    }
}

@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    user: User,
    userProfileState: UserProfileState = rememberUserProfileState(user),
    onPictureRequest: () -> Unit,
    onUpdateUserProfile: (updatedUser: User) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(userProfileState.scrollState)
            .padding(dimensionResource(id = R.dimen.padding_large)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
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
        if (userProfileState.isEditMode) {
            PrimaryActionButton(
                onPrimaryAction = onPictureRequest,
                primaryActionContent = {
                    Text(
                        modifier = Modifier.width(180.dp),
                        text = "Take a picture",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.button
                    )
                }
            )
        }
        UserEmailOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            readOnly = userProfileState.isEditMode,
            userEmailText = userProfileState.emailText,
            onEmailTextChange = { userProfileState.onEmailTextChange(it) }
        )
        UserNameOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            readOnly = userProfileState.isEditMode,
            userNameText = userProfileState.usernameText,
            onUserNameTextChange = { userProfileState.onUsernameTextChange(it) }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextFieldWithValidation(
                modifier = Modifier.fillMaxWidth(0.5f),
                readOnly = userProfileState.isEditMode,
                label = {
                    Text(
                        text = stringResource(id = R.string.user_hint_name_first_name),
                        style = MaterialTheme.typography.subtitle1
                    )
                },
                value = userProfileState.firstNameText,
                onValueChange = { userProfileState.onFirstNameTextChange(it) }
            )
            OutlinedTextFieldWithValidation(
                modifier = Modifier.fillMaxWidth(),
                readOnly = userProfileState.isEditMode,
                label = {
                    Text(
                        text = stringResource(id = R.string.user_hint_name_last_name),
                        style = MaterialTheme.typography.subtitle1
                    )
                },
                value = userProfileState.lastNameText,
                onValueChange = { userProfileState.onLastNameTextChange(it) }
            )
        }
        UserPasswordOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            readOnly = userProfileState.isEditMode,
            userPasswordText = userProfileState.passwordText,
            onUserPasswordChange = { userProfileState.onPasswordTextChange(it) }
        )
        OutlinedTextFieldWithValidation(
            modifier = Modifier.fillMaxWidth(),
            readOnly = userProfileState.isEditMode,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = null,
                    tint = colorResource(id = R.color.icon_color_black)
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.user_hint_phone),
                    style = MaterialTheme.typography.subtitle1
                )
            },
            value = userProfileState.phoneText,
            onValueChange = { userProfileState.onPhoneTextChange(it) }
        )
        OutlinedTextFieldWithValidation(
            modifier = Modifier.fillMaxWidth(),
            readOnly = userProfileState.isEditMode,
            label = {
                Text(
                    text = stringResource(id = R.string.user_hint_address_zipcode),
                    style = MaterialTheme.typography.subtitle1
                )
            },
            value = userProfileState.zipcodeText,
            onValueChange = { userProfileState.onZipcodeTextChange(it) }
        )
        OutlinedTextFieldWithValidation(
            modifier = Modifier.fillMaxWidth(),
            readOnly = userProfileState.isEditMode,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.LocationCity,
                    contentDescription = null,
                    tint = colorResource(id = R.color.icon_color_black)
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.user_hint_address_city),
                    style = MaterialTheme.typography.subtitle1
                )
            },
            value = userProfileState.cityText,
            onValueChange = { userProfileState.onCityTextChange(it) }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextFieldWithValidation(
                modifier = Modifier.fillMaxWidth(0.6f),
                readOnly = userProfileState.isEditMode,
                label = {
                    Text(
                        text = stringResource(id = R.string.user_hint_address_street),
                        style = MaterialTheme.typography.subtitle1
                    )
                },
                value = userProfileState.streetText,
                onValueChange = { userProfileState.onStreetTextChange(it) }
            )
            OutlinedTextFieldWithValidation(
                modifier = Modifier.fillMaxWidth(),
                readOnly = userProfileState.isEditMode,
                label = {
                    Text(
                        text = stringResource(id = R.string.user_hint_address_number),
                        style = MaterialTheme.typography.subtitle1
                    )
                },
                value = userProfileState.streetNumber.toString(),
                onValueChange = { userProfileState.onNumberTextChange(it) }
            )
        }
        if (!userProfileState.isEditMode) {
            PrimaryActionButton(
                modifier = Modifier.fillMaxWidth(),
                onPrimaryAction = { userProfileState.onEditModeChange() },
                primaryActionContent = {
                    Text(
                        text = stringResource(id = R.string.user_profile_start_update_btn),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.button
                    )
                }
            )
        } else {
            DoubleActionButton(
                modifier = Modifier.fillMaxWidth(),
                onPrimaryAction = {
                    onUpdateUserProfile(userProfileState.createUpdatedUser())
                    userProfileState.onEditModeChange()
                },
                isPrimaryActionEnabled = userProfileState.validateUpdate(),
                primaryActionContent = {
                    Text(
                        text = stringResource(id = R.string.user_profile_save_update_btn),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.button
                    )
                },
                onSecondaryAction = {
                    userProfileState.restoreUserState()
                    userProfileState.onEditModeChange()
                },
                secondaryActionContent = {
                    Text(
                        text = stringResource(id = R.string.user_profile_cancel_update_btn),
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
                    text = stringResource(id = R.string.user_profile_logout_btn),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.button
                )
            }
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewUserProfileScreenWithUser() {
    MaterialTheme {
        UserProfileScreen(
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