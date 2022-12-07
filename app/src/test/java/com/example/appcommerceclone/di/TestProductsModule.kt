package com.example.appcommerceclone.di

import com.example.appcommerceclone.data.product.FakeProductsRepository
import com.example.appcommerceclone.data.product.ProductsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [ProductsModule::class])
object TestProductsModule {

    @Singleton
    @Provides
    fun provideProductsRepository(): ProductsRepository {
        return FakeProductsRepository()
    }
}