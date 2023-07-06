package com.example.appcommerceclone.data.user

import com.example.appcommerceclone.data.model.user.User

interface UserAuthenticator {

    suspend fun getUserByUsernameAndPassword(username: String, password: String): User?

    suspend fun getUserById(userId: Int): User?
}