package com.example.appcommerceclone.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.ui.favorites.FavoritesViewModel
import com.example.appcommerceclone.util.getOrAwaitValue
import com.example.appcommerceclone.util.productElectronic
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
    fun addProductToFavoriteList_verifyFavoriteListContainsAddedProduct() {
        favoritesViewModel.addToFavorites(productJewelry)

        val favorites = favoritesViewModel.favorites.getOrAwaitValue()
        assertThat(favorites).contains(productJewelry)
    }

    @Test
    fun removeProductFromFavoriteList_verifyFavoriteListDoesNotContainRemovedProduct() {
        favoritesViewModel.addToFavorites(productJewelry)

        var favorites = favoritesViewModel.favorites.getOrAwaitValue()
        assertThat(favorites).contains(productJewelry)

        favoritesViewModel.removeFromFavorites(productJewelry)

        favorites = favoritesViewModel.favorites.getOrAwaitValue()
        assertThat(favorites).doesNotContain(productJewelry)
    }

    @Test
    fun removeAllProductsFromTheList_verifyFavoriteListIsEmpty() {
        favoritesViewModel.addToFavorites(productJewelry)
        favoritesViewModel.addToFavorites(productElectronic)

        var favorites = favoritesViewModel.favorites.getOrAwaitValue()
        assertThat(favorites).isNotEmpty()
        assertThat(favorites).hasSize(2)
        assertThat(favorites).contains(productJewelry)
        assertThat(favorites).contains(productElectronic)

        favoritesViewModel.removeFromFavorites(productJewelry)
        favoritesViewModel.removeFromFavorites(productElectronic)

        favorites = favoritesViewModel.favorites.getOrAwaitValue()
        assertThat(favorites).isEmpty()
    }
}