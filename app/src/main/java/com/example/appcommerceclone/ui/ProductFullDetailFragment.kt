package com.example.appcommerceclone.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.appcommerceclone.R
import com.example.appcommerceclone.databinding.FragmentProductFullDetailBinding
import com.example.appcommerceclone.ui.BaseNavigation.navigateToCartFragment
import com.example.appcommerceclone.ui.BaseNavigation.navigateToFavoritesFragment
import com.example.appcommerceclone.util.ViewExt
import com.example.appcommerceclone.viewmodels.CartViewModel
import com.example.appcommerceclone.viewmodels.FavoritesViewModel
import com.example.appcommerceclone.viewmodels.ProductViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFullDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductFullDetailBinding
    val productViewModel by activityViewModels<ProductViewModel>()
    private val favoritesViewModel by activityViewModels<FavoritesViewModel>()
    private val cartViewModel by activityViewModels<CartViewModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductFullDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProductBinding()
        setupAddToFavoritesFunctionality()
        setupColorsChipGroup()
        setupSizesChipGroup()
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

    private fun setupAddToFavoritesFunctionality() {
        binding.productFullDetailAddToFavorites.setOnClickListener {
            if (favoritesViewModel.addToFavorites(binding.product!!)) {
                navigateToFavoritesFragment()
            } else {
                ViewExt.showMessage(binding.root, getString(R.string.product_detail_favorites_warning))
            }
        }
    }

    private fun setupAddToCartFunctionality() {
        binding.productFullDetailAddToCart.setOnClickListener {
            if (isChipsSelected()) {
                cartViewModel.addToCart(binding.product!!)
                productViewModel.onSelectedProductFinish()
                navigateToCartFragment()
            }
        }
    }

    private fun setupBuyNowFunctionality() {
        binding.productFullDetailBuyNow.setOnClickListener {
            if (isChipsSelected()) {
                ViewExt.showMessage(requireView(), getString(R.string.product_detail_thanks_for_purchase))
            }
        }
    }


    private fun setupColorsChipGroup() {
        val colorsChipGroup = binding.productFullDetailColorsChipGroup
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
        val sizesChipGroup = binding.productFullDetailSizesChipGroup
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

        val colorsChipGroup = binding.productFullDetailColorsChipGroup
        val sizesChipGroup = binding.productFullDetailSizesChipGroup

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
}