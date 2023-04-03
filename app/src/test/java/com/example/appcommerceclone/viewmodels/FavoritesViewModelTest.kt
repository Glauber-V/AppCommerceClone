package com.example.appcommerceclone.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productElectronic
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productJewelery
import com.example.appcommerceclone.util.getOrAwaitValue
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
    fun addProductToFavoriteList_returnTrueIfProductWasAdded() {
        favoritesViewModel.addToFavorites(productJewelery)

        val favorites = favoritesViewModel.favorites.getOrAwaitValue()
        assertThat(favorites).contains(productJewelery)
    }

    @Test
    fun removeProductFromFavoriteList_returnTrueIfProductWasRemoved() {
        favoritesViewModel.addToFavorites(productJewelery)

        var favorites = favoritesViewModel.favorites.getOrAwaitValue()
        assertThat(favorites).contains(productJewelery)

        favoritesViewModel.removeFromFavorites(productJewelery)

        favorites = favoritesViewModel.favorites.getOrAwaitValue()
        assertThat(favorites).doesNotContain(productJewelery)
    }

    @Test
    fun removeAllProductsFromTheList_favoritesListShouldBeEmpty() {
        favoritesViewModel.addToFavorites(productJewelery)
        favoritesViewModel.addToFavorites(productElectronic)

        var favorites = favoritesViewModel.favorites.getOrAwaitValue()
        assertThat(favorites).isNotEmpty()
        assertThat(favorites).contains(productJewelery)
        assertThat(favorites).contains(productElectronic)

        favoritesViewModel.removeFromFavorites(productJewelery)
        favoritesViewModel.removeFromFavorites(productElectronic)

        favorites = favoritesViewModel.favorites.getOrAwaitValue()
        assertThat(favorites).isEmpty()
    }
}