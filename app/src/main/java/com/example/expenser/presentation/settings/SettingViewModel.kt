package com.example.expenser.presentation.settings

import androidx.lifecycle.ViewModel
import com.example.expenser.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class SettingViewModel @Inject constructor(
    private val repository: Repository
): ViewModel(){


    fun getUserSettings(){

    }

}