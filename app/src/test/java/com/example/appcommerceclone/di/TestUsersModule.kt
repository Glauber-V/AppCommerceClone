package com.example.appcommerceclone.di

import android.content.Context
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.user.*
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
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


    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideUserPreferences(
        @ApplicationContext context: Context,
        dispatcherProvider: DispatcherProvider
    ): UserPreferences = FakeUserPreferences(context, dispatcherProvider)


    @Singleton
    @Provides
    fun provideUserOrders(
        usersProvider: UsersProvider,
        dispatcherProvider: DispatcherProvider
    ): UserOrders = FakeUserOrders(usersProvider, dispatcherProvider)

}