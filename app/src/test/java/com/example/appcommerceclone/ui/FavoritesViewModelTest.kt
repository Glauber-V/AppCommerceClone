package com.example.appcommerceclone.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.ui.favorites.FavoritesViewModel
import com.example.appcommerceclone.util.productJewelry
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoritesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var favoritesViewModel: FavoritesViewModel

    @Before
    fun setup() {
        favoritesViewModel = FavoritesViewModel()
    }

    @Test
    fun addProductToFavoriteList_verifyFavoriteListHasSize1AndContainsAddedProduct() {
        val favorites = favoritesViewModel.favorites
        assertThat(favorites).isEmpty()

        favoritesViewModel.addToFavorites(productJewelry)

        assertThat(favorites).isNotEmpty()
        assertThat(favorites).hasSize(1)
        assertThat(favorites).contains(productJewelry)
    }

    @Test
    fun removeProductFromFavoriteList_verifyListIsEmpty() {
        val favorites = favoritesViewModel.favorites
        assertThat(favorites).isEmpty()

        favoritesViewModel.addToFavorites(productJewelry)

        assertThat(favorites).isNotEmpty()
        assertThat(favorites).hasSize(1)
        assertThat(favorites).contains(productJewelry)

        favoritesViewModel.removeFromFavorites(productJewelry)

        assertThat(favorites).doesNotContain(productJewelry)
        assertThat(favorites).isEmpty()
    }
}