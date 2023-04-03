package com.example.appcommerceclone

import com.example.appcommerceclone.ui.*
import com.example.appcommerceclone.viewmodels.*
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
    UserViewModelTest::class,
    CartFragmentLocalTest::class,
    CategoriesFragmentLocalTest::class,
    FavoritesFragmentLocalTest::class,
    OrdersFragmentLocalTest::class,
    ProductDetailFragmentLocalTest::class,
    ProductsFragmentLocalTest::class,
    UserLoginFragmentLocalTest::class,
    UserProfileFragmentLocalTest::class,
    UserRegisterFragmentLocalTest::class
)
class AllLocalTestsSuite