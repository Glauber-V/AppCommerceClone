package com.example.appcommerceclone.data.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {

    val unconfined: CoroutineDispatcher
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}