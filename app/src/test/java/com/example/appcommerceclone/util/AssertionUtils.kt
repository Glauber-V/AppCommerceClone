package com.example.appcommerceclone.util

import androidx.annotation.IdRes
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.user.model.User
import com.example.appcommerceclone.ui.product.ProductCategories
import com.example.appcommerceclone.ui.product.ProductViewModel
import com.example.appcommerceclone.ui.user.UserViewModel
import com.google.common.truth.Truth

fun UserViewModel.assertThatLoadingStateIsEqualTo(expectedLoadingState: LoadingState) {
    val value = this.loadingState.getOrAwaitValue()
    Truth.assertThat(value).isEqualTo(expectedLoadingState)
}

fun UserViewModel.assertThatThereIsNoCurrentUser() {
    assertThatLoadingStateIsEqualTo(LoadingState.NOT_STARTED)
    val currentUser = this.currentUser.getOrAwaitValue()
    Truth.assertThat(currentUser).isNull()
}

fun UserViewModel.assertThatUserWasLoadedCorrectly(user: User) {
    assertThatLoadingStateIsEqualTo(LoadingState.SUCCESS)
    val userUnderTest = currentUser.getOrAwaitValue()
    Truth.assertThat(userUnderTest).isNotNull()
    Truth.assertThat(userUnderTest?.username).isEqualTo(user.username)
    Truth.assertThat(userUnderTest?.password).isEqualTo(user.password)
}

fun ProductViewModel.assertThatLoadingStateIsEqualTo(expectedLoadingState: LoadingState) {
    val value = this.loadingState.getOrAwaitValue()
    Truth.assertThat(value).isEqualTo(expectedLoadingState)
}

fun ProductViewModel.assertThatProductListHasCorrectSizeAndElements(filter: ProductCategories = ProductCategories.NONE) {
    val products = this.products.getOrAwaitValue()
    Truth.assertThat(products).isNotEmpty()
    when (filter) {
        ProductCategories.NONE -> {
            Truth.assertThat(products.size).isEqualTo(4)
            Truth.assertThat(products).contains(productJewelry)
            Truth.assertThat(products).contains(productElectronic)
            Truth.assertThat(products).contains(productMensClothing)
            Truth.assertThat(products).contains(productWomensClothing)
        }

        ProductCategories.JEWELERY -> {
            Truth.assertThat(products.size).isEqualTo(1)
            Truth.assertThat(products).contains(productJewelry)
            Truth.assertThat(products).doesNotContain(productElectronic)
            Truth.assertThat(products).doesNotContain(productMensClothing)
            Truth.assertThat(products).doesNotContain(productWomensClothing)

            Truth.assertThat(products.first().category).isEqualTo(ProductCategories.JEWELERY.categoryName)
        }

        ProductCategories.ELECTRONICS -> {
            Truth.assertThat(products.size).isEqualTo(1)
            Truth.assertThat(products).doesNotContain(productJewelry)
            Truth.assertThat(products).contains(productElectronic)
            Truth.assertThat(products).doesNotContain(productMensClothing)
            Truth.assertThat(products).doesNotContain(productWomensClothing)

            Truth.assertThat(products.first().category).isEqualTo(ProductCategories.ELECTRONICS.categoryName)
        }

        ProductCategories.MENS_CLOTHING -> {
            Truth.assertThat(products.size).isEqualTo(1)
            Truth.assertThat(products).doesNotContain(productJewelry)
            Truth.assertThat(products).doesNotContain(productElectronic)
            Truth.assertThat(products).contains(productMensClothing)
            Truth.assertThat(products).doesNotContain(productWomensClothing)

            Truth.assertThat(products.first().category).isEqualTo(ProductCategories.MENS_CLOTHING.categoryName)
        }

        ProductCategories.WOMENS_CLOTHING -> {
            Truth.assertThat(products.size).isEqualTo(1)
            Truth.assertThat(products).doesNotContain(productJewelry)
            Truth.assertThat(products).doesNotContain(productElectronic)
            Truth.assertThat(products).doesNotContain(productMensClothing)
            Truth.assertThat(products).contains(productWomensClothing)

            Truth.assertThat(products.first().category).isEqualTo(ProductCategories.WOMENS_CLOTHING.categoryName)
        }
    }
}

fun ProductViewModel.assertThatShimmerVisibilityIsInSyncWithLoadingState() {
    val loadingState = this.loadingState.getOrAwaitValue()
    if (loadingState == LoadingState.LOADING) {
        Espresso.onView(ViewMatchers.withId(R.id.products_shimmer))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        Espresso.onView(ViewMatchers.withId(R.id.products_swipe_refresh_layout))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    } else {
        Espresso.onView(ViewMatchers.withId(R.id.products_shimmer))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        Espresso.onView(ViewMatchers.withId(R.id.products_swipe_refresh_layout))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }
}

fun TestNavHostController.assertThatCurrentDestinationIsEqualTo(@IdRes id: Int) {
    Truth.assertThat(currentDestination?.id).isEqualTo(id)
}

fun TestNavHostController.assertThatCurrentDestinationIsNotEqualTo(@IdRes id: Int) {
    Truth.assertThat(currentDestination?.id).isNotEqualTo(id)
}