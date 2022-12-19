package com.example.appcommerceclone.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcommerceclone.R
import com.example.appcommerceclone.adapters.cart.CartAdapter
import com.example.appcommerceclone.adapters.cart.CartListener
import com.example.appcommerceclone.databinding.FragmentCartAlertDialogBinding
import com.example.appcommerceclone.databinding.FragmentCartBinding
import com.example.appcommerceclone.model.order.OrderedProduct
import com.example.appcommerceclone.ui.BaseNavigation.navigateToOrdersFragment
import com.example.appcommerceclone.viewmodels.CartViewModel
import com.example.appcommerceclone.viewmodels.UserOrdersViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(), CartListener {

    private lateinit var binding: FragmentCartBinding
    val userViewModel by activityViewModels<UserViewModel>()
    val cartViewModel by activityViewModels<CartViewModel>()
    val userOrdersViewModel by activityViewModels<UserOrdersViewModel>()

    private lateinit var cartAdapter: CartAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCartProductsRecyclerView()
        setupTotalPriceObserver()
        setupConfirmPurchaseBtn()
        setupCancelPurchaseBtn()
    }


    private fun setupCartProductsRecyclerView() {
        cartAdapter = CartAdapter(this)

        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }

        cartViewModel.cartProducts.observe(viewLifecycleOwner) { cartProducts ->
            cartAdapter.submitList(cartProducts)
        }
    }

    private fun setupTotalPriceObserver() {
        cartViewModel.cartTotalPrice.observe(viewLifecycleOwner) { totalPrice ->
            val formattedPrice = cartViewModel.getFormattedTotalPrice(totalPrice)
            binding.cartTotalPrice.text = getString(R.string.cart_total_price, formattedPrice)
        }
    }

    private fun setupConfirmPurchaseBtn() {
        binding.cartConfirmPurchaseBtn.setOnClickListener {
            finishPurchase()
        }
    }

    private fun setupCancelPurchaseBtn() {
        binding.cartCancelPurchaseBtn.setOnClickListener {
            showAbandonCartAlertDialog()
        }
    }


    override fun cartIncreaseQuantity(orderedProduct: OrderedProduct, adapterPosition: Int) {
        cartViewModel.increaseQuantity(orderedProduct)
        cartAdapter.notifyItemChanged(adapterPosition)
    }

    override fun cartDecreaseQuantity(orderedProduct: OrderedProduct, adapterPosition: Int) {
        cartViewModel.decreaseQuantity(orderedProduct)
        if (orderedProduct.quantity > 1) cartAdapter.notifyItemChanged(adapterPosition)
        else cartAdapter.notifyItemRemoved(adapterPosition)
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
}