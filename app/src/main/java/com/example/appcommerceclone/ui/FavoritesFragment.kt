package com.example.appcommerceclone.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcommerceclone.adapters.favorites.FavoriteClickListener
import com.example.appcommerceclone.adapters.favorites.FavoritesAdapter
import com.example.appcommerceclone.databinding.FragmentFavoritesBinding
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.ui.BaseUser.verifyUserConnectionToProceed
import com.example.appcommerceclone.ui.BaseUser.verifyUserExistsToProceed
import com.example.appcommerceclone.viewmodels.ConnectivityViewModel
import com.example.appcommerceclone.viewmodels.FavoritesViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(), FavoriteClickListener {

    private lateinit var binding: FragmentFavoritesBinding
    val connectivityViewModel by activityViewModels<ConnectivityViewModel>()
    val userViewModel by activityViewModels<UserViewModel>()
    val favoritesViewModel by activityViewModels<FavoritesViewModel>()

    private lateinit var favoritesAdapter: FavoritesAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifyUserConnectionToProceed(connectivityViewModel)
        verifyUserExistsToProceed(userViewModel) {
            setupFavoritesList()
        }
    }


    private fun setupFavoritesList() {
        favoritesAdapter = FavoritesAdapter(this)
        binding.favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = favoritesAdapter
        }

        favoritesViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            favoritesAdapter.submitList(favorites)
        }
    }

    override fun onFavoriteRemoved(product: Product) {
        favoritesViewModel.removeFromFavorites(product)
        val position = favoritesAdapter.currentList.indexOf(product)
        favoritesAdapter.notifyItemRemoved(position)
    }
}