package com.example.appcommerceclone.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.data.user.FakeUserProvider.Companion.firstUser
import com.example.appcommerceclone.ui.order.UserOrdersViewModel
import com.example.appcommerceclone.util.getOrAwaitValue
import com.example.appcommerceclone.util.orderedProductList
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserOrdersViewModelTest {

    @get:Rule(order = 0)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userOrdersViewModel: UserOrdersViewModel

    @Before
    fun setUp() {
        userOrdersViewModel = UserOrdersViewModel()
    }

    @Test
    fun createNewUserOder_verifyOrderWasAddedToOrderList() {
        var orders = userOrdersViewModel.orders.getOrAwaitValue()
        assertThat(orders).isEmpty()

        userOrdersViewModel.createOrder(
            userId = firstUser.id,
            orderedProductList = orderedProductList
        )

        orders = userOrdersViewModel.orders.getOrAwaitValue()
        assertThat(orders).isNotEmpty()
        assertThat(orders).hasSize(1)
    }
}