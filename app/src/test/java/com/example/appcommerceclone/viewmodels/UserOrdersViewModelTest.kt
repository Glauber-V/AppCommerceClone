package com.example.appcommerceclone.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.data.user.FakeUserAuthenticator
import com.example.appcommerceclone.data.user.FakeUserAuthenticator.Companion.firstUser
import com.example.appcommerceclone.data.user.FakeUserOrders
import com.example.appcommerceclone.model.order.Order
import com.example.appcommerceclone.util.getOrAwaitValue
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
    fun refreshUserOrders_withCorrectUserId_ordersListShouldNotBeEmpty() = runTest {
        userOrdersViewModel.refreshUserOrders(firstUser.id)
        advanceUntilIdle()

        val result = userOrdersViewModel.orders.getOrAwaitValue()

        assertThat(result).isNotEmpty()
    }

    @Test
    fun refreshUserOrders_withWrongUserId_orderListShouldBeEmpty() = runTest {
        userOrdersViewModel.refreshUserOrders(99)
        advanceUntilIdle()

        val result = userOrdersViewModel.orders.getOrAwaitValue()

        assertThat(result).isEmpty()
    }

    @Test
    fun receiveOrder_orderIsNotNull() {
        userOrdersViewModel.receiveOrder(Order())

        val result = userOrdersViewModel.order.getOrAwaitValue()

        assertThat(result).isNotNull()
    }

    @Test
    fun processOrder_verifyOrderWasAddedToOrdersList() {
        val order = Order()
        userOrdersViewModel.processOrder(order, firstUser.id)

        val orders = userOrdersViewModel.orders.getOrAwaitValue()

        assertThat(orders).contains(order)
        assertThat(orders.first().userId).isEqualTo(firstUser.id)
    }

    @Test
    fun selectOrder_verifyOrderIsCompletedWithNullValue() {
        val order = Order()

        userOrdersViewModel.selectOrder(order)
        val result1 = userOrdersViewModel.selectedOrder.getOrAwaitValue()
        assertThat(result1).isNotNull()

        userOrdersViewModel.onSelectOrderComplete()
        val result2 = userOrdersViewModel.selectedOrder.getOrAwaitValue()
        assertThat(result2).isNull()
    }
}