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
import kotlinx.coroutines.launch
import retrofit2.HttpException
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
        viewModelScope.launch(dispatcherProvider.main) {
            _loadingState.value = LoadingState.LOADING
            try {
                _loggedUser.value = userAuthenticator.getUserByUsernameAndPassword(username, password)
                _loadingState.value = LoadingState.SUCCESS
            } catch (e: HttpException) {
                _loggedUser.value = null
                _loadingState.value = LoadingState.FAILURE
            }
        }
    }

    fun logout() {
        _loggedUser.value = null
    }
}