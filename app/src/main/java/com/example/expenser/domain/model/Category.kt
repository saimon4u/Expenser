package com.example.expenser.domain.model

data class Category(
    val createdAt: Long,
    val name: String,
    val userId: String,
    val icon: String,
    val type: String,
)

val categoryList = mutableListOf(
    Category(
        createdAt = System.currentTimeMillis(),
        name = "Saimon",
        userId = "Saimon",
        icon = "a",
        type = TransactionType.Income.type
    ),
    Category(
        createdAt = System.currentTimeMillis(),
        name = "Saimon1",
        userId = "Saimon",
        icon = "a",
        type = TransactionType.Income.type
    ),
    Category(
        createdAt = System.currentTimeMillis(),
        name = "Saimon2",
        userId = "Saimon",
        icon = "a",
        type = TransactionType.Income.type
    ),
    Category(
        createdAt = System.currentTimeMillis(),
        name = "Saimon3",
        userId = "Saimon",
        icon = "a",
        type = TransactionType.Income.type
    ),
    Category(
        createdAt = System.currentTimeMillis(),
        name = "Saimon4",
        userId = "Saimon",
        icon = "a",
        type = TransactionType.Income.type
    ),
)