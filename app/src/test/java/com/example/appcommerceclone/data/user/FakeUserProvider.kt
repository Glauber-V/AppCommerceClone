package com.example.appcommerceclone.data.user

import com.example.appcommerceclone.data.user.model.User
import com.example.appcommerceclone.data.user.model.UserToken
import com.example.appcommerceclone.util.firstUser
import com.example.appcommerceclone.util.secondUser

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
}