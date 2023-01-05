package com.example.appcommerceclone.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcommerceclone.databinding.FragmentOrdersBinding
import com.example.appcommerceclone.model.order.Order
import com.example.appcommerceclone.model.user.User
import com.example.appcommerceclone.ui.CommonVerifications.verifyUserConnectionToProceed
import com.example.appcommerceclone.ui.CommonVerifications.verifyUserExistsToProceed
import com.example.appcommerceclone.viewmodels.ConnectivityViewModel
import com.example.appcommerceclone.viewmodels.UserOrdersViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : Fragment(), OrderClickListener {

    private lateinit var binding: FragmentOrdersBinding
    val connectivityViewModel by activityViewModels<ConnectivityViewModel>()
    val userViewModel by activityViewModels<UserViewModel>()
    val userOrdersViewModel by activityViewModels<UserOrdersViewModel>()

    private lateinit var ordersAdapter: OrdersAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifyUserConnectionToProceed(connectivityViewModel)
        verifyUserExistsToProceed(userViewModel) { user ->
            processUnfinishedOrders(user)
            setupOrderRecycleView(user)
            observeOrdersListChanges()
        }
    }


    private fun processUnfinishedOrders(user: User) {
        userOrdersViewModel.order.observe(viewLifecycleOwner) { hasOrder ->
            hasOrder?.let { order ->
                userOrdersViewModel.processOrder(order, user.id)
            }
        }
    }

    private fun setupOrderRecycleView(user: User) {
        ordersAdapter = OrdersAdapter(this)
        userOrdersViewModel.refreshUserOrders(user.id)
        binding.ordersRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = ordersAdapter
        }
    }

    private fun observeOrdersListChanges() {
        userOrdersViewModel.orders.observe(viewLifecycleOwner) { orders ->
            if (!orders.isNullOrEmpty()) {
                ordersAdapter.submitList(orders)
            }
        }
    }


    override fun onOrderClicked(order: Order) {
        userOrdersViewModel.selectOrder(order)
    }
}