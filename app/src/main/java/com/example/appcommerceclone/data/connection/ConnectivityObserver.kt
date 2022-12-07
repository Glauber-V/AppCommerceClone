package com.example.appcommerceclone.data.connection

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observer(): Flow<Boolean>
}