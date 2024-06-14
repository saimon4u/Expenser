package com.example.expenser.domain.repository

import com.example.expenser.domain.model.Transaction

interface Repository {
    suspend fun addTransaction(transaction: Transaction)
}