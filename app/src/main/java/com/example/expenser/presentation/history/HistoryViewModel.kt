package com.example.expenser.presentation.history

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expenser.domain.repository.Repository
import com.example.expenser.presentation.sign_in.GoogleAuthClient
import com.example.expenser.util.Resource
import com.example.expenser.util.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: Repository,
): ViewModel() {

    private val _historyState = MutableStateFlow(HistoryState())
    val historyState = _historyState.asStateFlow()

    fun getAllTransaction(userId: String, startDate: Long, endDate: Long){
        viewModelScope.launch {
            repository.getAllTransaction(userId, startDate, endDate).collectLatest { result->
                when(result){
                    is Resource.Error -> {
                        _historyState.update {
                            it.copy(
                                showSnackbar = true,
                                snackbarMessage = "Error Fetching transactions..."
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _historyState.update {
                            it.copy(
                                isTransactionFetching = result.isLoading
                            )
                        }
                    }
                    is Resource.Success -> {
                        _historyState.update {
                            it.copy(
                                transactionList = result.data ?: emptyList(),
                            )
                        }
                    }
                }
            }
        }
    }

    fun getCategoriesByType(userId: String, type: TransactionType){
        viewModelScope.launch {
            repository.getCategoryListByType(userId, type).collectLatest{ result->
                when(result){
                    is Resource.Error -> {
                        _historyState.update {
                            it.copy(
                                showSnackbar = true,
                                snackbarMessage = "Error Fetching Categories...",
                                categoryFetchingError = true,
                                categoryErrorType = type
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _historyState.update {
                            it.copy(
                                isCategoryFetching = result.isLoading
                            )
                        }
                    }
                    is Resource.Success -> {
                        _historyState.update {
                            if(type == TransactionType.Expense)
                                it.copy(
                                    expenseCategoryList = result.data ?: emptyList(),
                                    categoryFetchingError = type != historyState.value.categoryErrorType
                                )
                            else
                                it.copy(
                                    incomeCategoryList = result.data ?: emptyList(),
                                    categoryFetchingError = type != historyState.value.categoryErrorType
                                )
                        }
                    }
                }
            }
        }
    }

}