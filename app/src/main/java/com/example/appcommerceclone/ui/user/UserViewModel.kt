package com.example.appcommerceclone.ui.user

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.user.UserAuthenticator
import com.example.appcommerceclone.data.user.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userAuthenticator: UserAuthenticator,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isDataLoaded = MutableLiveData(false)
    val isDataLoaded: LiveData<Boolean> = _isDataLoaded

    private val _loggedUser = MutableLiveData<User?>(null)
    val loggedUser: LiveData<User?> = _loggedUser

    private val _userProfilePic = MutableLiveData<Uri?>()
    val userProfilePic: LiveData<Uri?> = _userProfilePic


    fun login(username: String, password: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            _isDataLoaded.value = false
            _isLoading.value = true
            _loggedUser.value = userAuthenticator.getUserByUsernameAndPassword(username, password)
            _isDataLoaded.value = true
            _isLoading.value = false
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