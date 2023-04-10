package com.example.appcommerceclone.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.user.UserAuthenticator
import com.example.appcommerceclone.model.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userAuthenticator: UserAuthenticator,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _loggedUser = MutableLiveData<User?>(null)
    val loggedUser: LiveData<User?> = _loggedUser

    private val _userProfilePic = MutableLiveData<Uri?>()
    val userProfilePic: LiveData<Uri?> = _userProfilePic


    fun login(username: String, password: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            _loggedUser.value = userAuthenticator.getUserByUsernameAndPassword(username, password)
        }
    }

    fun updateUserProfilePicture(uri: Uri?) {
        _userProfilePic.value = uri
    }

    fun updateUser(updatedUser: User) {
        viewModelScope.launch(dispatcherProvider.main) {
            Log.d("User", updatedUser.toString())
        }
    }

    fun logout() {
        _loggedUser.value = null
    }
}