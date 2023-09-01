package com.example.appcommerceclone.ui.order

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appcommerceclone.databinding.FragmentOrdersBinding
import com.example.appcommerceclone.util.onBackPressedReturnToProductsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment(private val userOrdersViewModel: UserOrdersViewModel) : Fragment() {

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
        binding.ordersRecyclerView.adapter = ordersAdapter

        userOrdersViewModel.orders.observe(viewLifecycleOwner) { _orders ->
            if (_orders.isEmpty()) {
                binding.ordersRecyclerView.visibility = View.GONE
                binding.ordersEmptyListPlaceholder.visibility = View.VISIBLE
            } else {
                binding.ordersRecyclerView.visibility = View.VISIBLE
                binding.ordersEmptyListPlaceholder.visibility = View.GONE
                ordersAdapter.submitList(_orders)
            }
        }
    }
}