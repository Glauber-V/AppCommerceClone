package com.example.appcommerceclone.data.user

import com.example.appcommerceclone.model.user.Name
import com.example.appcommerceclone.model.user.User

class FakeUserAuthenticator : UserAuthenticator {

    private val name = Name(firstname = "Orisa", lastname = "The horse")
    private val user = User(id = 1, name = name, username = "Orisa", password = "321")


    override suspend fun getUserByUsernameAndPassword(username: String, password: String): User? {
        return if (username == user.username && password == user.password) user
        else null
    }

    override suspend fun getUserById(userId: Int): User? {
        return if (userId == user.id) user else null
    }
}