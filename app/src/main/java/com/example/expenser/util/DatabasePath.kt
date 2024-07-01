package com.example.expenser.util

enum class DatabasePath(
    val path: String,
) {
    Category(
        path = "category"
    ),
    Transaction(
        path = "transaction"
    ),
    UserSettings(
        path = "user_settings"
    )
}