package com.example.appcommerceclone.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcommerceclone.adapters.ProductsAdapter
import com.example.appcommerceclone.databinding.FragmentProductsBinding
import com.example.appcommerceclone.ui.BaseNavigation.navigateToProductDetail
import com.example.appcommerceclone.ui.BaseUser.verifyUserConnectionToProceed
import com.example.appcommerceclone.viewmodels.ConnectivityViewModel
import com.example.appcommerceclone.viewmodels.ProductViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private lateinit var binding: FragmentProductsBinding
    val connectionViewModel by activityViewModels<ConnectivityViewModel>()
    val userViewModel by activityViewModels<UserViewModel>()
    val productViewModel by activityViewModels<ProductViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel.loadSavedUser()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifyUserConnectionToProceed(connectionViewModel) {
            setupProductsRecyclerView()
        }
    }


    private fun setupProductsRecyclerView() {
        binding.productsShimmer.startShimmer()

        productViewModel.refreshProducts()

        val productsAdapter = ProductsAdapter { product ->
            productViewModel.selectProduct(product)
            navigateToProductDetail(product)
        }
        binding.productsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = productsAdapter
        }

        productViewModel.products.observe(viewLifecycleOwner) { products ->
            productsAdapter.submitList(products)
            if (!products.isNullOrEmpty()) {
                binding.productsShimmer.stopShimmer()
                binding.productsShimmer.visibility = View.GONE
                binding.productsRecyclerView.visibility = View.VISIBLE
            }
        }
    }
}