package com.example.appcommerceclone.ui.cart

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.R
import com.example.appcommerceclone.databinding.FragmentCartAlertDialogBinding
import com.example.appcommerceclone.databinding.FragmentCartBinding
import com.example.appcommerceclone.viewmodels.CartViewModel
import com.example.appcommerceclone.viewmodels.UserOrdersViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment(
    private val cartViewModel: CartViewModel,
    private val userOrdersViewModel: UserOrdersViewModel
) : Fragment() {

    private lateinit var binding: FragmentCartBinding

    private lateinit var cartAdapter: CartAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCartProductsRecyclerView()
        observeCartProductsChanges()
        observeCartTotalPriceChanges()
        setupConfirmPurchaseBtnListener()
        setupCancelPurchaseBtnListener()
    }


    private fun setupCartProductsRecyclerView() {
        cartAdapter = CartAdapter(cartViewModel)
        binding.cartRecyclerView.adapter = cartAdapter
    }

    private fun observeCartProductsChanges() {
        cartViewModel.cartProducts.observe(viewLifecycleOwner) { cartProducts ->
            cartAdapter.submitList(cartProducts)
        }
    }

    private fun observeCartTotalPriceChanges() {
        cartViewModel.cartTotalPrice.observe(viewLifecycleOwner) { totalPrice ->
            val formattedPrice = cartViewModel.getFormattedTotalPrice(totalPrice)
            binding.cartTotalPrice.text = getString(R.string.cart_total_price, formattedPrice)
        }
    }


    private fun setupConfirmPurchaseBtnListener() {
        binding.cartConfirmPurchaseBtn.setOnClickListener {
            finishPurchase()
        }
    }

    private fun setupCancelPurchaseBtnListener() {
        binding.cartCancelPurchaseBtn.setOnClickListener {
            showAbandonCartAlertDialog()
        }
    }


    private fun finishPurchase() {
        cartViewModel.getOrder()?.also { order ->
            userOrdersViewModel.receiveOrder(order)
        }
        cartViewModel.onOrderDispatched()
        navigateToOrdersFragment()
    }

    private fun showAbandonCartAlertDialog() {
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


    private fun navigateToOrdersFragment() {
        val toDestination = CartFragmentDirections.actionCartFragmentToOrdersFragment()
        findNavController().navigate(toDestination)
    }
}