package com.example.aplicatie.util

import java.io.Serializable

data class ReviewQuestion(
    val text: String,
    val options: ArrayList<String>,
    val correctAnswerIndex: Int
) : Serializable
