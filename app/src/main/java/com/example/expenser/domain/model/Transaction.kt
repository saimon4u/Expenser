package com.example.expenser.domain.model

import java.util.Date
data class Transaction(
    var transactionId: String,
    val createdAt: Long,
    val amount: Double,
    val description: String? = null,
    val date: Date,
    val userId: String,
    val type: String,
    val category: String,
    val categoryIcon: String
)
