package com.example.appcommerceclone.util

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.NavigationGraphDirections
import com.example.appcommerceclone.data.user.model.Address
import com.example.appcommerceclone.data.user.model.Name
import com.example.appcommerceclone.data.user.model.User

fun Fragment.verifyUserToProceed(user: User?, onUserVerified: (verifiedUser: User) -> Unit) {
    if (user != null) onUserVerified(user)
    else findNavController().navigate(
        NavigationGraphDirections.actionGlobalLoginFragment()
    )
}

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