package com.example.appcommerceclone.di

import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.user.*
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [UsersModule::class])
object TestUsersModule {

    @Singleton
    @Provides
    fun provideUserApi(): UsersProvider = FakeUserProvider()

    @Singleton
    @Provides
    fun provideUserAuthenticator(
        usersProvider: UsersProvider,
        dispatcherProvider: DispatcherProvider
    ): UserAuthenticator = FakeUserAuthenticator(usersProvider, dispatcherProvider)

    @Singleton
    @Provides
    fun provideUserOrders(
        usersProvider: UsersProvider,
        dispatcherProvider: DispatcherProvider
    ): UserOrders = FakeUserOrders(usersProvider, dispatcherProvider)
}