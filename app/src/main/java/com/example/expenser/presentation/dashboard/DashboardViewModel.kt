package com.example.expenser.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expenser.domain.model.Category
import com.example.expenser.domain.model.Transaction
import com.example.expenser.domain.repository.Repository
import com.example.expenser.util.Resource
import com.example.expenser.presentation.sign_in.GoogleAuthClient
import com.example.expenser.util.TransactionType
import com.example.expenser.util.debug
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: Repository,
    googleAuthClient: GoogleAuthClient?
): ViewModel() {

    private val _dashboardState = MutableStateFlow(DashboardState())
    val dashboardState = _dashboardState.asStateFlow()

    init {
        val user = googleAuthClient?.getSignedInUser()
        user?.run {
            _dashboardState.update {
                it.copy(
                    userData = this
                )
            }
        }
//        getBalance()
//        getAllTransaction(user!!.userId)
//        getCategoriesByType(user.userId, TransactionType.Income)
//        getCategoriesByType(user.userId, TransactionType.Expense)
    }


    fun onCategoryCreate(category: Category){
        viewModelScope.launch {
            repository.addCategory(category)
            getCategoriesByType(
                userId = dashboardState.value.userData!!.userId,
                type = if(category.type == TransactionType.Income.type) TransactionType.Income else TransactionType.Expense
            )
        }
    }

    fun getAllTransaction(userId: String){
        viewModelScope.launch {
            repository.getAllTransaction(userId).collectLatest { result->
                when(result){
                    is Resource.Error -> {
                        debug(result.message.toString())
                        _dashboardState.update {
                            it.copy(
                                showSnackbar = true,
                                snackbarMessage = "Error Fetching transactions..."
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _dashboardState.update {
                            it.copy(
                                isTransactionFetching = result.isLoading
                            )
                        }
                    }
                    is Resource.Success -> {
                        _dashboardState.update {
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
                        debug(result.message.toString())
                        _dashboardState.update {
                            it.copy(
                                showSnackbar = true,
                                snackbarMessage = "Error Fetching Categories...",
                                categoryFetchingError = true,
                                categoryErrorType = type
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _dashboardState.update {
                            it.copy(
                                isCategoryFetching = result.isLoading
                            )
                        }
                    }
                    is Resource.Success -> {
                        _dashboardState.update {
                            if(type == TransactionType.Expense)
                                it.copy(
                                    expenseCategoryList = result.data ?: emptyList(),
                                    categoryFetchingError = type != dashboardState.value.categoryErrorType
                                )
                            else
                                it.copy(
                                    incomeCategoryList = result.data ?: emptyList(),
                                    categoryFetchingError = type != dashboardState.value.categoryErrorType
                                )
                        }
                    }
                }
            }
        }
    }

    fun onTransactionCreate(transaction: Transaction){
        viewModelScope.launch {
            repository.addTransaction(transaction)
            getAllTransaction(dashboardState.value.userData!!.userId)
            getBalance()
        }
    }


    fun showSnackbar(message: String){
        _dashboardState.update {
            it.copy(
                showSnackbar = true,
                snackbarMessage = message
            )
        }
    }

    fun updateSnackbarState(){
        _dashboardState.update {
            it.copy(
                showSnackbar = false,
            )
        }
    }

    fun getBalance(){
        viewModelScope.launch {
            repository.getBalance(_dashboardState.value.userData!!.userId, TransactionType.Income.type).collectLatest {result->
                when(result){
                    is Resource.Error -> TODO()
                    is Resource.Loading -> {
                        _dashboardState.update {
                            it.copy(
                                isBalanceFetching = result.isLoading
                            )
                        }
                    }
                    is Resource.Success -> {
                        _dashboardState.update {
                            it.copy(
                                incomeBalance = result.data ?: 0.0
                            )
                        }
                    }
                }
            }

            repository.getBalance(_dashboardState.value.userData!!.userId, TransactionType.Expense.type).collectLatest {result->
                when(result){
                    is Resource.Error -> TODO()
                    is Resource.Loading -> {
                        _dashboardState.update {
                            it.copy(
                                isBalanceFetching = result.isLoading
                            )
                        }
                    }
                    is Resource.Success -> {
                        _dashboardState.update {
                            it.copy(
                                expenseBalance = result.data ?: 0.0
                            )
                        }
                    }
                }
            }
        }
    }
}