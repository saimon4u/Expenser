package com.example.expenser.domain.model

data class Category(
    val createdAt: Long = System.currentTimeMillis(),
    val name: String = "",
    val userId: String = "",
    val type: String = "",
    var categoryId: String = "",
    val categoryIcon: String = "",
)