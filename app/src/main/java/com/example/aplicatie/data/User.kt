package com.example.aplicatie.data

data class User(
    val username: String = "",
    val passwordHash: String = "",
    val highScore: Int = 0
)
