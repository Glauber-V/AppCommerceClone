package com.example.appcommerceclone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcommerceclone.data.user.UserAuthenticator
import com.example.appcommerceclone.data.user.UserPreferences
import com.example.appcommerceclone.data.user.UserPreferencesKeys.USER_PREF_ID_KEY
import com.example.appcommerceclone.model.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userAuthenticator: UserAuthenticator,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _loggedUser = MutableLiveData<User?>(null)
    val loggedUser: LiveData<User?> = _loggedUser


    fun loadSavedUser() {
        viewModelScope.launch {
            val userId = userPreferences.getIntValueFromKey(USER_PREF_ID_KEY)
            val user: User? = userAuthenticator.getUserById(userId)
            _loggedUser.value = user
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = userAuthenticator.getUserByUsernameAndPassword(username, password)?.also { saveUserId(it.id) }
            _loggedUser.value = user
        }
    }

    fun updateUser(updatedUser: User) {
        viewModelScope.launch {

        }
    }

    fun logout() {
        _loggedUser.value = null
        saveUserId(0)
    }

    private fun saveUserId(userId: Int) {
        viewModelScope.launch {
            userPreferences.saveIntValueToKey(USER_PREF_ID_KEY, userId)
        }
    }
}