package com.example.expenser.domain.model

data class UserSettings(
    val userId: String = "",
    val currency: Currency = Currency("Taka", "৳")
)