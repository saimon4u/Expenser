package com.example.expenser.presentation

import androidx.lifecycle.ViewModel
import com.example.expenser.domain.model.NavigationItem
import com.example.expenser.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private var _drawerState = MutableStateFlow(CustomDrawerState.Closed)
    var drawerState = _drawerState.asStateFlow()

    private var _selectedNavigationItem = MutableStateFlow(NavigationItem.Dashboard)
    var selectedNavigationItem = _selectedNavigationItem.asStateFlow()

    fun handleBackPress(){
        _drawerState.update {
            CustomDrawerState.Closed
        }
    }

    fun onNavigationItemClick(item: NavigationItem){
        _selectedNavigationItem.update { item }
    }

    fun onDrawerClick(state: CustomDrawerState){
        _drawerState.update { state }
    }

}