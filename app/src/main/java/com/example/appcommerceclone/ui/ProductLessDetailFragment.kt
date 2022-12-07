package com.example.appcommerceclone.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.appcommerceclone.R
import com.example.appcommerceclone.databinding.FragmentProductLessDetailBinding
import com.example.appcommerceclone.ui.BaseNavigation.navigateToCartFragment
import com.example.appcommerceclone.ui.BaseNavigation.navigateToFavoritesFragment
import com.example.appcommerceclone.util.ViewExt
import com.example.appcommerceclone.viewmodels.CartViewModel
import com.example.appcommerceclone.viewmodels.FavoritesViewModel
import com.example.appcommerceclone.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductLessDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductLessDetailBinding

    val productViewModel by activityViewModels<ProductViewModel>()
    private val favoritesViewModel by activityViewModels<FavoritesViewModel>()
    private val cartViewModel by activityViewModels<CartViewModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProductLessDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProductBinding()
        setupFavoritesFunctionality()
        setupAddToCartFunctionality()
        setupBuyNowFunctionality()
    }


    private fun setupProductBinding() {
        productViewModel.selectedProduct.observe(viewLifecycleOwner) { hasProduct ->
            hasProduct?.let { product ->
                binding.product = product
            }
        }
    }

    private fun setupFavoritesFunctionality() {
        binding.productLessDetailAddToFavorites.setOnClickListener {
            if (favoritesViewModel.addToFavorites(binding.product!!)) {
                navigateToFavoritesFragment()
            } else {
                ViewExt.showMessage(binding.root, getString(R.string.product_detail_favorites_warning))
            }
        }
    }

    private fun setupAddToCartFunctionality() {
        binding.productLessDetailAddToCart.setOnClickListener {
            cartViewModel.addToCart(binding.product!!)
            productViewModel.onSelectedProductFinish()
            navigateToCartFragment()
        }
    }

    private fun setupBuyNowFunctionality() {
        binding.productLessDetailBuyNow.setOnClickListener {
            ViewExt.showMessage(requireView(), getString(R.string.product_detail_thanks_for_purchase))
        }
    }
}