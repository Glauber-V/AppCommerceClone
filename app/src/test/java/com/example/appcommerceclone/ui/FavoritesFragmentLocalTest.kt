package com.example.appcommerceclone.ui

import androidx.fragment.app.FragmentFactory
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productJewelery
import com.example.appcommerceclone.data.user.FakeUserProvider.Companion.firstUser
import com.example.appcommerceclone.data.user.UserAuthenticator
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.UsersModule
import com.example.appcommerceclone.ui.favorites.FavoritesAdapter.*
import com.example.appcommerceclone.ui.favorites.FavoritesFragment
import com.example.appcommerceclone.util.*
import com.example.appcommerceclone.viewmodels.FavoritesViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@UninstallModules(UsersModule::class, DispatcherModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class FavoritesFragmentLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testNavHostControllerRule = TestNavHostControllerRule(R.id.favorites_fragment)

    @get:Rule(order = 2)
    val testMainDispatcherRule = TestMainDispatcherRule()

    @Inject lateinit var userAuthenticator: UserAuthenticator
    @Inject lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var navHostController: TestNavHostController
    private lateinit var userViewModel: UserViewModel
    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var factory: FragmentFactory

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
        userViewModel = UserViewModel(userAuthenticator, dispatcherProvider)
        favoritesViewModel = FavoritesViewModel()
        factory = TestFragmentFactory(userViewModelTest = userViewModel, favoritesViewModelTest = favoritesViewModel)
    }

    @Test
    fun launchFavoritesFragment_noUser_navigateToUserLoginFragment() = runTest {

        val user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNull()

        launchFragmentInHiltContainer<FavoritesFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.favorites_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)
        }
    }

    @Test
    fun launchFavoritesFragment_withUser_stayInFavoritesFragment() = runTest {

        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        val user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNotNull()
        assertThat(user).isEqualTo(firstUser)

        launchFragmentInHiltContainer<FavoritesFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.user_login_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.favorites_fragment)
        }
    }

    @Test
    fun launchFavoritesFragment_witUser_verifyListIsNotEmpty() = runTest {

        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        val user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNotNull()
        assertThat(user).isEqualTo(firstUser)

        favoritesViewModel.addToFavorites(productJewelery)

        var favorites = favoritesViewModel.favorites.getOrAwaitValue()
        assertThat(favorites).isNotEmpty()
        assertThat(favorites).hasSize(1)
        assertThat(favorites).contains(productJewelery)

        launchFragmentInHiltContainer<FavoritesFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.user_login_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.favorites_fragment)

            favorites = favoritesViewModel.favorites.getOrAwaitValue()
            assertThat(favorites).isNotEmpty()

            val favoriteProduct = favorites.first()
            onView(withId(R.id.product_favorite_name)).check(matches(withText(favoriteProduct.name)))
        }
    }

    @Test
    fun launchFavoritesFragment_withUser_removeFavoriteProduct() = runTest {

        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        val user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNotNull()
        assertThat(user).isEqualTo(firstUser)

        favoritesViewModel.addToFavorites(productJewelery)

        var favorites = favoritesViewModel.favorites.getOrAwaitValue()
        assertThat(favorites).isNotEmpty()
        assertThat(favorites).contains(productJewelery)

        launchFragmentInHiltContainer<FavoritesFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.user_login_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.favorites_fragment)

            onView(withId(R.id.product_favorite_remove_btn)).perform(click())

            favorites = favoritesViewModel.favorites.getOrAwaitValue()
            assertThat(favorites).isEmpty()
        }
    }
}