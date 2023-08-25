package com.example.appcommerceclone.ui


import androidx.fragment.app.FragmentFactory
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.user.UserAuthenticator
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.UsersModule
import com.example.appcommerceclone.ui.favorites.FavoritesFragment
import com.example.appcommerceclone.ui.favorites.FavoritesViewModel
import com.example.appcommerceclone.ui.user.UserViewModel
import com.example.appcommerceclone.util.TestNavHostControllerRule
import com.example.appcommerceclone.util.atPosition
import com.example.appcommerceclone.util.formatPrice
import com.example.appcommerceclone.util.getOrAwaitValue
import com.example.appcommerceclone.util.launchFragmentInHiltContainer
import com.example.appcommerceclone.util.productJewelry
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@UninstallModules(UsersModule::class, DispatcherModule::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class FavoritesFragmentLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testNavHostControllerRule = TestNavHostControllerRule(R.id.favorites_fragment)

    @Inject
    lateinit var userAuthenticator: UserAuthenticator

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

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
        factory = TestFragmentFactory(favoritesViewModelTest = favoritesViewModel)
    }

    @Test
    fun launchFavoritesFragment_addProductJewelery_verifyProductIsVisible() {
        favoritesViewModel.addToFavorites(productJewelry)

        val favorites = favoritesViewModel.favorites.getOrAwaitValue()
        assertThat(favorites).isNotEmpty()
        assertThat(favorites).hasSize(1)
        assertThat(favorites).contains(productJewelry)

        launchFragmentInHiltContainer<FavoritesFragment>(navHostController = navHostController, fragmentFactory = factory) {
            val favoriteProduct = favorites.first()
            onView(withId(R.id.favorites_recycler_view))
                .check(matches(atPosition(0, hasDescendant(withText(favoriteProduct.name)))))
                .check(matches(atPosition(0, hasDescendant(withText(favoriteProduct.formatPrice())))))
        }
    }

    @Test
    fun launchFavoritesFragment_removeFavoriteProduct_verifyListIsEmpty() {
        favoritesViewModel.addToFavorites(productJewelry)

        var favorites = favoritesViewModel.favorites.getOrAwaitValue()
        assertThat(favorites).isNotEmpty()
        assertThat(favorites).contains(productJewelry)

        launchFragmentInHiltContainer<FavoritesFragment>(navHostController = navHostController, fragmentFactory = factory) {
            onView(withId(R.id.item_product_favorite_remove_btn)).perform(click())

            favorites = favoritesViewModel.favorites.getOrAwaitValue()
            assertThat(favorites).isEmpty()
        }
    }
}