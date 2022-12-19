package com.example.appcommerceclone.data.user

import com.example.appcommerceclone.model.user.Name
import com.example.appcommerceclone.model.user.User

class FakeUserAuthenticator : UserAuthenticator {

    companion object {
        const val ID = 3
        const val FIRSTNAME = "Orisa"
        const val LASTNAME = "Horse"
        const val USERNAME = "Terra_Surge"
        const val PASSWORD = "041022"
    }

    private val name = Name(firstname = FIRSTNAME, lastname = LASTNAME)
    private val user = User(id = ID, name = name, username = USERNAME, password = PASSWORD)


    override suspend fun getUserByUsernameAndPassword(username: String, password: String): User? {
        return if (username == user.username && password == user.password) user
        else null
    }

    override suspend fun getUserById(userId: Int): User? {
        return if (userId == user.id) user else null
    }
}