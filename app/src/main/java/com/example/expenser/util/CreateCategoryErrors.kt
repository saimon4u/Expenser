package com.example.expenser.util

sealed class CreateCategoryErrors{
    data class DuplicateError(val name: String): CreateCategoryErrors()
    data object ContainNumberError: CreateCategoryErrors()
    data object BlackNameError: CreateCategoryErrors()
    data object LongNameError: CreateCategoryErrors()
    data object InternetError: CreateCategoryErrors()
}