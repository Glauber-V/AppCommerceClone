package com.example.appcommerceclone.data.connection

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeConnectivityObserver : ConnectivityObserver {

    private var isConnected = true

    override fun observer(): Flow<Boolean> {
        return flow { emit(isConnected) }
    }

    fun connect() {
        isConnected = true
    }

    fun disconnect() {
        isConnected = false
    }
}