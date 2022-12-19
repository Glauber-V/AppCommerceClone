package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.TestNavHostControllerRule
import com.example.appcommerceclone.data.user.FakeUserAuthenticator
import com.example.appcommerceclone.di.ConnectivityModule
import com.example.appcommerceclone.di.ProductsModule
import com.example.appcommerceclone.di.UsersModule
import com.example.appcommerceclone.getOrAwaitValue
import com.example.appcommerceclone.launchFragmentInHiltContainer
import com.example.appcommerceclone.model.product.Product
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@UninstallModules(
    ConnectivityModule::class,
    ProductsModule::class,
    UsersModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class FavoritesFragmentLocalTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testNavHostControllerRule = TestNavHostControllerRule(R.id.favorites_fragment)

    private lateinit var navHostController: TestNavHostController

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
    }

    @Test
    fun launchFavoritesFragment_noUser_shouldNavigateToUserLoginFragment() {
        launchFragmentInHiltContainer<FavoritesFragment>(navHostController = navHostController)

        assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)
    }

    @Test
    fun launchFavoritesFragment_withUser_recyclerViewShouldBeVisible() = runTest {
        launchFragmentInHiltContainer<FavoritesFragment>(navHostController = navHostController) {
            userViewModel.login(username = "Orisa", password = "321")
            advanceUntilIdle()
        }

        onView(withId(R.id.favorites_recycler_view))
            .check(matches(isDisplayed()))
    }

    @Test
    fun launchFavoritesFragment_witUser_verifyListIsNotEmpty() = runTest {
        launchFragmentInHiltContainer<FavoritesFragment>(navHostController = navHostController) {
            userViewModel.login(username = FakeUserAuthenticator.USERNAME, password = FakeUserAuthenticator.PASSWORD)
            advanceUntilIdle()

            val product = Product(name = "Product1", price = 20.0)
            favoritesViewModel.addToFavorites(product)
            advanceUntilIdle()

            val favorites = favoritesViewModel.favorites.getOrAwaitValue()
            assertThat(favorites).isNotEmpty()
            assertThat(favorites).contains(product)
        }
    }

    @Test
    fun launchFavoritesFragment_withUser_removeFavoriteProduct() = runTest {
        launchFragmentInHiltContainer<FavoritesFragment>(navHostController = navHostController) {
            userViewModel.login(username = FakeUserAuthenticator.USERNAME, password = FakeUserAuthenticator.PASSWORD)
            advanceUntilIdle()

            val product = Product(name = "Product1", price = 20.0)
            favoritesViewModel.addToFavorites(product)
            advanceUntilIdle()

            var favorites = favoritesViewModel.favorites.getOrAwaitValue()
            assertThat(favorites).isNotEmpty()
            assertThat(favorites).contains(product)

            onView(withId(R.id.product_favorite_remove_btn)).perform(click())
            advanceUntilIdle()

            favorites = favoritesViewModel.favorites.getOrAwaitValue()
            assertThat(favorites).isEmpty()
        }
    }
}