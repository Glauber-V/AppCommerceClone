package com.example.appcommerceclone.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.getOrAwaitValue
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_ELECTRONICS
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_JEWELRY
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
    fun `add product to favorite list and return true if product was added`() {
        val product = Product(id = 1, name = "A1", price = 5.0, description = "AAA", category = CATEGORY_NAME_JEWELRY, imageUrl = "")
        favoritesViewModel.addToFavorites(product)

        val favorites = favoritesViewModel.favorites.getOrAwaitValue()

        assertThat(favorites).contains(product)
    }

    @Test
    fun `remove product from favorite list and return true if product was removed`() {
        val product = Product(id = 1, name = "A1", price = 5.0, description = "AAA", category = CATEGORY_NAME_JEWELRY, imageUrl = "")
        favoritesViewModel.addToFavorites(product)
        favoritesViewModel.removeFromFavorites(product)

        val result = favoritesViewModel.favorites.getOrAwaitValue()

        assertThat(result).doesNotContain(product)
    }

    @Test
    fun `remove all products from the list`() {
        val product1 = Product(id = 1, name = "A1", price = 10.0, description = "AAA", category = CATEGORY_NAME_JEWELRY, imageUrl = "")
        val product2 = Product(id = 2, name = "B2", price = 20.0, description = "BBB", category = CATEGORY_NAME_ELECTRONICS, imageUrl = "")
        favoritesViewModel.addToFavorites(product1)
        favoritesViewModel.addToFavorites(product2)

        favoritesViewModel.removeAllFromFavorites()

        val result = favoritesViewModel.favorites.getOrAwaitValue()

        assertThat(result).isEmpty()
        assertThat(result).doesNotContain(product1)
        assertThat(result).doesNotContain(product2)
    }

}