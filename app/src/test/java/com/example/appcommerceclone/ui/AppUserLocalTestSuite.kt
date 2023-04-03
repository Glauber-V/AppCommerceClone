package com.example.appcommerceclone.ui

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    UserLoginFragmentLocalTest::class,
    UserProfileFragmentLocalTest::class,
    UserRegisterFragmentLocalTest::class
)
class AppUserLocalTestSuite