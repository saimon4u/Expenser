package com.example.expenser.util

enum class TransactionType(
    val type: String,
) {
    Income(
        type = "income"
    ),
    Expense(
        type = "expense"
    )
}