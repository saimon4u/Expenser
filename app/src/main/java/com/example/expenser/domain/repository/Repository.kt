package com.example.expenser.domain.repository

import com.example.expenser.domain.model.Category
import com.example.expenser.domain.model.Currency
import com.example.expenser.domain.model.Transaction
import com.example.expenser.domain.model.UserSettings
import com.example.expenser.util.Resource
import com.example.expenser.util.TransactionType
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun addTransaction(transaction: Transaction)
    suspend fun addCategory(category: Category)
    suspend fun getCategoryListByType(userId: String, type: TransactionType): Flow<Resource<List<Category>>>
    suspend fun getAllTransaction(userId: String): Flow<Resource<List<Transaction>>>
    suspend fun getBalance(userId: String, balanceType: String): Flow<Resource<Double>>
    suspend fun updateUserSettings(userId: String, currency: Currency)
    suspend fun getUserSettings(userId: String): Flow<Resource<UserSettings>>
    suspend fun deleteCategory(userId: String, category: Category)
}