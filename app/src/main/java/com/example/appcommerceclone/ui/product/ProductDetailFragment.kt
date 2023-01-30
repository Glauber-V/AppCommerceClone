package com.example.appcommerceclone.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.R
import com.example.appcommerceclone.databinding.FragmentProductDetailBinding
import com.example.appcommerceclone.util.ViewExt
import com.example.appcommerceclone.viewmodels.CartViewModel
import com.example.appcommerceclone.viewmodels.FavoritesViewModel
import com.example.appcommerceclone.viewmodels.ProductViewModel
import com.google.android.material.chip.Chip

class ProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding
    val productViewModel by activityViewModels<ProductViewModel>()
    val favoritesViewModel by activityViewModels<FavoritesViewModel>()
    val cartViewModel by activityViewModels<CartViewModel>()

    private var isInFullMode = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupAddToFavoritesBtnListener()
        setupColorsChipGroup()
        setupSizesChipGroup()
        setupAddToCartBtnListener()
        setupBuyNowBtnListener()
    }


    private fun setupDataBinding() {
        productViewModel.selectedProduct.observe(viewLifecycleOwner) { hasProduct ->
            hasProduct?.let { product ->
                binding.product = product
                isInFullMode = productViewModel.checkIfShouldDisplayInFullDetailMode(product)
                binding.isInFullMode = isInFullMode
            }
        }
    }

    private fun setupAddToFavoritesBtnListener() {
        binding.productDetailAddToFavorites.setOnClickListener {
            if (favoritesViewModel.addToFavorites(binding.product!!)) {
                navigateToFavoritesFragment()
            } else {
                ViewExt.showSnackbar(requireView(), getString(R.string.product_detail_favorites_warning))
            }
        }
    }

    private fun setupAddToCartBtnListener() {
        binding.productDetailAddToCart.setOnClickListener {
            if (isChipsSelected()) {
                cartViewModel.addToCart(binding.product!!)
                productViewModel.onSelectedProductFinish()
                navigateToCartFragment()
            }
        }
    }

    private fun setupBuyNowBtnListener() {
        binding.productDetailBuyNow.setOnClickListener {
            if (isChipsSelected()) {
                ViewExt.showSnackbar(requireView(), getString(R.string.product_detail_thanks_for_purchase))
            }
        }
    }


    private fun setupColorsChipGroup() {
        val colorsChipGroup = binding.productDetailColorsChipGroup
        colorsChipGroup.setOnCheckedStateChangeListener { group, _ ->
            group.forEach {
                val chip = it as Chip
                if (chip.isChecked) {
                    chip.setChipStrokeColorResource(R.color.chip_checked_stroke_color)
                    chip.setChipStrokeWidthResource(R.dimen.chip_checked_stroke_width)
                } else {
                    chip.setChipStrokeColorResource(R.color.chip_unchecked_stroke_color)
                    chip.setChipStrokeWidthResource(R.dimen.chip_unchecked_stroke_width)
                }
            }
        }
    }

    private fun setupSizesChipGroup() {
        val sizesChipGroup = binding.productDetailSizesChipGroup
        sizesChipGroup.setOnCheckedStateChangeListener { group, _ ->
            group.forEach {
                val chip = it as Chip
                if (chip.isChecked) {
                    chip.setChipStrokeColorResource(R.color.chip_checked_stroke_color)
                    chip.setChipStrokeWidthResource(R.dimen.chip_checked_stroke_width)
                } else {
                    chip.setChipStrokeColorResource(R.color.chip_unchecked_stroke_color)
                    chip.setChipStrokeWidthResource(R.dimen.chip_unchecked_stroke_width)
                }
            }
        }
    }

    private fun isChipsSelected(): Boolean {
        if (!isInFullMode)
            return true

        val colorsChipGroup = binding.productDetailColorsChipGroup
        val sizesChipGroup = binding.productDetailSizesChipGroup

        if (colorsChipGroup.checkedChipId == View.NO_ID) {
            Toast.makeText(requireContext(), getString(R.string.product_detail_chip_color_warning), Toast.LENGTH_SHORT).show()
            return false
        }

        if (sizesChipGroup.checkedChipId == View.NO_ID) {
            Toast.makeText(requireContext(), getString(R.string.product_detail_chip_size_warning), Toast.LENGTH_SHORT).show()
            return false
        }

        if (colorsChipGroup.checkedChipId == View.NO_ID || sizesChipGroup.checkedChipId == View.NO_ID) {
            Toast.makeText(requireContext(), getString(R.string.product_detail_chip_color_and_size_warning), Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


    private fun navigateToFavoritesFragment() {
        val toDestination = ProductDetailFragmentDirections.actionProductDetailFragmentToFavoritesFragment()
        findNavController().navigate(toDestination)
    }

    private fun navigateToCartFragment() {
        val toDestination = ProductDetailFragmentDirections.actionProductDetailFragmentToCartFragment()
        findNavController().navigate(toDestination)
    }
}