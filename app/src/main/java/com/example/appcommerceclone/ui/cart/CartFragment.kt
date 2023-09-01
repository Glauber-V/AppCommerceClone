package com.example.appcommerceclone.ui.cart

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.product.model.OrderedProduct
import com.example.appcommerceclone.data.user.model.User
import com.example.appcommerceclone.databinding.FragmentCartAlertDialogBinding
import com.example.appcommerceclone.databinding.FragmentCartBinding
import com.example.appcommerceclone.ui.order.UserOrdersViewModel
import com.example.appcommerceclone.ui.user.UserViewModel
import com.example.appcommerceclone.util.formatPrice
import com.example.appcommerceclone.util.onBackPressedReturnToProductsFragment
import com.example.appcommerceclone.util.showSnackbar
import com.example.appcommerceclone.util.verifyUserToProceed
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment(
    private val cartViewModel: CartViewModel,
    private val userViewModel: UserViewModel,
    private val userOrdersViewModel: UserOrdersViewModel
) : Fragment(), CartClickHandler {

    private lateinit var binding: FragmentCartBinding

    private lateinit var cartAdapter: CartAdapter
    private var user: User? = null
    private var cartProducts: List<OrderedProduct> = emptyList()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        onBackPressedReturnToProductsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartAdapter = CartAdapter(cartClickHandler = this@CartFragment)
        binding.cartRecyclerView.adapter = cartAdapter

        cartViewModel.cartProducts.observe(viewLifecycleOwner) { _cartProducts ->
            cartProducts = _cartProducts

            if (cartProducts.isEmpty()) {
                binding.cartRecyclerView.visibility = View.GONE
                binding.cartEmptyListPlaceholder.visibility = View.VISIBLE
            } else {
                binding.cartRecyclerView.visibility = View.VISIBLE
                binding.cartEmptyListPlaceholder.visibility = View.GONE
                cartAdapter.submitList(cartProducts)
            }

            binding.cartCancelPurchaseBtn.isEnabled = cartProducts.isNotEmpty()
            binding.cartConfirmPurchaseBtn.isEnabled = cartProducts.isNotEmpty()
        }

        cartViewModel.cartTotalPrice.observe(viewLifecycleOwner) {
            binding.cartTotalPrice.text = getString(R.string.cart_total_price, it.formatPrice())
        }

        userViewModel.currentUser.observe(viewLifecycleOwner) { user = it }

        binding.cartConfirmPurchaseBtn.setOnClickListener {
            verifyUserToProceed(user) { verifiedUser: User ->
                showSnackbar(requireView(), getString(R.string.product_detail_thanks_for_purchase))
                userOrdersViewModel.createOrder(userId = verifiedUser.id, orderedProductList = cartProducts)
                findNavController().navigate(
                    CartFragmentDirections.actionCartFragmentToOrdersFragment()
                )
                cartViewModel.abandonCart()
            }
        }

        binding.cartCancelPurchaseBtn.setOnClickListener {
            val alertDialogBinding = FragmentCartAlertDialogBinding.inflate(requireActivity().layoutInflater)
            val alertDialog = MaterialAlertDialogBuilder(requireContext())
                .setView(alertDialogBinding.root)
                .setBackground(ColorDrawable(Color.TRANSPARENT))
                .create()

            alertDialog.show()

            alertDialogBinding.cartDialogNegative.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialogBinding.cartDialogPositive.setOnClickListener {
                cartViewModel.abandonCart()
                alertDialog.dismiss()
            }
        }
    }

    override fun onIncreaseQuantity(orderedProduct: OrderedProduct) {
        cartViewModel.increaseQuantity(orderedProduct)
    }

    override fun onDecreaseQuantity(orderedProduct: OrderedProduct) {
        cartViewModel.decreaseQuantity(orderedProduct)
    }
}