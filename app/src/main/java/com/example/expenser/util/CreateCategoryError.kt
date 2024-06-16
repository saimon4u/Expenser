package com.example.expenser.util

sealed class CreateCategoryError{
    data class DuplicateError(val name: String): CreateCategoryError()
    data object ValidationError: CreateCategoryError()
    data object ContainNumberError: CreateCategoryError()
}