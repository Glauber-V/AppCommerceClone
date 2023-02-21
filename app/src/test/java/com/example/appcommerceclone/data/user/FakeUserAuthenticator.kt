package com.example.appcommerceclone.data.user

import com.example.appcommerceclone.model.user.Address
import com.example.appcommerceclone.model.user.Name
import com.example.appcommerceclone.model.user.User

class FakeUserAuthenticator : UserAuthenticator {

    private val users = listOf(firstUser, secondUser)


    override suspend fun getUserByUsernameAndPassword(username: String, password: String): User? {
        return users.firstOrNull { it.username == username && it.password == password }
    }

    override suspend fun getUserById(userId: Int): User? {
        return users.firstOrNull { it.id == userId }
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