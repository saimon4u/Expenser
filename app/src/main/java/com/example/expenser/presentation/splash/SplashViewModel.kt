package com.example.expenser.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expenser.domain.model.Currency
import com.example.expenser.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun updateUserSettings(userId: String, currency: Currency){
        viewModelScope.launch {
            repository.updateUserSettings(userId, currency)
        }
    }

}