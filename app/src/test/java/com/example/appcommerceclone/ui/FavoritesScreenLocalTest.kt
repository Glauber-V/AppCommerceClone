package com.example.appcommerceclone.ui

import androidx.activity.ComponentActivity
import androidx.compose.material.MaterialTheme
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
import com.example.appcommerceclone.data.product.model.Product
import com.example.appcommerceclone.ui.favorites.FavoritesScreen
import com.example.appcommerceclone.ui.favorites.FavoritesViewModel
import com.example.appcommerceclone.util.getStringResource
import com.example.appcommerceclone.util.productJewelry
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
class FavoritesScreenLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeAndroidRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var favoritesViewModel: FavoritesViewModel

    private lateinit var favoriteProducts: List<Product>

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        showSemanticTreeInConsole()
        composeAndroidRule.setContent {
            MaterialTheme {
                favoritesViewModel = FavoritesViewModel()
                favoriteProducts = favoritesViewModel.favorites
                FavoritesScreen(
                    favoriteProducts = favoriteProducts,
                    onRemoveFavoriteProduct = { product: Product ->
                        favoritesViewModel.removeFromFavorites(product)
                    }
                )
            }
        }
    }

    @Test
    fun onFavoriteScreen_removeFavoriteProduct_verifyProductWasRemoved() {
        with(composeAndroidRule) {
            onRoot().printToLog("onFavoriteScreen|EmptyList")

            onNodeWithText(getStringResource(R.string.place_holder_text_no_favorites))
                .assertExists()
                .assertIsDisplayed()

            favoritesViewModel.addToFavorites(productJewelry)

            assertThat(favoriteProducts).hasSize(1)
            assertThat(favoriteProducts).contains(productJewelry)

            onRoot().printToLog("onFavoriteScreen|FavoriteListHasSize1")

            onNodeWithText(getStringResource(R.string.place_holder_text_no_favorites))
                .assertDoesNotExist()

            onNodeWithText(productJewelry.name)
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .onParent()
                .onChildren()
                .filterToOne(hasContentDescription(getStringResource(R.string.content_desc_remove_from_favorites_btn)))
                .performClick()

            onRoot().printToLog("onFavoriteScreen|ProductJeweleryRemoved")

            assertThat(favoriteProducts).isEmpty()

            onNodeWithText(productJewelry.name)
                .assertDoesNotExist()
        }
    }
}