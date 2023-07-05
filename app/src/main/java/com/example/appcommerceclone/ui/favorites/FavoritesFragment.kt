package com.example.appcommerceclone.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.appcommerceclone.R
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.ui.common.LeftToRightCard
import com.example.appcommerceclone.util.productList
import com.example.appcommerceclone.viewmodels.FavoritesViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment(
    private val userViewModel: UserViewModel,
    private val favoritesViewModel: FavoritesViewModel
) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val user by userViewModel.loggedUser.observeAsState(initial = null)
                if (user != null) {
                    val favoriteProducts by favoritesViewModel.favorites.observeAsState(initial = emptyList())
                    FavoritesScreen(
                        favoriteProducts = favoriteProducts,
                        onRemoveFavoriteProduct = { product: Product ->
                            favoritesViewModel.removeFromFavorites(product)
                        }
                    )
                } else {
                    findNavController().navigate(
                        FavoritesFragmentDirections.actionGlobalUserLoginFragment()
                    )
                }
            }
        }
    }
}

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    favoriteProducts: List<Product>,
    onRemoveFavoriteProduct: (Product) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = favoriteProducts,
            key = { product: Product -> product.id }
        ) { favoriteProduct: Product ->
            FavoriteProductItem(
                product = favoriteProduct,
                onRemoveFavoriteProduct = { onRemoveFavoriteProduct(favoriteProduct) }
            )
        }
    }
}

@Composable
fun FavoriteProductItem(
    modifier: Modifier = Modifier,
    product: Product,
    onRemoveFavoriteProduct: (Product) -> Unit
) {
    LeftToRightCard(modifier) {
        Row(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl)
                    .placeholder(R.drawable.place_holder)
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .build(),
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = dimensionResource(id = R.dimen.padding_medium))
            ) {
                Text(
                    text = product.name,
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6
                )
                Text(
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_small)),
                    text = product.getFormattedPrice(),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.subtitle1
                )
            }
            IconButton(
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium)),
                onClick = { onRemoveFavoriteProduct(product) },
                content = {
                    Icon(
                        tint = colorResource(id = R.color.primaryColor),
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = stringResource(id = R.string.content_desc_remove_from_favorites_btn)
                    )
                }
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewFavoritesScreen() {
    MaterialTheme {
        FavoritesScreen(
            favoriteProducts = productList,
            onRemoveFavoriteProduct = {}
        )
    }
}