package com.example.expenser.util

sealed class CreateCategoryErrors{
    data class DuplicateError(val name: String): CreateCategoryErrors()
    data object ValidationError: CreateCategoryErrors()
    data object ContainNumberError: CreateCategoryErrors()
}