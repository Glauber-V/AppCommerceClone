package com.example.appcommerceclone

import com.example.appcommerceclone.ui.*
import com.example.appcommerceclone.viewmodels.*
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
    UserOrdersViewModelTest::class,
    ProductsFragmentLocalTest::class,
    CategoriesFragmentLocalTest::class,
    FavoritesFragmentLocalTest::class,
    ProductFullDetailFragmentLocalTest::class,
    ProductLessDetailFragmentLocalTest::class,
    OrdersFragmentLocalTest::class,
    UserLoginFragmentLocalTest::class,
    UserProfileFragmentLocalTest::class,
    UserRegisterFragmentLocalTest::class)
class AllLocalTestsSuite