package com.example.appcommerceclone.ui.order

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.appcommerceclone.databinding.FragmentOrdersBinding
import com.example.appcommerceclone.util.navigateToProductsFragment
import com.example.appcommerceclone.util.onBackPressedReturnToProductsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment(private val userOrdersViewModel: UserOrdersViewModel) : Fragment(), AppBarConfiguration.OnNavigateUpListener {

    private lateinit var binding: FragmentOrdersBinding

    private lateinit var ordersAdapter: OrdersAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        onBackPressedReturnToProductsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ordersAdapter = OrdersAdapter()
        binding.ordersRecyclerview.adapter = ordersAdapter

        userOrdersViewModel.orders.observe(viewLifecycleOwner) { _orders ->
            ordersAdapter.submitList(_orders)
        }
    }

    override fun onNavigateUp(): Boolean {
        return navigateToProductsFragment()
    }
}