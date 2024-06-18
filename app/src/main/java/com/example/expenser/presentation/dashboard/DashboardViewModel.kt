package com.example.expenser.presentation.dashboard

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.Color
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
    }


    fun onCategoryCreate(category: Category){
        viewModelScope.launch {
            repository.addCategory(category)
            getAllCategories(
                userId = dashboardState.value.userData!!.userId,
                type = if(category.type == TransactionType.Income.type) TransactionType.Income else TransactionType.Expense
            )
        }
    }

    fun getAllCategories(userId: String, type: TransactionType){
        viewModelScope.launch {
            repository.getAllCategory(userId, type).collectLatest{result->
                when(result){
                    is Resource.Error -> TODO()
                    is Resource.Loading -> {
                        _dashboardState.update {
                            it.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }
                    is Resource.Success -> {
                        _dashboardState.update {
                            it.copy(
                                categoryList = result.data ?: emptyList()
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

}