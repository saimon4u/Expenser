package com.example.expenser.data.repository

import com.example.expenser.domain.model.Category
import com.example.expenser.domain.model.Currency
import com.example.expenser.domain.model.Transaction
import com.example.expenser.domain.model.UserSettings
import com.example.expenser.domain.repository.Repository
import com.example.expenser.util.DatabasePath
import com.example.expenser.util.Resource
import com.example.expenser.util.TransactionType
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
class RepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore
): Repository {
    override suspend fun addTransaction(transaction: Transaction) {
        val document = database.collection(DatabasePath.Transaction.path).document(transaction.userId).collection(transaction.type).document()
        transaction.transactionId = document.id
        document.set(transaction).await()
    }

    override suspend fun addCategory(category: Category) {
        val document = database.collection(DatabasePath.Category.path).document(category.userId).collection(category.type).document()
        category.categoryId = document.id
        document.set(category).await()
    }

    override suspend fun getCategoryListByType(userId: String, type: TransactionType): Flow<Resource<List<Category>>> {
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

    override suspend fun getAllTransaction(userId: String, startDate: Long, endDate: Long): Flow<Resource<List<Transaction>>> {
        return flow {

            val transactionList = mutableListOf<Transaction>()
            var result = true
            emit(Resource.Loading(true))

            database.collection(DatabasePath.Transaction.path)
                .document(userId)
                .collection(TransactionType.Income.type)
                .whereGreaterThanOrEqualTo("date", startDate)
                .whereLessThanOrEqualTo("date", endDate + 500L)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        val transaction = document.toObject(Transaction::class.java)
                        transactionList.add(transaction)
                    }
                }
                .addOnFailureListener {
                    result = false
                }
                .await()
            database.collection(DatabasePath.Transaction.path)
                .document(userId)
                .collection(TransactionType.Expense.type)
                .whereGreaterThanOrEqualTo("date", startDate)
                .whereLessThanOrEqualTo("date", endDate + 500L)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        val transaction = document.toObject(Transaction::class.java)
                        transactionList.add(transaction)
                    }
                }
                .addOnFailureListener {
                    result = false
                }
                .await()

            emit(Resource.Loading(false))
            if(result){
                emit(Resource.Success(data = transactionList))
            }else{
                emit(Resource.Error("Error fetching transaction list"))
            }
        }
    }

    override suspend fun updateUserSettings(userId: String, currency: Currency) {
        val document = database.collection(DatabasePath.UserSettings.path).document(userId)
        val settings = UserSettings(userId, currency)
        document.set(settings).await()
    }

    override suspend fun getUserSettings(userId: String): Flow<Resource<UserSettings>> {
        return flow {
            emit(Resource.Loading(true))

            var result = true
            var settings: UserSettings? = null

            database.collection(DatabasePath.UserSettings.path)
                .document(userId)
                .get()
                .addOnSuccessListener{
                    settings = it.toObject(UserSettings::class.java)
                    result = true
                }
                .addOnFailureListener{
                    result = false
                }
                .await()

            emit(Resource.Loading(false))
            if(result){
                emit(Resource.Success(settings))
            }else{
                emit(Resource.Error("Error fetching user settings"))
            }
        }
    }

    override suspend fun deleteCategory(userId: String, category: Category) {
        database.collection(DatabasePath.Category.path).document(userId).collection(category.type).document(category.categoryId)
            .delete()
            .await()
    }

}