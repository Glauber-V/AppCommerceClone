package com.example.appcommerceclone.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.data.user.FakeUserProvider.Companion.firstUser
import com.example.appcommerceclone.model.order.Order
import com.example.appcommerceclone.util.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserOrdersViewModelTest {

    @get:Rule(order = 0)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userOrdersViewModel: UserOrdersViewModel

    @Before
    fun setUp() {
        userOrdersViewModel = UserOrdersViewModel()
    }

    @Test
    fun receiveOrder_processOrder_withUserId_verifyOrderWasAddedToOrderList() {
        val order = Order(id = 1)
        userOrdersViewModel.receiveOrder(order)

        val orderReceived = userOrdersViewModel.order.getOrAwaitValue()
        assertThat(orderReceived).isNotNull()
        assertThat(orderReceived).isEqualTo(order)

        userOrdersViewModel.processOrder(order = orderReceived!!, userId = firstUser.id)

        val orders = userOrdersViewModel.orders.getOrAwaitValue()
        assertThat(orders).isNotEmpty()
        assertThat(orders).contains(order)
    }
}