package com.example.appcommerceclone.ui

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    CartFragmentLocalTest::class,
    CategoriesFragmentLocalTest::class,
    FavoritesFragmentLocalTest::class,
    OrdersFragmentLocalTest::class,
    ProductDetailFragmentLocalTest::class,
    ProductsFragmentLocalTest::class
)
class AppProductsLocalTestSuite