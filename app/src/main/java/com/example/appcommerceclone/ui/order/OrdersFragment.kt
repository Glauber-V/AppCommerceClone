package com.example.appcommerceclone.ui.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.model.order.Order
import com.example.appcommerceclone.ui.common.LeftToRightCard
import com.example.appcommerceclone.util.getTotalPrice
import com.example.appcommerceclone.util.orderedProductList

@Composable
fun OrdersScreen(
    modifier: Modifier = Modifier,
    orders: List<Order>
) {
    if (orders.isNotEmpty()) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            items(orders) { order: Order ->
                OrderItem(order = order)
            }
        }
    } else {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.order_empty_history),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Composable
fun OrderItem(
    modifier: Modifier = Modifier,
    order: Order
) {
    LeftToRightCard(modifier) {
        Column(
            modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.Top,
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
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_small)),
                text = stringResource(
                    id = R.string.order_item_date,
                    order.date
                ),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.body1
            )
            Text(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_small)),
                text = stringResource(
                    id = R.string.order_item_products,
                    order.getOrderedProductListAsString()
                ),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.body1
            )
            Text(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_small)),
                text = stringResource(
                    id = R.string.order_item_total_price,
                    order.getFormattedPrice()
                ),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewOrdersScreen() {
    MaterialTheme {
        OrdersScreen(orders = emptyList())
    }
}

@Preview
@Composable
fun PreviewOrderItem() {
    MaterialTheme {
        OrderItem(
            modifier = Modifier.padding(8.dp),
            order = Order(
                id = 3846,
                userId = 9256,
                date = "29/06/2023",
                orderedProducts = orderedProductList,
                total = orderedProductList.getTotalPrice()
            )
        )
    }
}