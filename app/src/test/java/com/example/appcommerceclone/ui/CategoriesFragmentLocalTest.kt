package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.appcommerceclone.R
import com.example.appcommerceclone.di.ProductsModule
import com.example.appcommerceclone.ui.product.CategoriesFragment
import com.example.appcommerceclone.util.TestNavHostControllerRule
import com.example.appcommerceclone.util.launchFragmentInHiltContainer
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@UninstallModules(ProductsModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class CategoriesFragmentLocalTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testNavHostControllerRule = TestNavHostControllerRule(R.id.categories_fragment)

    private lateinit var navHostController: TestNavHostController

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
    }

    @Test
    fun selectCategory_navigateToProductsFragment() {
        launchFragmentInHiltContainer<CategoriesFragment>(navHostController = navHostController) {

            onView(withId(R.id.product_category_electronics))
                .perform(click())

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.products_fragment)
        }
    }
}