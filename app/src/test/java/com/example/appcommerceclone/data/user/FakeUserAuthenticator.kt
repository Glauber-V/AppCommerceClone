package com.example.appcommerceclone.data.user

import com.example.appcommerceclone.data.user.model.User

class FakeUserAuthenticator(private val usersProvider: UsersProvider) : UserAuthenticator {

    override suspend fun getUserByUsernameAndPassword(username: String, password: String): User? {
        return usersProvider.getAllUsers().firstOrNull {
            it.username == username && it.password == password
        }
    }

    override suspend fun getUserById(userId: Int): User? {
        return usersProvider.getUserById(userId)
    }
}