package com.example.appcommerceclone.data.user

import com.example.appcommerceclone.data.user.model.User

interface UserAuthenticator {

    suspend fun getUserByUsernameAndPassword(username: String, password: String): User?

    suspend fun getUserById(userId: Int): User?
}