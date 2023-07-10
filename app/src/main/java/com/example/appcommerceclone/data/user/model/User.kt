package com.example.appcommerceclone.data.user.model

import com.squareup.moshi.Json

data class User(
    @Json(name = "id") val id: Int = 0,
    @Json(name = "name") var name: Name = Name(),
    @Json(name = "username") var username: String = "",
    @Json(name = "password") var password: String = "",
    @Json(name = "email") var email: String = "",
    @Json(name = "phone") var phone: String = "",
    @Json(name = "address") var address: Address = Address()
)