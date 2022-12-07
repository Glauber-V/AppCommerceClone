package com.example.appcommerceclone.data.user

import com.example.appcommerceclone.model.order.Order
import com.example.appcommerceclone.model.order.OrderedProduct
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.model.user.Name
import com.example.appcommerceclone.model.user.User
import com.example.appcommerceclone.util.Constants

class FakeUserOrders : UserOrders {

    override suspend fun getOrdersByUserId(userId: Int): MutableList<Order> {
        if (userId != 1)
            return mutableListOf()

        val name = Name(firstname = "Orisa", lastname = "The horse")
        val user = User(id = 1, name = name, username = "Orisa", password = "321")

        val orders = mutableListOf<Order>()
        val orderedProducts = mutableListOf<OrderedProduct>()

        val product1 = Product(id = 1, price = 5.0, category = Constants.CATEGORY_JEWELRY)
        val product2 = Product(id = 2, price = 10.0, category = Constants.CATEGORY_ELECTRONICS)
        val product3 = Product(id = 3, price = 15.0, category = Constants.CATEGORY_MENS_CLOTHING)
        val product4 = Product(id = 4, price = 20.0, category = Constants.CATEGORY_WOMENS_CLOTHING)
        val product5 = Product(id = 5, price = 25.0, category = Constants.CATEGORY_ELECTRONICS)

        val orderedProduct1 = OrderedProduct(product = product1, quantity = 4)
        val orderedProduct2 = OrderedProduct(product = product2, quantity = 6)
        val orderedProduct3 = OrderedProduct(product = product3, quantity = 2)
        val orderedProduct4 = OrderedProduct(product = product4, quantity = 1)
        val orderedProduct5 = OrderedProduct(product = product5, quantity = 5)

        orderedProducts.add(orderedProduct1)
        orderedProducts.add(orderedProduct2)
        orderedProducts.add(orderedProduct3)
        orderedProducts.add(orderedProduct4)
        orderedProducts.add(orderedProduct5)

        val order1 = Order(
            id = 1,
            userId = user.id,
            date = "26/10/22",
            orderedProducts = orderedProducts,
            total = orderedProducts.getTotalPrice()
        )

        val order2 = Order(
            id = 2,
            userId = user.id,
            date = "24/10/22",
            orderedProducts = orderedProducts,
            total = orderedProducts.getTotalPrice()
        )

        orders.add(order1)
        orders.add(order2)

        return orders
    }

    private fun List<OrderedProduct>.getTotalPrice(): Double {
        var totalPrice = 0.0

        forEach { orderedProduct ->
            totalPrice += orderedProduct.product.price * orderedProduct.quantity
        }

        return totalPrice
    }
}