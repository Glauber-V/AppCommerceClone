package com.example.appcommerceclone.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.appcommerceclone.databinding.FragmentProductsBinding
import com.example.appcommerceclone.util.CommonVerifications.verifyUserConnectionToProceed
import com.example.appcommerceclone.viewmodels.ConnectivityViewModel
import com.example.appcommerceclone.viewmodels.ProductViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

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

        binding.productsSwipeRefreshLayout.setOnRefreshListener(this)
        setupProductsRecyclerView()

        verifyUserConnectionToProceed(connectionViewModel) {
            observeLoadingProcess()
            observeProductsListChanges()
        }
    }


    private fun setupProductsRecyclerView() {
        productsAdapter = ProductsAdapter { product ->
            productViewModel.selectProduct(product)
            navigateToProductDetailFragment()
        }
        binding.productsRecyclerView.apply {
            val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            layoutManager = staggeredGridLayoutManager
            adapter = productsAdapter
        }
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