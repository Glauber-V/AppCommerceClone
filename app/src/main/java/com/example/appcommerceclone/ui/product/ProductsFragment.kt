package com.example.appcommerceclone.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.appcommerceclone.data.product.model.Product
import com.example.appcommerceclone.databinding.FragmentProductsBinding
import com.example.appcommerceclone.util.LoadingState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment(private val productViewModel: ProductViewModel) : Fragment(), ProductClickHandler, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentProductsBinding

    private lateinit var productsAdapter: ProductsAdapter
    private var loadingState: LoadingState = LoadingState.NOT_STARTED
    private var products: List<Product> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.productsSwipeRefreshLayout.setOnRefreshListener(this)

        productsAdapter = ProductsAdapter(productClickHandler = this@ProductsFragment)
        binding.productsRecyclerView.adapter = productsAdapter

        productViewModel.loadingState.observe(viewLifecycleOwner) { _loadingState ->
            loadingState = _loadingState
            when (loadingState) {
                LoadingState.NOT_STARTED -> productViewModel.updateProductList()
                LoadingState.LOADING -> startShimmer()
                else -> stopShimmer()
            }
        }

        productViewModel.products.observe(viewLifecycleOwner) { _products ->
            products = _products
            if (products.isNotEmpty()) productsAdapter.submitList(products)
        }
    }

    override fun onRefresh() {
        when (loadingState) {
            LoadingState.FAILURE -> productViewModel.updateProductList()
            LoadingState.SUCCESS -> productViewModel.filterProductList(ProductCategories.NONE)
            else -> {}
        }
    }

    override fun onProductClicked(product: Product) {
        productViewModel.selectProduct(product)
        findNavController().navigate(
            ProductsFragmentDirections.actionProductsFragmentToProductDetailFragment()
        )
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
}