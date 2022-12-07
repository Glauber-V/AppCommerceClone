package com.example.appcommerceclone.viewmodels

import com.example.appcommerceclone.data.connection.FakeConnectivityObserver
import com.example.appcommerceclone.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ConnectivityViewModelTest {

    private lateinit var fakeConnectivityObserver: FakeConnectivityObserver

    @Before
    fun setUp() {
        fakeConnectivityObserver = FakeConnectivityObserver()
    }

    @Test
    fun changeConnectionStatusTo_connect_returnTrue() {
        fakeConnectivityObserver.connect()

        val connectivityViewModel = ConnectivityViewModel(fakeConnectivityObserver)
        val isConnected = connectivityViewModel.isConnected.getOrAwaitValue()

        assertThat(isConnected).isTrue()
    }

    @Test
    fun changeConnectionStatusTo_disconnect_returnFalse() {
        fakeConnectivityObserver.disconnect()

        val connectivityViewModel = ConnectivityViewModel(fakeConnectivityObserver)
        val isConnected = connectivityViewModel.isConnected.getOrAwaitValue()

        assertThat(isConnected).isFalse()
    }
}