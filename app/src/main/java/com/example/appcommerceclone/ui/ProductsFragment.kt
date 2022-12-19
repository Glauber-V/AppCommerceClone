package com.example.appcommerceclone.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.appcommerceclone.adapters.product.ProductsAdapter
import com.example.appcommerceclone.databinding.FragmentProductsBinding
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.adapters.product.ProductClickListener
import com.example.appcommerceclone.ui.BaseNavigation.navigateToProductDetail
import com.example.appcommerceclone.ui.BaseUser.verifyUserConnectionToProceed
import com.example.appcommerceclone.viewmodels.ConnectivityViewModel
import com.example.appcommerceclone.viewmodels.ProductViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment(), ProductClickListener {

    private lateinit var binding: FragmentProductsBinding
    val connectionViewModel by activityViewModels<ConnectivityViewModel>()
    val userViewModel by activityViewModels<UserViewModel>()
    val productViewModel by activityViewModels<ProductViewModel>()


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
        }
    }


    private fun setupProductsRecyclerView() {
        binding.productsShimmer.startShimmer()

        productViewModel.refreshProducts()

        val productsAdapter = ProductsAdapter(this)

        binding.productsRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            adapter = productsAdapter
        }

        productViewModel.products.observe(viewLifecycleOwner) { products ->
            if (!products.isNullOrEmpty()) {
                productsAdapter.submitList(products)
                binding.productsShimmer.stopShimmer()
                binding.productsShimmer.visibility = View.GONE
                binding.productsRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    override fun onProductClicked(product: Product) {
        productViewModel.selectProduct(product)
        navigateToProductDetail(product)
    }
}