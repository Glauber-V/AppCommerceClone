package com.example.appcommerceclone.di

import android.content.Context
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.user.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UsersModule {

    @Singleton
    @Provides
    fun provideUsersApi(): UsersProvider {
        val baseUrl = "https://fakestoreapi.com/"

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .build()

        return retrofit.create(UsersProvider::class.java)
    }

    @Singleton
    @Provides
    fun provideUserAuthenticator(
        usersProvider: UsersProvider,
        dispatcher: DispatcherProvider
    ): UserAuthenticator = DefaultUserAuthenticator(usersProvider, dispatcher)

    @Singleton
    @Provides
    fun provideUserPreferences(
        @ApplicationContext context: Context,
        dispatcher: DispatcherProvider
    ): UserPreferences = DefaultUserPreferences(context, dispatcher)

    @Singleton
    @Provides
    fun provideUserOrders(
        usersProvider: UsersProvider,
        dispatcher: DispatcherProvider
    ): UserOrders = DefaultUserOrders(usersProvider, dispatcher)
}