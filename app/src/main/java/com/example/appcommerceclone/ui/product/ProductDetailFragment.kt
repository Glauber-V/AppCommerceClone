package com.example.appcommerceclone.ui.product

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.appcommerceclone.R
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.util.productMensClothing
import com.example.appcommerceclone.viewmodels.CartViewModel
import com.example.appcommerceclone.viewmodels.FavoritesViewModel
import com.example.appcommerceclone.viewmodels.ProductColors
import com.example.appcommerceclone.viewmodels.ProductSizes
import com.example.appcommerceclone.viewmodels.ProductViewModel
import kotlinx.coroutines.launch

class ProductDetailFragment(
    private val productViewModel: ProductViewModel,
    private val favoritesViewModel: FavoritesViewModel,
    private val cartViewModel: CartViewModel
) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val selectedProduct by productViewModel.selectedProduct.observeAsState(initial = null)
                selectedProduct?.let { product: Product ->
                    ProductDetailScreen(
                        showOptions = productViewModel.showMoreOptions(product),
                        product = product,
                        onAddToFavorites = {
                            favoritesViewModel.addToFavorites(product)
                            findNavController().navigate(
                                ProductDetailFragmentDirections.actionProductDetailFragmentToFavoritesFragment()
                            )
                        },
                        onBuyNow = { },
                        onAddToCart = {
                            cartViewModel.addToCart(product)
                            findNavController().navigate(
                                ProductDetailFragmentDirections.actionProductDetailFragmentToCartFragment()
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    product: Product,
    showOptions: Boolean,
    onAddToFavorites: () -> Unit,
    onBuyNow: () -> Unit,
    onAddToCart: () -> Unit
) {
    val context = LocalContext.current
    val verticaScrollState = rememberScrollState()
    val snackBarScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedColor by rememberSaveable { mutableStateOf(ProductColors.NONE) }
    var selectedSize by rememberSaveable { mutableStateOf(ProductSizes.NONE) }
    val canProceed = if (showOptions) selectedColor != ProductColors.NONE && selectedSize != ProductSizes.NONE else true

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->
        Column(modifier = modifier.verticalScroll(state = verticaScrollState)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProductDetails(
                    modifier = Modifier.padding(
                        top = dimensionResource(id = R.dimen.padding_large),
                        start = dimensionResource(id = R.dimen.padding_large),
                        end = dimensionResource(id = R.dimen.padding_large)
                    ),
                    product = product,
                    onAddToFavorites = onAddToFavorites
                )
                ProductOptions(
                    modifier = Modifier.padding(
                        top = dimensionResource(id = R.dimen.padding_large),
                        start = dimensionResource(id = R.dimen.padding_large),
                        end = dimensionResource(id = R.dimen.padding_large)
                    ),
                    isShowFullDetail = showOptions,
                    selectedColor = selectedColor,
                    onSelectedColorChange = { selectedColor = it },
                    selectedSize = selectedSize,
                    onSelectedSizeChange = { selectedSize = it }
                )
                PurchaseActions(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
                    canProceed = canProceed,
                    onProceedFailed = {
                        snackBarScope.launch {
                            snackbarHostState.showSnackbar(
                                message = context.createSnackbarMessage(selectedColor, selectedSize)
                            )
                        }
                    },
                    onAddToCart = onAddToCart,
                    onBuyNow = onBuyNow
                )
            }
        }
    }
}

@Composable
fun ProductDetails(
    modifier: Modifier = Modifier,
    product: Product,
    onAddToFavorites: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.item_product_image_height)),
            model = ImageRequest.Builder(LocalContext.current)
                .data(product.imageUrl)
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.ic_baseline_broken_image_24)
                .build(),
            contentDescription = null
        )
        Row(
            modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_large)),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = product.name,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.h5
                )
                Text(
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_medium)),
                    text = product.getFormattedPrice(),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.h6
                )
            }
            IconButton(
                onClick = onAddToFavorites,
                content = {
                    Icon(
                        tint = colorResource(id = R.color.primaryColor),
                        imageVector = Icons.Default.Favorite,
                        contentDescription = stringResource(id = R.string.content_desc_add_to_favorites_btn)
                    )
                }
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.padding_medium)),
            text = product.description,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun ProductOptions(
    modifier: Modifier = Modifier,
    isShowFullDetail: Boolean,
    selectedColor: ProductColors,
    onSelectedColorChange: (ProductColors) -> Unit,
    selectedSize: ProductSizes,
    onSelectedSizeChange: (ProductSizes) -> Unit
) {
    if (isShowFullDetail) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Divider(
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.padding_large))
                    .fillMaxWidth(),
                color = colorResource(id = R.color.divider_color),
                thickness = dimensionResource(id = R.dimen.divider_size)
            )
            ColorChipGroup(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_large)),
                selectedColor = selectedColor,
                onSelectedColorChange = { onSelectedColorChange(it) }
            )
            Divider(
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.padding_large))
                    .fillMaxWidth(),
                color = colorResource(id = R.color.divider_color),
                thickness = dimensionResource(id = R.dimen.divider_size)
            )
            SizeChipGroup(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_large)),
                selectedSize = selectedSize,
                onSelectedSizeChange = { onSelectedSizeChange(it) }
            )
        }
    }
}

