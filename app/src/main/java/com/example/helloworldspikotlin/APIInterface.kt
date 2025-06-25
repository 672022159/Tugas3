package com.example.helloworldspikotlin

import retrofit2.http.GET

interface APIInterface {
    @GET("users")
    suspend fun getUsers(): UserResponse
}