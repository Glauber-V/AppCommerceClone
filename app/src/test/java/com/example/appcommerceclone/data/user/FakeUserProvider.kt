package com.example.appcommerceclone.data.user

import com.example.appcommerceclone.data.model.user.Address
import com.example.appcommerceclone.data.model.user.Name
import com.example.appcommerceclone.data.model.user.User
import com.example.appcommerceclone.data.model.user.UserToken

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