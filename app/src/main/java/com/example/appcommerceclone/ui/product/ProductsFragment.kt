package com.example.appcommerceclone.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.appcommerceclone.databinding.FragmentProductsBinding
import com.example.appcommerceclone.ui.CommonVerifications.verifyUserConnectionToProceed
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

    private lateinit var productsAdapter: ProductsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel.loadSavedUser()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifyUserConnectionToProceed(connectionViewModel) {
            setupProductsRecyclerView()
            observeProductsListChanges()
        }
    }


    private fun setupProductsRecyclerView() {
        productsAdapter = ProductsAdapter { product ->
            productViewModel.selectProduct(product)
            navigateToProductDetailFragment()
        }
        binding.productsRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            adapter = productsAdapter
        }
    }

    private fun observeProductsListChanges() {
        binding.productsShimmer.startShimmer()
        productViewModel.products.observe(viewLifecycleOwner) { products ->
            if (products.isEmpty()) {
                productViewModel.updateProductsList()
            } else {
                productsAdapter.submitList(products)
                binding.productsShimmer.stopShimmer()
                binding.productsShimmer.visibility = View.GONE
                binding.productsRecyclerView.visibility = View.VISIBLE
            }
        }
    }


    private fun navigateToProductDetailFragment() {
        val toDestination = ProductsFragmentDirections.actionProductsFragmentToProductDetailFragment()
        findNavController().navigate(toDestination)
    }
}