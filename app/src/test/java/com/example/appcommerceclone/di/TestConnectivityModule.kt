package com.example.appcommerceclone.di

import com.example.appcommerceclone.data.connection.ConnectivityObserver
import com.example.appcommerceclone.data.connection.FakeConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [ConnectivityModule::class])
object TestConnectivityModule {

    @Singleton
    @Provides
    fun provideConnectivityObserver(): ConnectivityObserver =
        FakeConnectivityObserver()
}