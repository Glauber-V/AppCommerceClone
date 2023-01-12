package com.example.appcommerceclone.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcommerceclone.databinding.FragmentFavoritesBinding
import com.example.appcommerceclone.ui.CommonVerifications.verifyUserConnectionToProceed
import com.example.appcommerceclone.ui.CommonVerifications.verifyUserExistsToProceed
import com.example.appcommerceclone.viewmodels.ConnectivityViewModel
import com.example.appcommerceclone.viewmodels.FavoritesViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

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
            setupFavoritesRecyclerView()
            observeFavoritesListChanges()
        }
    }


    private fun setupFavoritesRecyclerView() {
        favoritesAdapter = FavoritesAdapter(favoritesViewModel)
        binding.favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = favoritesAdapter
        }
    }

    private fun observeFavoritesListChanges() {
        favoritesViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            favoritesAdapter.submitList(favorites)
        }
    }
}