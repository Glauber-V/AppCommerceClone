package com.example.appcommerceclone.di

import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.product.DefaultProductsRepository
import com.example.appcommerceclone.data.product.ProductsProvider
import com.example.appcommerceclone.data.product.ProductsRepository
import com.example.appcommerceclone.util.Constants.FAKE_STORE_API_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductsModule {

    @Singleton
    @Provides
    fun provideProductsApi(): ProductsProvider {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(FAKE_STORE_API_URL)
            .build()

        return retrofit.create(ProductsProvider::class.java)
    }

    @Singleton
    @Provides
    fun provideProductsRepository(
        productsProvider: ProductsProvider,
        dispatcherProvider: DispatcherProvider
    ): ProductsRepository = DefaultProductsRepository(productsProvider, dispatcherProvider)
}