package com.example.appcommerceclone.ui

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    ProductsFragmentLocalTest::class,
    CategoriesFragmentLocalTest::class,
    FavoritesFragmentLocalTest::class,
    ProductFullDetailFragmentLocalTest::class,
    ProductLessDetailFragmentLocalTest::class,
    OrdersFragmentLocalTest::class)
class AppProductsLocalTestSuite