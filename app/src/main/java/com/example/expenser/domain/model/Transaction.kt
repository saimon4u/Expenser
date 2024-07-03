package com.example.expenser.domain.model

import java.util.Date
data class Transaction(
    var transactionId: String = "",
    val createdAt: Long = 0L,
    val amount: Double = 0.0,
    val description: String? = null,
    val date: Long = 0L,
    val userId: String = "",
    val type: String = "",
    val category: String = "",
)
