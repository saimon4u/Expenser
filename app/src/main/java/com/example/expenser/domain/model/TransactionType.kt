package com.example.expenser.domain.model

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