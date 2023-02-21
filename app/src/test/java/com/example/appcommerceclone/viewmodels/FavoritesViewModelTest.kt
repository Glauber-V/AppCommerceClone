package com.example.appcommerceclone.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.data.product.FakeProductsRepository.Companion.productElectronic
import com.example.appcommerceclone.data.product.FakeProductsRepository.Companion.productJewelery
import com.example.appcommerceclone.util.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class FavoritesViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

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
        favoritesViewModel.removeFromFavorites(productJewelery)

        val result = favoritesViewModel.favorites.getOrAwaitValue()

        assertThat(result).doesNotContain(productJewelery)
    }

    @Test
    fun removeAllProductsFromTheList_favoritesListShouldBeEmpty() {
        favoritesViewModel.addToFavorites(productJewelery)
        favoritesViewModel.addToFavorites(productElectronic)

        val firstCheck = favoritesViewModel.favorites.getOrAwaitValue()
        assertThat(firstCheck).isNotEmpty()
        assertThat(firstCheck).contains(productJewelery)
        assertThat(firstCheck).contains(productElectronic)

        favoritesViewModel.removeAllFromFavorites()

        val secondCheck = favoritesViewModel.favorites.getOrAwaitValue()
        assertThat(secondCheck).isEmpty()
        assertThat(secondCheck).doesNotContain(productJewelery)
        assertThat(secondCheck).doesNotContain(productElectronic)
    }
}