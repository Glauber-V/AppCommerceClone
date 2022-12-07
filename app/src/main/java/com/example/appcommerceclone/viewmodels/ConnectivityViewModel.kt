package com.example.appcommerceclone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.appcommerceclone.data.connection.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConnectivityViewModel @Inject constructor(connectivityObserver: ConnectivityObserver) : ViewModel() {

    val isConnected: LiveData<Boolean> =
        connectivityObserver.observer().asLiveData(viewModelScope.coroutineContext)
}