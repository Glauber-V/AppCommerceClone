package com.example.appcommerceclone.ui

import androidx.activity.ComponentActivity
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productElectronic
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productJewelery
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productMensClothing
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productWomensClothing
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.ui.favorites.FavoritesAdapter.*
import com.example.appcommerceclone.ui.favorites.FavoritesScreen
import com.example.appcommerceclone.util.*
import com.example.appcommerceclone.viewmodels.FavoritesViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class FavoritesFragmentLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeAndroidRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var favoriteProducts: State<List<Product>>

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        showSemanticTreeInConsole()
        composeAndroidRule.setContent {
            favoritesViewModel = FavoritesViewModel().apply {
                addToFavorites(productJewelery)
                addToFavorites(productElectronic)
                addToFavorites(productMensClothing)
                addToFavorites(productWomensClothing)
            }
            favoriteProducts = favoritesViewModel.favorites.observeAsState(initial = emptyList())
            FavoritesScreen(
                favoriteProducts = favoriteProducts.value,
                onRemoveFavoriteProduct = { product: Product ->
                    favoritesViewModel.removeFromFavorites(product)
                }
            )
        }
    }

    @Test
    fun onFavoriteScreen_removeFavoriteProduct_verifyProductWasRemoved() {

        assertThat(favoriteProducts.value).hasSize(4)

        with(composeAndroidRule) {
            onRoot().printToLog("onFavoriteScreen")

            onAllNodesWithContentDescription(getStringResource(R.string.content_desc_remove_from_favorites_btn))
                .onFirst()
                .performClick()

            assertThat(favoriteProducts.value).hasSize(3)
        }
    }
}