package com.example.appcommerceclone.ui.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.appcommerceclone.data.product.model.Product
import com.example.appcommerceclone.databinding.FragmentFavoritesBinding
import com.example.appcommerceclone.util.navigateToProductsFragment
import com.example.appcommerceclone.util.onBackPressedReturnToProductsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment(private val favoritesViewModel: FavoritesViewModel) : Fragment(), FavoriteClickHandler, AppBarConfiguration.OnNavigateUpListener {

    private lateinit var binding: FragmentFavoritesBinding

    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        onBackPressedReturnToProductsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesAdapter = FavoritesAdapter(favoriteClickHandler = this@FavoritesFragment)
        binding.favoritesRecyclerView.adapter = favoritesAdapter

        favoritesViewModel.favorites.observe(viewLifecycleOwner) { _favorites ->
            if (_favorites.isEmpty()) {
                binding.favoritesRecyclerView.visibility = View.GONE
                binding.favoritesEmptyListPlaceholder.visibility = View.VISIBLE
            } else {
                binding.favoritesRecyclerView.visibility = View.VISIBLE
                binding.favoritesEmptyListPlaceholder.visibility = View.GONE
                favoritesAdapter.submitList(_favorites)
            }
        }
    }

    override fun onRemoveFromFavorites(product: Product) {
        favoritesViewModel.removeFromFavorites(product)
    }

    override fun onNavigateUp(): Boolean {
        return navigateToProductsFragment()
    }
}