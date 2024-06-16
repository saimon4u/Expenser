package com.example.expenser.presentation.dashboard

import com.example.expenser.domain.model.Category
import com.example.expenser.presentation.sign_in.UserData

data class DashboardState(
    val userData: UserData? = null,
    val categoryList: List<Category> = emptyList(),
)