package com.example.appcommerceclone.ui

import androidx.activity.ComponentActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.printToLog
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.model.product.Product
import com.example.appcommerceclone.ui.favorites.FavoritesScreen
import com.example.appcommerceclone.ui.favorites.FavoritesViewModel
import com.example.appcommerceclone.util.getStringResource
import com.example.appcommerceclone.util.productElectronic
import com.example.appcommerceclone.util.productJewelry
import com.example.appcommerceclone.util.productMensClothing
import com.example.appcommerceclone.util.productWomensClothing
import com.example.appcommerceclone.util.showSemanticTreeInConsole
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
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
                addToFavorites(productJewelry)
                addToFavorites(productElectronic)
                addToFavorites(productMensClothing)
                addToFavorites(productWomensClothing)
            }
            favoriteProducts = favoritesViewModel.favorites.observeAsState(initial = emptyList())
            MaterialTheme {
                FavoritesScreen(
                    favoriteProducts = favoriteProducts.value,
                    onRemoveFavoriteProduct = { product: Product ->
                        favoritesViewModel.removeFromFavorites(product)
                    }
                )
            }
        }
    }

    @Test
    fun onFavoriteScreen_removeFavoriteProduct_verifyProductWasRemoved() {

        assertThat(favoriteProducts.value).hasSize(4)

        with(composeAndroidRule) {
            onRoot().printToLog("onFavoriteScreen")

            onNodeWithText(productJewelry.name)
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .onParent()
                .onChildren()
                .filterToOne(hasContentDescription(getStringResource(R.string.content_desc_remove_from_favorites_btn)))
                .performClick()

            assertThat(favoriteProducts.value).hasSize(3)

            onRoot().printToLog("onFavoriteScreen|ProductJeweleryRemoved")
        }
    }
}