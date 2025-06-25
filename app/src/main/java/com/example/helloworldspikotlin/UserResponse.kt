package com.example.helloworldspikotlin

import com.google.gson.annotations.SerializedName

data class UserResponse(

    @SerializedName("users")
    val users: List<User>
)