package com.example.appcommerceclone.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.product.model.Product
import com.example.appcommerceclone.ui.common.LeftToRightCard
import com.example.appcommerceclone.ui.common.PlaceHolder
import com.example.appcommerceclone.util.productList

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    favoriteProducts: List<Product>,
    onRemoveFavoriteProduct: (Product) -> Unit
) {
    if (favoriteProducts.isNotEmpty()) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(top = dimensionResource(id = R.dimen.padding_small)),
            verticalArrangement = Arrangement.spacedBy(
                space = dimensionResource(id = R.dimen.padding_small),
                alignment = Alignment.Top
            ),
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
    } else {
        PlaceHolder(
            modifier = modifier.fillMaxSize(),
            placeHolderText = stringResource(id = R.string.place_holder_text_no_favorites)
        )
    }
}

@Composable
fun FavoriteProductItem(
    modifier: Modifier = Modifier,
    product: Product,
    onRemoveFavoriteProduct: (Product) -> Unit
) {
    LeftToRightCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = dimensionResource(id = R.dimen.padding_large))
    ) {
        Row(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.spacedBy(
                space = dimensionResource(id = R.dimen.padding_medium),
                alignment = Alignment.Start
            ),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl)
                    .placeholder(R.drawable.place_holder)
                    .error(R.drawable.ic_broken_image)
                    .build(),
                contentDescription = null
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(
                    space = dimensionResource(id = R.dimen.padding_small),
                    alignment = Alignment.Top
                )
            ) {
                Text(
                    text = product.name,
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = product.getFormattedPrice(),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.subtitle1
                )
            }
            IconButton(
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewFavoritesScreenWithEmptyList() {
    MaterialTheme {
        FavoritesScreen(
            favoriteProducts = emptyList(),
            onRemoveFavoriteProduct = {}
        )
    }
}