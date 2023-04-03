package com.example.appcommerceclone.data.user

import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productElectronic
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productJewelery
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productMensClothing
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productWomensClothing
import com.example.appcommerceclone.model.order.Order
import com.example.appcommerceclone.model.order.OrderedProduct
import com.example.appcommerceclone.model.user.Address
import com.example.appcommerceclone.model.user.Name
import com.example.appcommerceclone.model.user.User
import com.example.appcommerceclone.model.user.UserToken

class FakeUserProvider : UsersProvider {

    private val users = listOf(firstUser, secondUser)


    override suspend fun getAllUsers(): List<User> {
        return users
    }

    override suspend fun verifyUserExist(username: String, password: String): UserToken {
        val user = users.firstOrNull { it.username == username && it.password == password } ?: return UserToken("")
        return when (user.id) {
            1 -> UserToken("User1Token")
            2 -> UserToken("User2Token")
            else -> UserToken("There is something wrong!")
        }
    }

    override suspend fun getUserById(id: Int): User {
        return users.firstOrNull { it.id == id } ?: User()
    }

    override suspend fun getUserOrders(userId: Int): MutableList<Order> {
        val user: User? = users.firstOrNull { it.id == userId }
        return if (user != null) createOrdersForUserWithId(userId) else mutableListOf()
    }


    private fun createOrdersForUserWithId(id: Int): MutableList<Order> {

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
            userId = id,
            date = "26/10/22",
            orderedProducts = orderedProducts,
            total = orderedProducts.sumOf { it.product.price * it.quantity }
        )

        val order2 = Order(
            id = 2,
            userId = id,
            date = "24/10/22",
            orderedProducts = orderedProducts,
            total = orderedProducts.sumOf { it.product.price * it.quantity }
        )

        orders.add(order1)
        orders.add(order2)

        return orders
    }


    companion object {

        val firstUser = User(
            id = 1,
            name = Name(
                firstname = "User1FirstName",
                lastname = "User1LastName"
            ),
            username = "User1",
            password = "123",
            email = "user1@hotmail.com",
            phone = "55 99876-5432",
            address = Address()
        )

        val secondUser = User(
            id = 2,
            name = Name(
                firstname = "User2FirstName",
                lastname = "User2LastName"
            ),
            username = "User2",
            password = "321",
            email = "user2@hotmail.com",
            phone = "55 91234-5678",
            address = Address()
        )
    }
}