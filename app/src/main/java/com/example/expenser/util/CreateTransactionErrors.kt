package com.example.expenser.util

sealed class CreateTransactionErrors {
    data object AmountError: CreateTransactionErrors()
    data object CategorySelectError: CreateTransactionErrors()
    data object DateError: CreateTransactionErrors()
    data object InternetError: CreateTransactionErrors()
}