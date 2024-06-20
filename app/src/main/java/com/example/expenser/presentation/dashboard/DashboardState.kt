package com.example.expenser.presentation.dashboard

import androidx.compose.ui.graphics.Color
import com.example.expenser.domain.model.Category
import com.example.expenser.presentation.sign_in.UserData

data class DashboardState(
    val userData: UserData? = null,
    val categoryList: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val showSnackbar: Boolean = false,
    val snackbarMessage: String = "",
    val isBalanceFetching: Boolean = true,
    val incomeBalance: Double = 0.0,
    val expenseBalance: Double = 0.0,
)