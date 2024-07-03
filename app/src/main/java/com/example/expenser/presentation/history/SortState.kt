package com.example.expenser.presentation.history

import com.example.expenser.util.SortFilterItem

data class SortState(
    val sortFilterItems: List<SortFilterItem> = emptyList()
)