package com.example.expenser.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expenser.domain.model.Category
import com.example.expenser.domain.model.Currency
import com.example.expenser.domain.repository.Repository
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
class SettingViewModel @Inject constructor(
    private val repository: Repository
): ViewModel(){

    private val _settingState = MutableStateFlow(SettingState())
    val settingState = _settingState.asStateFlow()

    fun getUserSettings(userId: String){
        viewModelScope.launch {
            repository.getUserSettings(userId).collectLatest {result->
                when(result){
                    is Resource.Error -> {
                        _settingState.update {
                            it.copy(
                                errorMessage = result.message.toString()
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _settingState.update {
                            it.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }
                    is Resource.Success -> {
                        _settingState.update {
                            it.copy(
                                userSettings = result.data
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
                        _settingState.update {
                            it.copy(
                                errorMessage = result.message.toString()
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _settingState.update {
                            it.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }
                    is Resource.Success -> {
                        _settingState.update {
                            if(type == TransactionType.Expense)
                                it.copy(
                                    expenseCategoryList = result.data ?: emptyList(),
                                )
                            else
                                it.copy(
                                    incomeCategoryList = result.data ?: emptyList(),
                                )
                        }
                    }
                }
            }
        }
    }

    fun onCategoryCreate(category: Category, userId: String){
        viewModelScope.launch {
            repository.addCategory(category)
            getCategoriesByType(
                userId = userId,
                type = if(category.type == TransactionType.Income.type) TransactionType.Income else TransactionType.Expense
            )
        }
    }

    fun onCategoryDelete(category: Category){
        viewModelScope.launch {
            repository.deleteCategory(category.userId, category)
            getCategoriesByType(category.userId, if(category.type == TransactionType.Income.type) TransactionType.Income else TransactionType.Expense)
            showSnackbar("${category.name} category deleted!")
        }
    }

    fun showSnackbar(message: String){
        _settingState.update {
            it.copy(
                showSnackbar = true,
                errorMessage = message
            )
        }
    }

    fun updateSnackbarState(){
        _settingState.update {
            it.copy(
                showSnackbar = false,
            )
        }
    }

    fun updateUserSettings(userId: String, currency: Currency){
        viewModelScope.launch {
            repository.updateUserSettings(userId, currency)
        }
    }

}