package com.example.appcommerceclone.data.user

import com.example.appcommerceclone.data.model.user.User
import com.example.appcommerceclone.data.model.user.UserToken
import retrofit2.http.*

interface UsersProvider {

    @GET("users")
    suspend fun getAllUsers(): List<User>

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun verifyUserExist(
        @Field("username") username: String,
        @Field("password") password: String
    ): UserToken

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") id: Int
    ): User
}

