package com.example.expenser.domain.model

import java.util.Date
data class Transaction(
    var transactionId: String = "",
    val createdAt: Long = 1232424,
    val amount: Double = 0.0,
    val description: String? = null,
    val date: Date = Date(),
    val userId: String = "",
    val type: String = "",
    val category: String = "",
)
