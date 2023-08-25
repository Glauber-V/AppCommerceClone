package com.example.appcommerceclone.di

import com.example.appcommerceclone.data.dispatcher.DefaultDispatcherProvider
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Singleton
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider =
        DefaultDispatcherProvider()
}