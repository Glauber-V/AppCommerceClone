package com.example.appcommerceclone.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.product.model.Product
import com.example.appcommerceclone.data.user.model.User
import com.example.appcommerceclone.databinding.FragmentProductDetailBinding
import com.example.appcommerceclone.ui.cart.CartViewModel
import com.example.appcommerceclone.ui.favorites.FavoritesViewModel
import com.example.appcommerceclone.ui.order.UserOrdersViewModel
import com.example.appcommerceclone.ui.user.UserViewModel
import com.example.appcommerceclone.util.showSnackbar
import com.example.appcommerceclone.util.verifyUserToProceed
import com.google.android.material.chip.Chip

class ProductDetailFragment(
    private val productViewModel: ProductViewModel,
    private val userViewModel: UserViewModel,
    private val favoritesViewModel: FavoritesViewModel,
    private val userOrdersViewModel: UserOrdersViewModel,
    private val cartViewModel: CartViewModel
) : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding

    private lateinit var selectedProduct: Product
    private var showOptions = false

    private var user: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productViewModel.selectedProduct.observe(viewLifecycleOwner) {
            it?.let { product ->
                selectedProduct = product
                binding.product = selectedProduct
                when (selectedProduct.category) {
                    ProductCategories.JEWELERY.categoryName -> hideOptions()
                    ProductCategories.ELECTRONICS.categoryName -> hideOptions()
                    else -> showOptions()
                }
            }
        }

        userViewModel.currentUser.observe(viewLifecycleOwner) { user = it }

        binding.productDetailAddToFavorites.setOnClickListener {
            verifyUserToProceed(user) {
                if (favoritesViewModel.isFavorite(selectedProduct)) {
                    showSnackbar(requireView(), getString(R.string.error_already_favorite))
                    return@verifyUserToProceed
                }

                favoritesViewModel.addToFavorites(selectedProduct)
                findNavController().navigate(
                    ProductDetailFragmentDirections.actionProductDetailFragmentToFavoritesFragment()
                )
            }
        }

        if (showOptions) {
            binding.productDetailColorsChipGroup.setOnCheckedStateChangeListener { group, _ ->
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

            binding.productDetailSizesChipGroup.setOnCheckedStateChangeListener { group, _ ->
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

        binding.productDetailAddToCart.setOnClickListener {
            if (validateSelectedOptions()) {
                cartViewModel.addToCart(selectedProduct)
                findNavController().navigate(
                    ProductDetailFragmentDirections.actionProductDetailFragmentToCartFragment()
                )
                productViewModel.onSelectedProductFinish()
            }
        }

        binding.productDetailBuyNow.setOnClickListener {
            if (!validateSelectedOptions()) return@setOnClickListener

            verifyUserToProceed(user) { verifiedUser ->
                showSnackbar(requireView(), getString(R.string.product_detail_thanks_for_purchase))
                userOrdersViewModel.createOrder(userId = verifiedUser.id, product = selectedProduct)
                findNavController().navigate(
                    ProductDetailFragmentDirections.actionGlobalOrdersFragment()
                )
                productViewModel.onSelectedProductFinish()
            }
        }
    }

    private fun showOptions() {
        showOptions = true
        binding.productDetailDivider1.visibility = View.VISIBLE
        binding.productDetailAvailableColors.visibility = View.VISIBLE
        binding.productDetailColorsChipGroup.visibility = View.VISIBLE
        binding.productDetailDivider2.visibility = View.VISIBLE
        binding.productDetailAvailableSizes.visibility = View.VISIBLE
        binding.productDetailSizesChipGroup.visibility = View.VISIBLE
    }

    private fun hideOptions() {
        showOptions = false
        binding.productDetailDivider1.visibility = View.GONE
        binding.productDetailAvailableColors.visibility = View.GONE
        binding.productDetailColorsChipGroup.visibility = View.GONE
        binding.productDetailDivider2.visibility = View.GONE
        binding.productDetailAvailableSizes.visibility = View.GONE
        binding.productDetailSizesChipGroup.visibility = View.GONE
    }

    private fun validateSelectedOptions(): Boolean {
        if (!showOptions)
            return true

        val colorsChipGroup = binding.productDetailColorsChipGroup
        val sizesChipGroup = binding.productDetailSizesChipGroup

        if (colorsChipGroup.checkedChipId == View.NO_ID && sizesChipGroup.checkedChipId == View.NO_ID) {
            showSnackbar(requireView(), getString(R.string.error_no_color_and_size_selected))
            return false
        }

        if (colorsChipGroup.checkedChipId == View.NO_ID) {
            showSnackbar(requireView(), getString(R.string.error_no_color_selected))
            return false
        }

        if (sizesChipGroup.checkedChipId == View.NO_ID) {
            showSnackbar(requireView(), getString(R.string.error_no_size_selected))
            return false
        }

        return true
    }
}