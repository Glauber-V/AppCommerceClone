package com.example.appcommerceclone.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appcommerceclone.data.dispatcher.FakeDispatcherProvider
import com.example.appcommerceclone.data.user.FakeUserAuthenticator
import com.example.appcommerceclone.data.user.FakeUserOrders
import com.example.appcommerceclone.data.user.FakeUserProvider
import com.example.appcommerceclone.data.user.FakeUserProvider.Companion.firstUser
import com.example.appcommerceclone.model.order.Order
import com.example.appcommerceclone.util.TestMainDispatcherRule
import com.example.appcommerceclone.util.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserOrdersViewModelTest {

    @get:Rule(order = 0)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 1)
    val testMainDispatcherRule = TestMainDispatcherRule()

    private lateinit var usersProvider: FakeUserProvider
    private lateinit var dispatcherProvider: FakeDispatcherProvider
    private lateinit var userAuthenticator: FakeUserAuthenticator
    private lateinit var userOrders: FakeUserOrders
    private lateinit var userOrdersViewModel: UserOrdersViewModel

    @Before
    fun setUp() {
        usersProvider = FakeUserProvider()
        dispatcherProvider = FakeDispatcherProvider()
        userAuthenticator = FakeUserAuthenticator(usersProvider, dispatcherProvider)
        userOrders = FakeUserOrders(usersProvider, dispatcherProvider)
        userOrdersViewModel = UserOrdersViewModel(userOrders, dispatcherProvider)
    }

    @Test
    fun refreshUserOrders_withCorrectUserId_ordersListShouldNotBeEmpty() = runTest {
        var orders = userOrdersViewModel.orders.getOrAwaitValue()
        assertThat(orders).isEmpty()

        userOrdersViewModel.refreshUserOrders(firstUser.id)
        advanceUntilIdle()

        orders = userOrdersViewModel.orders.getOrAwaitValue()
        assertThat(orders).isNotEmpty()
    }

    @Test
    fun refreshUserOrders_withWrongUserId_orderListShouldBeEmpty() = runTest {
        var orders = userOrdersViewModel.orders.getOrAwaitValue()
        assertThat(orders).isEmpty()

        userOrdersViewModel.refreshUserOrders(2018)
        advanceUntilIdle()

        orders = userOrdersViewModel.orders.getOrAwaitValue()
        assertThat(orders).isEmpty()
    }

    @Test
    fun receiveOrder_withId1_verifyIdIsReally1() {
        userOrdersViewModel.receiveOrder(Order(id = 1))
        val order = userOrdersViewModel.order.getOrAwaitValue()
        assertThat(order?.id).isEqualTo(1)
    }

    @Test
    fun processOrder_verifyOrderWasAddedToOrdersList_withCorrectUser() {
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

        var selectedOrder = userOrdersViewModel.selectedOrder.getOrAwaitValue()
        assertThat(selectedOrder).isNotNull()
        assertThat(selectedOrder).isEqualTo(order)

        userOrdersViewModel.onSelectOrderComplete()
        selectedOrder = userOrdersViewModel.selectedOrder.getOrAwaitValue()
        assertThat(selectedOrder).isNull()
    }
}