package com.example.expenser.presentation.dashboard

import com.example.expenser.domain.model.Category
import com.example.expenser.domain.model.Transaction
import com.example.expenser.presentation.sign_in.UserData
import com.example.expenser.util.TransactionType

data class DashboardState(
    val userData: UserData? = null,
    val expenseCategoryList: List<Category> = emptyList(),
    val incomeCategoryList: List<Category> = emptyList(),
    val transactionList: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val showSnackbar: Boolean = false,
    val isBalanceFetching: Boolean = true,
    val isCategoryFetching: Boolean = true,
    val isTransactionFetching: Boolean = true,
    val snackbarMessage: String = "",
    val incomeBalance: Double = 0.0,
    val expenseBalance: Double = 0.0,
    val categoryFetchingError: Boolean = false,
    val categoryErrorType: TransactionType = TransactionType.Income
)