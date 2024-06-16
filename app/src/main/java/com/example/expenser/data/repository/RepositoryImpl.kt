package com.example.expenser.data.repository

import com.example.expenser.domain.model.Category
import com.example.expenser.domain.model.Transaction
import com.example.expenser.domain.repository.Repository
import com.example.expenser.util.DatabasePath
import com.example.expenser.util.Resource
import com.example.expenser.util.TransactionType
import com.example.expenser.util.debug
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
class RepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore
): Repository {
    override suspend fun addTransaction(transaction: Transaction) {
        val document = database.collection(DatabasePath.Transaction.path).document(transaction.userId).collection(transaction.type).document()
        transaction.transactionId = document.id
        document.set(transaction)
    }

    override suspend fun addCategory(category: Category) {
        val document = database.collection(DatabasePath.Category.path).document(category.userId).collection(category.type).document()
        category.categoryId = document.id
        document.set(category)
    }

    override suspend fun getAllCategory(userId: String, type: TransactionType): Flow<Resource<List<Category>>> {
        return flow {

            val categoryList = mutableListOf<Category>()
            var result = false
            emit(Resource.Loading(true))

            database.collection(DatabasePath.Category.path)
                .document(userId)
                .collection(type.type)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        val category = document.toObject(Category::class.java)
                        categoryList.add(category)
                    }
                    result = true
                }
                .addOnFailureListener {
                    result = false
                }
                .await()
            emit(Resource.Loading(false))
            if (result) emit(Resource.Success(categoryList))
            else emit(Resource.Error(message = "Error Fetching data"))
        }
    }
}