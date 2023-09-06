package com.example.appcommerceclone.ui.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.product.model.Order
import com.example.appcommerceclone.ui.common.LeftToRightCard
import com.example.appcommerceclone.ui.common.PlaceHolder
import com.example.appcommerceclone.util.getFormattedTotalPrice
import com.example.appcommerceclone.util.getProductsNamesAsString
import com.example.appcommerceclone.util.orderedProductList

@Composable
fun OrdersScreen(
    modifier: Modifier = Modifier,
    orders: List<Order>
) {
    if (orders.isNotEmpty()) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(top = dimensionResource(id = R.dimen.padding_small)),
            verticalArrangement = Arrangement.spacedBy(
                space = dimensionResource(id = R.dimen.padding_medium),
                alignment = Alignment.Top
            ),
            horizontalAlignment = Alignment.Start
        ) {
            items(orders) { order: Order ->
                OrderItem(order = order)
            }
        }
    } else {
        PlaceHolder(
            modifier = modifier.fillMaxSize(),
            placeHolderText = stringResource(id = R.string.place_holder_text_no_orders)
        )
    }
}

@Composable
fun OrderItem(
    modifier: Modifier = Modifier,
    order: Order
) {
    LeftToRightCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = dimensionResource(id = R.dimen.padding_large))
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(
                space = dimensionResource(id = R.dimen.padding_small),
                alignment = Alignment.Top
            ),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(
                    id = R.string.order_item_id,
                    order.id
                ),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.body1
            )
            Text(
                text = stringResource(
                    id = R.string.order_item_date,
                    order.date
                ),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.body1
            )
            Text(
                text = stringResource(
                    id = R.string.order_item_products,
                    order.getProductsNamesAsString()
                ),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.body1
            )
            Text(
                text = stringResource(
                    id = R.string.order_item_total_price,
                    order.getFormattedTotalPrice()
                ),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewOrdersScreenWithNoPurchases() {
    MaterialTheme {
        OrdersScreen(orders = emptyList())
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewOrdersScreen() {
    MaterialTheme {
        OrdersScreen(
            orders = List(
                size = 4,
                init = {
                    Order(
                        id = it,
                        userId = 9256,
                        date = "29/06/2023",
                        orderedProducts = orderedProductList
                    )
                }
            )
        )
    }
}