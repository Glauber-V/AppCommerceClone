package com.example.appcommerceclone.data.user

import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.model.user.User
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultUserAuthenticator @Inject constructor(
    private val usersProvider: UsersProvider,
    private val dispatcher: DispatcherProvider
) : UserAuthenticator {

    override suspend fun getUserByUsernameAndPassword(username: String, password: String): User? {
        return runCatching {
            withContext(dispatcher.io) {
                val users = usersProvider.getAllUsers()
                val user = users.firstOrNull { it.username == username && it.password == password }
                user
            }
        }.getOrNull()
    }

    override suspend fun getUserById(userId: Int): User? {
        return runCatching {
            withContext(dispatcher.io) {
                usersProvider.getUserById(userId)
            }
        }.getOrNull()
    }
}