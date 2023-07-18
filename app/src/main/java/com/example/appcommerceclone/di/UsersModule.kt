package com.example.appcommerceclone.di

import com.example.appcommerceclone.data.FAKE_STORE_API_URL
import com.example.appcommerceclone.data.user.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
            .baseUrl(FAKE_STORE_API_URL)
            .build()

        return retrofit.create(UsersProvider::class.java)
    }

    @Singleton
    @Provides
    fun provideUserAuthenticator(usersProvider: UsersProvider): UserAuthenticator =
        DefaultUserAuthenticator(usersProvider)
}