package com.example.appcommerceclone.util

import androidx.annotation.IdRes
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.user.model.User
import com.example.appcommerceclone.ui.cart.CartViewModel
import com.example.appcommerceclone.ui.favorites.FavoritesViewModel
import com.example.appcommerceclone.ui.order.UserOrdersViewModel
import com.example.appcommerceclone.ui.product.ProductCategories
import com.example.appcommerceclone.ui.product.ProductViewModel
import com.example.appcommerceclone.ui.user.UserViewModel
import com.google.common.truth.Truth.assertThat

fun UserViewModel.assertThatLoadingStateIsEqualTo(expectedLoadingState: LoadingState) {
    val loadingState = this.loadingState.getOrAwaitValue()
    assertThat(loadingState).isEqualTo(expectedLoadingState)
}

fun UserViewModel.assertThatThereIsNoCurrentUser() {
    assertThatLoadingStateIsEqualTo(LoadingState.NOT_STARTED)
    val userUnderTest = this.currentUser.getOrAwaitValue()
    assertThat(userUnderTest).isNull()
}

fun UserViewModel.assertThatUserWasLoadedCorrectly(user: User) {
    assertThatLoadingStateIsEqualTo(LoadingState.SUCCESS)
    val userUnderTest = this.currentUser.getOrAwaitValue()
    assertThat(userUnderTest).isNotNull()
    assertThat(userUnderTest?.username).isEqualTo(user.username)
    assertThat(userUnderTest?.password).isEqualTo(user.password)
}

fun ProductViewModel.assertThatLoadingStateIsEqualTo(expectedLoadingState: LoadingState) {
    val loadingState = this.loadingState.getOrAwaitValue()
    assertThat(loadingState).isEqualTo(expectedLoadingState)
}

fun ProductViewModel.assertThatProductListHasCorrectSizeAndElements(filter: ProductCategories = ProductCategories.NONE) {
    val products = this.products.getOrAwaitValue()
    assertThat(products).isNotEmpty()
    when (filter) {
        ProductCategories.NONE -> {
            assertThat(products.size).isEqualTo(4)
            assertThat(products).contains(productJewelry)
            assertThat(products).contains(productElectronic)
            assertThat(products).contains(productMensClothing)
            assertThat(products).contains(productWomensClothing)
        }

        ProductCategories.JEWELERY -> {
            assertThat(products.size).isEqualTo(1)
            assertThat(products).contains(productJewelry)
            assertThat(products).doesNotContain(productElectronic)
            assertThat(products).doesNotContain(productMensClothing)
            assertThat(products).doesNotContain(productWomensClothing)

            assertThat(products.first().category).isEqualTo(ProductCategories.JEWELERY.categoryName)
        }

        ProductCategories.ELECTRONICS -> {
            assertThat(products.size).isEqualTo(1)
            assertThat(products).doesNotContain(productJewelry)
            assertThat(products).contains(productElectronic)
            assertThat(products).doesNotContain(productMensClothing)
            assertThat(products).doesNotContain(productWomensClothing)

            assertThat(products.first().category).isEqualTo(ProductCategories.ELECTRONICS.categoryName)
        }

        ProductCategories.MENS_CLOTHING -> {
            assertThat(products.size).isEqualTo(1)
            assertThat(products).doesNotContain(productJewelry)
            assertThat(products).doesNotContain(productElectronic)
            assertThat(products).contains(productMensClothing)
            assertThat(products).doesNotContain(productWomensClothing)

            assertThat(products.first().category).isEqualTo(ProductCategories.MENS_CLOTHING.categoryName)
        }

        ProductCategories.WOMENS_CLOTHING -> {
            assertThat(products.size).isEqualTo(1)
            assertThat(products).doesNotContain(productJewelry)
            assertThat(products).doesNotContain(productElectronic)
            assertThat(products).doesNotContain(productMensClothing)
            assertThat(products).contains(productWomensClothing)

            assertThat(products.first().category).isEqualTo(ProductCategories.WOMENS_CLOTHING.categoryName)
        }
    }
}

fun ProductViewModel.assertThatShimmerVisibilityIsInSyncWithLoadingState() {
    val loadingState = this.loadingState.getOrAwaitValue()
    if (loadingState == LoadingState.LOADING) {
        onView(withId(R.id.products_shimmer))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.products_swipe_refresh_layout))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    } else {
        onView(withId(R.id.products_shimmer))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.products_swipe_refresh_layout))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
}

fun FavoritesViewModel.assertThatFavoritesPlaceholderIsInSyncWithListState() {
    val favorites = this.favorites.getOrAwaitValue()
    if (favorites.isEmpty()) {
        onView(withId(R.id.favorites_recycler_view))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.favorites_empty_list_placeholder))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    } else {
        onView(withId(R.id.favorites_recycler_view))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.favorites_empty_list_placeholder))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }
}

fun CartViewModel.assertThatCartPlaceholderIsInSyncWithListState() {
    val cartProducts = this.cartProducts.getOrAwaitValue()
    if (cartProducts.isEmpty()) {
        onView(withId(R.id.cart_recycler_view))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.cart_empty_list_placeholder))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    } else {
        onView(withId(R.id.cart_recycler_view))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.cart_empty_list_placeholder))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }
}

fun UserOrdersViewModel.assertThatOrdersPlaceholderIsInSyncWithListState() {
    val orders = this.orders.getOrAwaitValue()
    if (orders.isEmpty()) {
        onView(withId(R.id.orders_recycler_view))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.orders_empty_list_placeholder))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    } else {
        onView(withId(R.id.orders_recycler_view))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.orders_empty_list_placeholder))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }
}

fun TestNavHostController.assertThatCurrentDestinationIsEqualTo(@IdRes id: Int) {
    assertThat(currentDestination?.id).isEqualTo(id)
}

fun TestNavHostController.assertThatCurrentDestinationIsNotEqualTo(@IdRes id: Int) {
    assertThat(currentDestination?.id).isNotEqualTo(id)
}