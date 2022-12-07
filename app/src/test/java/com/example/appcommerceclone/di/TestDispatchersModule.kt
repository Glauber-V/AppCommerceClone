package com.example.appcommerceclone.di

import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.dispatcher.FakeDispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DispatcherModule::class])
object TestDispatchersModule {

    @Singleton
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider {
        return FakeDispatcherProvider()
    }
}