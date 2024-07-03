package com.example.expenser.presentation.history

import com.example.expenser.domain.model.Category
import com.example.expenser.domain.model.Transaction
import com.example.expenser.util.TransactionType

data class HistoryState(
    val transactionList: List<Transaction> = emptyList(),
    val expenseCategoryList: List<Category> = emptyList(),
    val incomeCategoryList: List<Category> = emptyList(),
    val isTransactionFetching: Boolean = true,
    val isCategoryFetching: Boolean = true,
    val categoryFetchingError: Boolean = false,
    val categoryErrorType: TransactionType = TransactionType.Income,
    val showSnackbar: Boolean = false,
    val snackbarMessage: String = "",
)