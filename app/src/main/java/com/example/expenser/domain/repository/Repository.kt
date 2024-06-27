package com.example.expenser.domain.repository

import com.example.expenser.domain.model.Category
import com.example.expenser.domain.model.Transaction
import com.example.expenser.util.Resource
import com.example.expenser.util.TransactionType
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun addTransaction(transaction: Transaction)
    suspend fun addCategory(category: Category)
    suspend fun getCategoryListByType(userId: String, type: TransactionType): Flow<Resource<List<Category>>>
//    suspend fun getAllCategories(userId: String): Flow<Resource<List<Category>>>
    suspend fun getAllTransaction(userId: String): Flow<Resource<List<Transaction>>>
    suspend fun getBalance(userId: String, balanceType: String): Flow<Resource<Double>>
}