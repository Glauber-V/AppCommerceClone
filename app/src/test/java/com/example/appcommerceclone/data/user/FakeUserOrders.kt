package com.example.appcommerceclone.data.user

import com.example.appcommerceclone.data.product.FakeProductsRepository.Companion.productElectronic
import com.example.appcommerceclone.data.product.FakeProductsRepository.Companion.productJewelery
import com.example.appcommerceclone.data.product.FakeProductsRepository.Companion.productMensClothing
import com.example.appcommerceclone.data.product.FakeProductsRepository.Companion.productWomensClothing
import com.example.appcommerceclone.data.user.FakeUserAuthenticator.Companion.firstUser
import com.example.appcommerceclone.data.user.FakeUserAuthenticator.Companion.secondUser
import com.example.appcommerceclone.model.order.Order
import com.example.appcommerceclone.model.order.OrderedProduct
import com.example.appcommerceclone.model.user.User

class FakeUserOrders : UserOrders {

    override suspend fun getOrdersByUserId(userId: Int): MutableList<Order> {
        val currentUser: User = when (userId) {
            firstUser.id -> firstUser
            secondUser.id -> secondUser
            else -> null
        } ?: return mutableListOf()

        val orders = mutableListOf<Order>()
        val orderedProducts = mutableListOf<OrderedProduct>()

        val orderedProductJewelery = OrderedProduct(product = productJewelery, quantity = 4)
        val orderedProductElectronic = OrderedProduct(product = productElectronic, quantity = 6)
        val orderedProductWomensClothing = OrderedProduct(product = productWomensClothing, quantity = 2)
        val orderedProductMensClothing = OrderedProduct(product = productMensClothing, quantity = 1)

        orderedProducts.add(orderedProductJewelery)
        orderedProducts.add(orderedProductElectronic)
        orderedProducts.add(orderedProductWomensClothing)
        orderedProducts.add(orderedProductMensClothing)

        val order1 = Order(
            id = 1,
            userId = currentUser.id,
            date = "26/10/22",
            orderedProducts = orderedProducts,
            total = orderedProducts.getTotalPrice()
        )

        val order2 = Order(
            id = 2,
            userId = currentUser.id,
            date = "24/10/22",
            orderedProducts = orderedProducts,
            total = orderedProducts.getTotalPrice()
        )

        orders.add(order1)
        orders.add(order2)

        return orders
    }

    private fun List<OrderedProduct>.getTotalPrice(): Double =
        sumOf { it.product.price * it.quantity }
}