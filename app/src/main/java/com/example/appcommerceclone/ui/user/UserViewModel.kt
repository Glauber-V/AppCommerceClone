package com.example.appcommerceclone.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcommerceclone.LoadingState
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.user.UserAuthenticator
import com.example.appcommerceclone.data.user.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userAuthenticator: UserAuthenticator,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _loadingState = MutableLiveData(LoadingState.NOT_STARTED)
    val loadingState: LiveData<LoadingState> = _loadingState

    private val _loggedUser = MutableLiveData<User?>(null)
    val loggedUser: LiveData<User?> = _loggedUser


    fun login(username: String, password: String) {
        _loadingState.value = LoadingState.LOADING
        viewModelScope.launch(dispatcherProvider.main) {
            _loggedUser.value = try {
                userAuthenticator.getUserByUsernameAndPassword(username, password)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                null
            }
            _loadingState.value = if (_loggedUser.value != null) LoadingState.SUCCESS else LoadingState.FAILURE
        }
    }

    fun resetLoadingState() {
        _loadingState.value = LoadingState.NOT_STARTED
    }

    fun logout() {
        _loggedUser.value = null
    }
}