package com.example.appcommerceclone.ui.cart

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.product.model.OrderedProduct
import com.example.appcommerceclone.ui.common.DoubleActionButton
import com.example.appcommerceclone.ui.common.LeftToRightCard
import com.example.appcommerceclone.ui.common.PrimaryActionButton
import com.example.appcommerceclone.ui.common.SecondaryActionButton
import com.example.appcommerceclone.util.getFormattedPrice
import com.example.appcommerceclone.util.orderedProductList

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    cartProducts: List<OrderedProduct>,
    onQuantityIncrease: (OrderedProduct) -> Unit,
    onQuantityDecrease: (OrderedProduct) -> Unit,
    cartTotalPrice: String,
    onAbandonCart: () -> Unit,
    onConfirmPurchase: () -> Unit
) {
    var isDialogOpen by remember { mutableStateOf(false) }
    if (isDialogOpen) {
        AlertDialog(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_extra_large)),
            contentColor = colorResource(id = R.color.primaryColor),
            backgroundColor = colorResource(id = R.color.onPrimaryColor),
            properties = DialogProperties(usePlatformDefaultWidth = false),
            title = {
                Text(
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_extra_large)),
                    text = stringResource(id = R.string.cart_dialog_title),
                    style = MaterialTheme.typography.h6
                )
            },
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_size_small)),
            onDismissRequest = { isDialogOpen = false },
            confirmButton = {
                PrimaryActionButton(
                    onPrimaryAction = {
                        onAbandonCart()
                        isDialogOpen = false
                    },
                    primaryActionContent = {
                        Text(
                            text = stringResource(id = R.string.cart_dialog_positive),
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                )
            },
            dismissButton = {
                SecondaryActionButton(
                    onSecondaryAction = { isDialogOpen = false },
                    secondaryActionContent = {
                        Text(
                            text = stringResource(id = R.string.cart_dialog_negative),
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                )
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(
            space = dimensionResource(id = R.dimen.padding_medium),
            alignment = Alignment.Top
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(
                space = dimensionResource(id = R.dimen.padding_medium),
                alignment = Alignment.Top
            ),
            horizontalAlignment = Alignment.Start
        ) {
            items(
                items = cartProducts,
                key = { it.id }
            ) { orderedProduct: OrderedProduct ->
                CartProductItem(
                    orderedProduct = orderedProduct,
                    onQuantityIncrease = { onQuantityIncrease(orderedProduct) },
                    onQuantityDecrease = { onQuantityDecrease(orderedProduct) })
            }
        }
        Text(
            text = stringResource(id = R.string.cart_total_price, cartTotalPrice),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6
        )
        DoubleActionButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.padding_large)),
            onPrimaryAction = onConfirmPurchase,
            isPrimaryActionEnabled = cartProducts.isNotEmpty(),
            primaryActionContent = {
                Text(
                    text = stringResource(id = R.string.cart_confirm_purchase_btn),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.button
                )
            },
            onSecondaryAction = { isDialogOpen = true },
            isSecondaryActionEnabled = cartProducts.isNotEmpty(),
            secondaryActionContent = {
                Icon(
                    tint = colorResource(id = R.color.icon_color_black),
                    imageVector = Icons.Filled.RemoveShoppingCart,
                    contentDescription = null
                )
            }
        )
    }
}

@Composable
fun CartProductItem(
    modifier: Modifier = Modifier,
    orderedProduct: OrderedProduct,
    onQuantityIncrease: () -> Unit,
    onQuantityDecrease: () -> Unit
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
                    .data(orderedProduct.product.imageUrl)
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
                    modifier = Modifier.fillMaxWidth(),
                    text = orderedProduct.product.name,
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = orderedProduct.getFormattedPrice(),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.subtitle1
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        space = dimensionResource(id = R.dimen.padding_extra_large),
                        alignment = Alignment.Start
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SecondaryActionButton(
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        onSecondaryAction = onQuantityDecrease,
                        secondaryActionContent = {
                            Icon(
                                imageVector = Icons.Rounded.Remove,
                                contentDescription = stringResource(id = R.string.content_desc_item_cart_decrease_btn)
                            )
                        }
                    )
                    Text(
                        text = orderedProduct.quantity.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6
                    )
                    PrimaryActionButton(
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        onPrimaryAction = onQuantityIncrease,
                        primaryActionContent = {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = stringResource(id = R.string.content_desc_item_cart_increase_btn)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewCartScreen() {
    MaterialTheme {
        CartScreen(
            cartProducts = orderedProductList,
            onQuantityIncrease = { },
            onQuantityDecrease = { },
            cartTotalPrice = "R$ 39.99",
            onAbandonCart = { },
            onConfirmPurchase = { }
        )
    }
}