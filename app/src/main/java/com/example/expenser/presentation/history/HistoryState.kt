package com.example.expenser.presentation.history

import com.example.expenser.domain.model.Category
import com.example.expenser.domain.model.Transaction
import com.example.expenser.domain.model.UserSettings
import com.example.expenser.util.Resource
import com.example.expenser.util.SortFilterItem
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
    val sortList: List<SortFilterItem> = emptyList(),
    val userSettings: UserSettings? = null,
    val isLoading: Boolean = false,
    val filteredList: List<Transaction> = emptyList()
)