@Composable
fun ColorChipGroup(
    modifier: Modifier = Modifier,
    selectedColor: ProductColors,
    onSelectedColorChange: (ProductColors) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(ProductColors.values()) { colorOption ->
            if (colorOption != ProductColors.NONE) {
                ProductDetailChip(
                    modifier = Modifier.testTag(colorOption.name),
                    isSelected = colorOption == selectedColor,
                    onChipSelected = { isChipSelected ->
                        if (isChipSelected) onSelectedColorChange(colorOption)
                        else onSelectedColorChange(ProductColors.NONE)
                    },
                    backGroundColor = colorOption.color
                )
            }
        }
    }
}

@Composable
fun SizeChipGroup(
    modifier: Modifier = Modifier,
    selectedSize: ProductSizes,
    onSelectedSizeChange: (ProductSizes) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(ProductSizes.values()) { sizeOption ->
            if (sizeOption != ProductSizes.NONE) {
                ProductDetailChip(
                    modifier = Modifier.testTag(sizeOption.name),
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_size_small)),
                    isSelected = sizeOption == selectedSize,
                    onChipSelected = { isChipSelected ->
                        if (isChipSelected) onSelectedSizeChange(sizeOption)
                        else onSelectedSizeChange(ProductSizes.NONE)
                    },
                    chipContent = {
                        Text(
                            text = sizeOption.size,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.body1
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductDetailChip(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onChipSelected: (Boolean) -> Unit,
    shape: Shape = CircleShape,
    backGroundColor: Color = Color.Transparent,
    chipContent: @Composable (() -> Unit)? = null
) {
    Surface(
        checked = isSelected,
        onCheckedChange = onChipSelected,
        modifier = modifier
            .size(48.dp)
            .padding(dimensionResource(id = R.dimen.padding_small)),
        shape = shape,
        color = backGroundColor,
        border = BorderStroke(
            width = if (isSelected) dimensionResource(id = R.dimen.stroke_size_medium) else dimensionResource(id = R.dimen.stroke_size_small),
            color = if (isSelected) colorResource(id = R.color.chip_checked_stroke_color)
            else colorResource(id = R.color.chip_unchecked_stroke_color)
        )
    ) {
        Row(
            modifier = modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelected) {
                Icon(
                    modifier = Modifier.padding(
                        start = dimensionResource(id = R.dimen.padding_small),
                        end = dimensionResource(id = R.dimen.padding_small)
                    ),
                    imageVector = Icons.Default.Done,
                    contentDescription = null
                )
            }
            chipContent?.let { it() }
        }
    }
}

@Composable
fun PurchaseActions(
    modifier: Modifier = Modifier,
    canProceed: Boolean,
    onAddToCart: () -> Unit,
    onBuyNow: () -> Unit,
    onProceedFailed: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = { if (canProceed) onAddToCart() else onProceedFailed() },
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.btn_min_height_size))
                .width(dimensionResource(id = R.dimen.btn_min_width_size)),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_size_small)),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = colorResource(id = R.color.icon_color_black)
            ),
            content = {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = stringResource(id = R.string.content_desc_add_to_cart_btn)
                )
            }
        )
        TextButton(
            onClick = { if (canProceed) onBuyNow() else onProceedFailed() },
            modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.padding_medium))
                .height(dimensionResource(id = R.dimen.btn_min_height_size))
                .weight(1f),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_size_small)),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.primaryColor),
                contentColor = colorResource(id = R.color.white_100)
            ),
            content = {
                Text(
                    text = stringResource(id = R.string.product_detail_buy_now),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.button
                )
            }
        )
    }
}

fun Context.createSnackbarMessage(selectedColor: ProductColors, selectedSize: ProductSizes): String {
    if (selectedColor == ProductColors.NONE && selectedSize == ProductSizes.NONE) return getString(R.string.product_detail_chip_color_and_size_warning)
    if (selectedColor == ProductColors.NONE) return getString(R.string.product_detail_chip_color_warning)
    if (selectedSize == ProductSizes.NONE) return getString(R.string.product_detail_chip_size_warning)

    return ""
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewProductDetailScreenContent() {
    MaterialTheme {
        ProductDetailScreen(
            showOptions = true,
            product = productMensClothing,
            onAddToFavorites = { },
            onBuyNow = { },
            onAddToCart = { }
        )
    }
}