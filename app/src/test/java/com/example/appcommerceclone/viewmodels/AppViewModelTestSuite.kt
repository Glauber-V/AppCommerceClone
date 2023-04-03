package com.example.appcommerceclone.viewmodels

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    CartViewModelTest::class,
    FavoritesViewModelTest::class,
    ProductViewModelTest::class,
    UserOrdersViewModelTest::class,
    UserViewModelTest::class
)
class AppViewModelTestSuite