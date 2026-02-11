package com.example.modernization.model.responses

class ApiResponse<T> (
    val data: T,
    val status: Int,
    val error: String?
    )