package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.product.FakeProductsRepository.Companion.productJewelery
import com.example.appcommerceclone.data.user.FakeUserAuthenticator.Companion.firstUser
import com.example.appcommerceclone.ui.favorites.FavoritesFragment
import com.example.appcommerceclone.util.*
import com.example.appcommerceclone.viewmodels.FavoritesViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class FavoritesFragmentLocalTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testNavHostControllerRule = TestNavHostControllerRule(R.id.favorites_fragment)

    @get:Rule(order = 2)
    var testFragmentFactoryRule = TestFragmentFactoryRule()

    private lateinit var navHostController: TestNavHostController
    private lateinit var userViewModel: UserViewModel
    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var factory: TestFragmentFactory

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
        userViewModel = testFragmentFactoryRule.userViewModel!!
        favoritesViewModel = testFragmentFactoryRule.favoritesViewModel!!
        factory = testFragmentFactoryRule.factory!!
    }

    @Test
    fun launchFavoritesFragment_noUser_shouldNavigateToUserLoginFragment() = runTest {

        launch { userViewModel.login("", "") }
        advanceUntilIdle()

        val loggedUser = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(loggedUser).isNull()

        launchFragmentInHiltContainer<FavoritesFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)
        }
    }

    @Test
    fun launchFavoritesFragment_withUser_recyclerViewShouldBeVisible() = runTest {

        launch { userViewModel.login(username = firstUser.username, password = firstUser.password) }
        advanceUntilIdle()

        val loggedUser = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(loggedUser).isNotNull()
        assertThat(loggedUser).isEqualTo(firstUser)

        launchFragmentInHiltContainer<FavoritesFragment>(navHostController = navHostController, fragmentFactory = factory) {

            onView(withId(R.id.favorites_recycler_view)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun launchFavoritesFragment_witUser_verifyListIsNotEmpty() = runTest {

        launch { userViewModel.login(username = firstUser.username, password = firstUser.password) }
        advanceUntilIdle()

        val loggedUser = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(loggedUser).isNotNull()
        assertThat(loggedUser).isEqualTo(firstUser)

        launchFragmentInHiltContainer<FavoritesFragment>(navHostController = navHostController, fragmentFactory = factory) {

            favoritesViewModel.addToFavorites(productJewelery)

            val favorites = favoritesViewModel.favorites.getOrAwaitValue()
            assertThat(favorites).isNotEmpty()
            assertThat(favorites).contains(productJewelery)
        }
    }

    @Test
    fun launchFavoritesFragment_withUser_removeFavoriteProduct() = runTest {

        launch { userViewModel.login(username = firstUser.username, password = firstUser.password) }
        advanceUntilIdle()

        val loggedUser = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(loggedUser).isNotNull()
        assertThat(loggedUser).isEqualTo(firstUser)

        launchFragmentInHiltContainer<FavoritesFragment>(navHostController = navHostController, fragmentFactory = factory) {

            favoritesViewModel.addToFavorites(productJewelery)

            var favorites = favoritesViewModel.favorites.getOrAwaitValue()
            assertThat(favorites).isNotEmpty()
            assertThat(favorites).contains(productJewelery)

            onView(withId(R.id.product_favorite_remove_btn)).perform(click())

            favorites = favoritesViewModel.favorites.getOrAwaitValue()
            assertThat(favorites).isEmpty()
        }
    }
}