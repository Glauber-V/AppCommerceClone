package com.example.appcommerceclone.viewmodels

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    ConnectivityViewModelTest::class,
    UserViewModelTest::class,
    ProductViewModelTest::class,
    FavoritesViewModelTest::class,
    CartViewModelTest::class,
    UserOrdersViewModelTest::class)
class AppViewModelTestSuite