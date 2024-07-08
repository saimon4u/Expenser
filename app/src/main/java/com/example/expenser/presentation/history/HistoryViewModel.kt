package com.example.expenser.presentation.history

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expenser.domain.model.Category
import com.example.expenser.domain.model.Transaction
import com.example.expenser.domain.repository.Repository
import com.example.expenser.presentation.sign_in.GoogleAuthClient
import com.example.expenser.util.Resource
import com.example.expenser.util.SortFilterItem
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

    fun updateSortList(list: List<SortFilterItem>, categories: List<Category>){
        _historyState.update { state ->
            state.copy(
                sortList = categories.map {
                    SortFilterItem(
                        name = it.name,
                        icon = it.categoryIcon,
                        type = "category",
                        isSelected = historyState.value.sortList.find { item -> item.name == it.name }?.isSelected ?: false
                    )
                } + listOf(TransactionType.Income, TransactionType.Expense).map {type ->
                    SortFilterItem(
                        name = type.type,
                        type = "transaction_type",
                        isSelected = historyState.value.sortList.find { item-> item.name == type.type }?.isSelected ?: false
                    )
                }
            )
        }
    }

    fun onSortItemClick(item: SortFilterItem){
        _historyState.update {
            it.copy(
                sortList = it.sortList.map {sortFilterItem ->
                    if(sortFilterItem.name == item.name) item
                    else sortFilterItem
                }
            )
        }
    }

    fun onClearSelection(type: String){
        _historyState.update {
            it.copy(
                sortList = it.sortList.map {item->
                    if(item.type == type){
                        item.copy(
                            isSelected = false
                        )
                    }else item
                }
            )
        }
    }

    fun getUserSettings(userId: String){
        viewModelScope.launch {
            repository.getUserSettings(userId).collectLatest {result->
                when(result){
                    is Resource.Error -> {
                        debug("Error Fetching User Settings")
                    }
                    is Resource.Loading -> {
                        _historyState.update {
                            it.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }
                    is Resource.Success -> {
                        _historyState.update {
                            it.copy(
                                userSettings = result.data
                            )
                        }
                    }
                }
            }
        }
    }

    fun onTransactionDelete(userId: String, transaction: Transaction){
        viewModelScope.launch {
            repository.deleteTransaction(userId, transaction)
            _historyState.update {
                it.copy(
                    transactionList = it.transactionList.filter { item-> transaction.transactionId != item.transactionId }
                )
            }
            showSnackbar("Transaction Deleted")
        }
    }

    fun showSnackbar(message: String){
        _historyState.update {
            it.copy(
                showSnackbar = true,
                snackbarMessage = message
            )
        }
    }

    fun updateSnackbarState(){
        _historyState.update {
            it.copy(
                showSnackbar = false,
            )
        }
    }

    fun filterList(selectedTypes: List<SortFilterItem>, selectedCategory: List<SortFilterItem>){
        _historyState.update {
            if(selectedTypes.isEmpty() && selectedCategory.isEmpty()){
                it.copy(
                    filteredList = historyState.value.transactionList
                )
            }else if(selectedCategory.isEmpty()){
                it.copy(
                    filteredList = historyState.value.transactionList.filter {transaction->
                        selectedTypes.map { sort -> sort.name }.contains(transaction.type)
                    },
                )
            }else if(selectedTypes.isEmpty()){
                it.copy(
                    filteredList = historyState.value.transactionList.filter {transaction->
                                selectedCategory.map { sort -> sort.name }.contains(transaction.category)
                    },
                )
            }else{
                it.copy(
                    filteredList = historyState.value.transactionList.filter {transaction->
                        selectedTypes.map { sort -> sort.name }.contains(transaction.type) &&
                                selectedCategory.map { sort -> sort.name }.contains(transaction.category)
                    },
                )
            }
        }
    }
}