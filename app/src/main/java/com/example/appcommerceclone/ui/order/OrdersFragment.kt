package com.example.appcommerceclone.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appcommerceclone.databinding.FragmentOrdersBinding
import com.example.appcommerceclone.model.user.User
import com.example.appcommerceclone.util.UserExt.verifyUserExistsToProceed
import com.example.appcommerceclone.viewmodels.UserOrdersViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment(
    private val userViewModel: UserViewModel,
    private val userOrdersViewModel: UserOrdersViewModel
) : Fragment() {

    private lateinit var binding: FragmentOrdersBinding

    private lateinit var ordersAdapter: OrdersAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifyUserExistsToProceed(userViewModel) { user ->
            processUnfinishedOrders(user)
            setupOrderRecycleView()
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

    private fun setupOrderRecycleView() {
        ordersAdapter = OrdersAdapter()
        binding.ordersRecyclerview.adapter = ordersAdapter
    }

    private fun observeOrdersListChanges() {
        userOrdersViewModel.orders.observe(viewLifecycleOwner) { orders ->
            if (orders.isNotEmpty()) ordersAdapter.submitList(orders)
        }
    }
}