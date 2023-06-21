package com.example.appcommerceclone.ui.cart

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
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
import com.example.appcommerceclone.model.order.OrderedProduct
import com.example.appcommerceclone.ui.common.DoubleActionButton
import com.example.appcommerceclone.util.formatPrice
import com.example.appcommerceclone.util.orderedProductList
import com.example.appcommerceclone.viewmodels.CartViewModel
import com.example.appcommerceclone.viewmodels.UserOrdersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment(
    private val cartViewModel: CartViewModel,
    private val userOrdersViewModel: UserOrdersViewModel
) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val cartProducts by cartViewModel.cartProducts.observeAsState(initial = emptyList())
                val cartTotalPrice by cartViewModel.cartTotalPrice.observeAsState(initial = 0.0)
                CartScreen(
                    cartProducts = cartProducts,
                    onQuantityIncrease = { cartViewModel.increaseQuantity(it) },
                    onQuantityDecrease = { cartViewModel.decreaseQuantity(it) },
                    cartTotalPrice = cartTotalPrice.formatPrice(),
                    onAbandonCart = { cartViewModel.abandonCart() },
                    onConfirmPurchase = {
                        val order = cartViewModel.createOrder(cartProducts)
                        userOrdersViewModel.receiveOrder(order)
                        findNavController().navigate(
                            CartFragmentDirections.actionCartFragmentToOrdersFragment()
                        )
                    }
                )
            }
        }
    }
}

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
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OrderedProductListColumn(
            modifier = Modifier.weight(1f),
            cartProducts = cartProducts,
            onQuantityIncrease = onQuantityIncrease,
            onQuantityDecrease = onQuantityDecrease
        )
        Text(
            text = stringResource(id = R.string.cart_total_price, cartTotalPrice),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6
        )
        DoubleActionButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(id = R.dimen.padding_medium),
                    start = dimensionResource(id = R.dimen.padding_large),
                    end = dimensionResource(id = R.dimen.padding_large),
                    bottom = dimensionResource(id = R.dimen.padding_medium)
                ),
            onPrimaryAction = onConfirmPurchase,
            primaryActionEnabled = cartProducts.isNotEmpty(),
            primaryButtonColors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.primaryColor),
                contentColor = colorResource(id = R.color.white_100)
            ),
            primaryContent = {
                Text(
                    text = stringResource(id = R.string.cart_confirm_purchase_btn),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.button
                )
            },
            onSecondaryAction = onAbandonCart,
            secondaryActionEnabled = cartProducts.isNotEmpty(),
            secondaryButtonColors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = colorResource(id = R.color.primaryColor)
            ),
            secondaryContent = {
                Text(
                    text = stringResource(id = R.string.cart_cancel_purchase_btn),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.button
                )
            }
        )
    }
}

@Composable
fun OrderedProductListColumn(
    modifier: Modifier = Modifier,
    cartProducts: List<OrderedProduct>,
    onQuantityIncrease: (OrderedProduct) -> Unit,
    onQuantityDecrease: (OrderedProduct) -> Unit
) {
    LazyColumn(modifier) {
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
}

@Composable
fun CartProductItem(
    modifier: Modifier = Modifier,
    orderedProduct: OrderedProduct,
    onQuantityIncrease: () -> Unit,
    onQuantityDecrease: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(id = R.dimen.padding_small),
                end = dimensionResource(id = R.dimen.padding_small),
                bottom = dimensionResource(id = R.dimen.padding_small)
            ),
        shape = RoundedCornerShape(
            topEnd = dimensionResource(id = R.dimen.corner_size_small),
            bottomEnd = dimensionResource(id = R.dimen.corner_size_small)
        ),
        border = BorderStroke(
            width = dimensionResource(id = R.dimen.stroke_size_small),
            color = colorResource(id = R.color.stroke_color_dark)
        )
    ) {
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
                    .data(orderedProduct.product.imageUrl)
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
                    modifier = Modifier.fillMaxWidth(),
                    text = orderedProduct.product.name,
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6
                )
                Text(
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_small)),
                    text = orderedProduct.getFormattedPrice(),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.subtitle1
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onQuantityDecrease,
                        shape = CircleShape,
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_remove_24),
                                contentDescription = stringResource(id = R.string.content_desc_item_cart_decrease_btn)
                            )
                        }
                    )
                    Text(
                        modifier = Modifier.padding(
                            start = dimensionResource(id = R.dimen.padding_extra_large),
                            end = dimensionResource(id = R.dimen.padding_extra_large)
                        ),
                        text = orderedProduct.quantity.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6
                    )
                    Button(
                        onClick = onQuantityIncrease,
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.primaryColor),
                            contentColor = colorResource(id = R.color.white_100)
                        ),
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_add_24),
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