package com.example.appcommerceclone.ui.product

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.product.model.Product
import com.example.appcommerceclone.ui.common.DoubleActionButton
import com.example.appcommerceclone.ui.common.SecondaryActionButton
import com.example.appcommerceclone.util.getFormattedPrice
import com.example.appcommerceclone.util.productMensClothing

@Stable
class ProductDetailScreenUiState(private val product: Product) {

    val showOptions: Boolean
        get() {
            return when (product.category) {
                ProductCategories.JEWELERY.categoryName -> false
                ProductCategories.ELECTRONICS.categoryName -> false
                else -> true
            }
        }

    var selectedColor by mutableStateOf(ProductColors.NONE)
        private set

    var selectedSize by mutableStateOf(ProductSizes.NONE)
        private set

    fun validateSelections(): Boolean {
        return if (showOptions) {
            selectedColor != ProductColors.NONE && selectedSize != ProductSizes.NONE
        } else {
            true
        }
    }

    fun onSelectedColorChange(productColor: ProductColors) {
        selectedColor = productColor
    }

    fun onSelectedSizeChange(productSize: ProductSizes) {
        selectedSize = productSize
    }

    fun createValidationErrorMessage(context: Context): String {
        if (selectedColor == ProductColors.NONE && selectedSize == ProductSizes.NONE)
            return context.getString(R.string.error_no_color_and_size)

        if (selectedColor == ProductColors.NONE && selectedSize != ProductSizes.NONE)
            return context.getString(R.string.error_no_color_selected)

        if (selectedColor != ProductColors.NONE && selectedSize == ProductSizes.NONE)
            return context.getString(R.string.error_no_size_selected)

        return ""
    }
}

@Composable
fun rememberProductDetailScreenUiState(product: Product): ProductDetailScreenUiState {
    return remember(product) { ProductDetailScreenUiState(product) }
}

@Composable
fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    product: Product,
    isFavorite: Boolean,
    productDetailScreenUiState: ProductDetailScreenUiState = rememberProductDetailScreenUiState(product),
    onAddToFavorites: () -> Unit,
    onAddToFavoritesFailed: (errorMessage: String) -> Unit,
    onBuyNow: (message: String) -> Unit,
    onAddToCart: () -> Unit,
    onPurchaseFailed: (errorMessage: String) -> Unit,
    context: Context = LocalContext.current,
    verticaScrollState: ScrollState = rememberScrollState()
) {
    Box(modifier = modifier.verticalScroll(verticaScrollState)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(
                space = dimensionResource(id = R.dimen.padding_large),
                alignment = Alignment.Top
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier.size(width = 400.dp, height = 220.dp),
                model = ImageRequest.Builder(context)
                    .data(product.imageUrl)
                    .placeholder(R.drawable.place_holder)
                    .error(R.drawable.ic_broken_image)
                    .build(),
                contentDescription = null
            )
            Row(verticalAlignment = Alignment.Top) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = product.name,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.h5
                    )
                    Text(
                        text = product.getFormattedPrice(),
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.h6
                    )
                }
                SecondaryActionButton(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    onSecondaryAction = {
                        if (!isFavorite) {
                            onAddToFavorites()
                        } else {
                            onAddToFavoritesFailed(context.getString(R.string.error_already_favorite))
                        }
                    },
                    secondaryActionContent = {
                        Icon(
                            tint = colorResource(id = R.color.primaryColor),
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = stringResource(id = R.string.content_desc_add_to_favorites_btn)
                        )
                    }
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = product.description,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.body1
            )
            if (productDetailScreenUiState.showOptions) {
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = colorResource(id = R.color.divider_color),
                    thickness = dimensionResource(id = R.dimen.divider_size)
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = dimensionResource(id = R.dimen.padding_small),
                        alignment = Alignment.Start
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(ProductColors.values()) { colorOption ->
                        if (colorOption != ProductColors.NONE) {
                            ProductDetailChip(
                                modifier = Modifier
                                    .testTag(colorOption.name)
                                    .requiredSize(48.dp),
                                isSelected = colorOption == productDetailScreenUiState.selectedColor,
                                onChipSelected = { isChipSelected ->
                                    productDetailScreenUiState.onSelectedColorChange(
                                        if (isChipSelected) colorOption else ProductColors.NONE
                                    )
                                },
                                backGroundColor = colorOption.color
                            )
                        }
                    }
                }
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = colorResource(id = R.color.divider_color),
                    thickness = dimensionResource(id = R.dimen.divider_size)
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = dimensionResource(id = R.dimen.padding_small),
                        alignment = Alignment.Start
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(ProductSizes.values()) { sizeOption ->
                        if (sizeOption != ProductSizes.NONE) {
                            ProductDetailChip(
                                modifier = Modifier
                                    .testTag(sizeOption.name)
                                    .sizeIn(minWidth = 48.dp, minHeight = 48.dp),
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_size_small)),
                                isSelected = sizeOption == productDetailScreenUiState.selectedSize,
                                onChipSelected = { isChipSelected ->
                                    productDetailScreenUiState.onSelectedSizeChange(
                                        if (isChipSelected) sizeOption else ProductSizes.NONE
                                    )
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DoubleActionButton(
                    onPrimaryAction = {
                        if (productDetailScreenUiState.validateSelections()) {
                            onBuyNow(context.getString(R.string.product_detail_thanks_for_purchase))
                        } else {
                            onPurchaseFailed(productDetailScreenUiState.createValidationErrorMessage(context))
                        }
                    },
                    primaryActionContent = {
                        Text(
                            text = stringResource(id = R.string.product_detail_buy_now),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.button
                        )
                    },
                    onSecondaryAction = {
                        if (productDetailScreenUiState.validateSelections()) {
                            onAddToCart()
                        } else {
                            onPurchaseFailed(productDetailScreenUiState.createValidationErrorMessage(context))
                        }
                    },
                    secondaryActionContent = {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = stringResource(id = R.string.content_desc_add_to_cart_btn)
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
        modifier = modifier,
        shape = shape,
        color = backGroundColor,
        border = BorderStroke(
            width = if (isSelected) dimensionResource(id = R.dimen.stroke_size_medium) else dimensionResource(id = R.dimen.stroke_size_small),
            color = if (isSelected) colorResource(id = R.color.chip_checked_stroke_color)
            else colorResource(id = R.color.chip_unchecked_stroke_color)
        )
    ) {
        Row(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelected) {
                Icon(
                    modifier = Modifier.padding(
                        start = if (chipContent != null) dimensionResource(id = R.dimen.padding_small) else 0.dp,
                        end = if (chipContent != null) dimensionResource(id = R.dimen.padding_small) else 0.dp
                    ),
                    imageVector = Icons.Filled.Done,
                    contentDescription = null
                )
            }
            chipContent?.let { it() }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewProductDetailScreenContent() {
    MaterialTheme {
        ProductDetailScreen(
            product = productMensClothing,
            isFavorite = false,
            onAddToFavorites = {},
            onAddToFavoritesFailed = {},
            onBuyNow = {},
            onAddToCart = {},
            onPurchaseFailed = {}
        )
    }
}