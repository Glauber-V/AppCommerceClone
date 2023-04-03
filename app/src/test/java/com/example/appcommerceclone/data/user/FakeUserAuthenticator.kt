package com.example.appcommerceclone.data.user

import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.model.user.User
import kotlinx.coroutines.withContext

class FakeUserAuthenticator(
    private val usersProvider: UsersProvider,
    private val dispatcherProvider: DispatcherProvider
) : UserAuthenticator {

    override suspend fun getUserByUsernameAndPassword(username: String, password: String): User? {
        return withContext(dispatcherProvider.default) {
            val users = usersProvider.getAllUsers()
            val user = users.firstOrNull { it.username == username && it.password == password }
            user
        }
    }

    override suspend fun getUserById(userId: Int): User? {
        return withContext(dispatcherProvider.default) {
            val user = usersProvider.getUserById(userId)
            if (user.id == 0) null else user
        }
    }
}