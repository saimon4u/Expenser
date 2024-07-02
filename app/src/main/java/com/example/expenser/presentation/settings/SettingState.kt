package com.example.expenser.presentation.settings

import com.example.expenser.domain.model.Category
import com.example.expenser.domain.model.UserSettings

data class SettingState(
    val userSettings: UserSettings? = null,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val expenseCategoryList: List<Category> = emptyList(),
    val incomeCategoryList: List<Category> = emptyList(),
    val showSnackbar: Boolean = false,
)