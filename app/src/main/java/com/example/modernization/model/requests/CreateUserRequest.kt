package com.example.modernization.model.requests
import com.example.modernization.model.types.UserCredential


class CreateUserRequest (
    val userName: String,
    val credential: UserCredential
)