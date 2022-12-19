package com.example.appcommerceclone.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.data.user.FakeUserAuthenticator
import com.example.appcommerceclone.data.user.FakeUserOrders
import com.example.appcommerceclone.getOrAwaitValue
import com.example.appcommerceclone.model.order.Order
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserOrdersViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeUserAuthenticator: FakeUserAuthenticator
    private lateinit var fakeUserOrders: FakeUserOrders
    private lateinit var userOrdersViewModel: UserOrdersViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        fakeUserAuthenticator = FakeUserAuthenticator()
        fakeUserOrders = FakeUserOrders()
        userOrdersViewModel = UserOrdersViewModel(fakeUserOrders)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `refresh user orders with correct user id, return orders list`() = runTest {
        userOrdersViewModel.refreshUserOrders(FakeUserAuthenticator.ID)
        advanceUntilIdle()

        val result = userOrdersViewModel.orders.getOrAwaitValue()

        assertThat(result).isNotEmpty()
    }

    @Test
    fun `try refresh user orders with wrong user id, return empty list`() = runTest {
        userOrdersViewModel.refreshUserOrders(4)
        advanceUntilIdle()

        val result = userOrdersViewModel.orders.getOrAwaitValue()

        assertThat(result).isEmpty()
    }

    @Test
    fun `receive order and verify is not null`() {
        userOrdersViewModel.receiveOrder(Order())

        val result = userOrdersViewModel.order.getOrAwaitValue()

        assertThat(result).isNotNull()
    }

    @Test
    fun `process an order and verify it was added the orders list`() {
        val order = Order()
        userOrdersViewModel.processOrder(order, 2)

        val orders = userOrdersViewModel.orders.getOrAwaitValue()

        assertThat(orders).contains(order)
    }

    @Test
    fun `select an order and complete selection`() {
        val order = Order()

        userOrdersViewModel.selectOrder(order)
        val result1 = userOrdersViewModel.selectedOrder.getOrAwaitValue()
        assertThat(result1).isNotNull()

        userOrdersViewModel.onSelectOrderComplete()
        val result2 = userOrdersViewModel.selectedOrder.getOrAwaitValue()
        assertThat(result2).isNull()
    }
}