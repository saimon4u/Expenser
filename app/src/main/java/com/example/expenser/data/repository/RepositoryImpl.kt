package com.example.expenser.data.repository

import com.example.expenser.domain.model.Transaction
import com.example.expenser.domain.repository.Repository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
class RepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore
): Repository {
    override suspend fun addTransaction(transaction: Transaction) {
        val document = database.collection("transaction").document(transaction.userId).collection("transaction").document()
        transaction.transactionId = document.id
        document.set(transaction)
    }
}