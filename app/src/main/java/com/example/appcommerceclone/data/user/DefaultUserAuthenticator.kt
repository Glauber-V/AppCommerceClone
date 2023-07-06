package com.example.appcommerceclone.data.user

import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.model.user.User
import kotlinx.coroutines.withContext

class DefaultUserAuthenticator(
    private val usersProvider: UsersProvider,
    private val dispatcherProvider: DispatcherProvider
) : UserAuthenticator {

    override suspend fun getUserByUsernameAndPassword(username: String, password: String): User? {
        return runCatching {
            withContext(dispatcherProvider.default) {
                val users = usersProvider.getAllUsers()
                val user = users.firstOrNull { it.username == username && it.password == password }
                user
            }
        }.getOrNull()
    }

    override suspend fun getUserById(userId: Int): User? {
        return runCatching {
            withContext(dispatcherProvider.default) {
                usersProvider.getUserById(userId)
            }
        }.getOrNull()
    }
}