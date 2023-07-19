package com.example.appcommerceclone.data.connection

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeConnectivityObserver : ConnectivityObserver {

    override fun observer(): Flow<Boolean> {
        return flowOf(true)
    }
}