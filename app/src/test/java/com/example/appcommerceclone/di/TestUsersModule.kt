package com.example.appcommerceclone.di

import android.content.Context
import com.example.appcommerceclone.data.connection.ConnectivityObserver
import com.example.appcommerceclone.data.connection.FakeConnectivityObserver
import com.example.appcommerceclone.data.product.FakeProductsRepository
import com.example.appcommerceclone.data.product.ProductsRepository
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
    fun provideUserAuthenticator(): UserAuthenticator {
        return FakeUserAuthenticator()
    }

    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return FakeUserPreferences(context)
    }

    @Singleton
    @Provides
    fun provideUserOrders(): UserOrders {
        return FakeUserOrders()
    }
}