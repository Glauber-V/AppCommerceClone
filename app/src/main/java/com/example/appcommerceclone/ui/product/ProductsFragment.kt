package com.example.appcommerceclone.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.appcommerceclone.databinding.FragmentProductsBinding
import com.example.appcommerceclone.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment(private val productViewModel: ProductViewModel) : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentProductsBinding

    private lateinit var productsAdapter: ProductsAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.productsSwipeRefreshLayout.setOnRefreshListener(this)
        setupProductsRecyclerView()
        observeLoadingProcess()
        observeProductsListChanges()
    }


    private fun setupProductsRecyclerView() {
        productsAdapter = ProductsAdapter { product ->
            productViewModel.selectProduct(product)
            navigateToProductDetailFragment()
        }
        binding.productsRecyclerView.adapter = productsAdapter
    }

    private fun observeLoadingProcess() {
        productViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) startShimmer()
            else stopShimmer()
        }
    }

    private fun observeProductsListChanges() {
        productViewModel.products.observe(viewLifecycleOwner) { products ->
            if (products.isEmpty()) productViewModel.updateProductsList()
            else productsAdapter.submitList(products)
        }
    }


    private fun startShimmer() {
        binding.productsShimmer.startShimmer()
        binding.productsShimmer.visibility = View.VISIBLE
        binding.productsSwipeRefreshLayout.visibility = View.GONE
    }

    private fun stopShimmer() {
        binding.productsShimmer.stopShimmer()
        binding.productsShimmer.visibility = View.GONE
        binding.productsSwipeRefreshLayout.visibility = View.VISIBLE
        binding.productsSwipeRefreshLayout.isRefreshing = false
    }


    private fun navigateToProductDetailFragment() {
        val toDestination = ProductsFragmentDirections.actionProductsFragmentToProductDetailFragment()
        findNavController().navigate(toDestination)
    }

    override fun onRefresh() {
        productViewModel.updateProductsList()
    }
}