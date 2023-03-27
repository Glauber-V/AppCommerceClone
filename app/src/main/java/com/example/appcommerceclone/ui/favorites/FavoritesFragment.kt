package com.example.appcommerceclone.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appcommerceclone.databinding.FragmentFavoritesBinding
import com.example.appcommerceclone.util.UserExt.verifyUserExistsToProceed
import com.example.appcommerceclone.viewmodels.FavoritesViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment(
    private val userViewModel: UserViewModel,
    private val favoritesViewModel: FavoritesViewModel
) : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding

    private lateinit var favoritesAdapter: FavoritesAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifyUserExistsToProceed(userViewModel) {
            setupFavoritesRecyclerView()
            observeFavoritesListChanges()
        }
    }


    private fun setupFavoritesRecyclerView() {
        favoritesAdapter = FavoritesAdapter(favoritesViewModel)
        binding.favoritesRecyclerView.adapter = favoritesAdapter

    }

    private fun observeFavoritesListChanges() {
        favoritesViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            favoritesAdapter.submitList(favorites)
        }
    }
}