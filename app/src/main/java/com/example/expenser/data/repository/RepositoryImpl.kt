package com.example.expenser.data.repository

import com.example.expenser.domain.model.Category
import com.example.expenser.domain.model.Transaction
import com.example.expenser.domain.repository.Repository
import com.example.expenser.util.DatabasePath
import com.example.expenser.util.Resource
import com.example.expenser.util.TransactionType
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

    override suspend fun getAllTransaction(userId: String): Flow<Resource<List<Transaction>>> {
        return flow {

            val transactionList = mutableListOf<Transaction>()
            var result = true
            emit(Resource.Loading(true))

            database.collection(DatabasePath.Transaction.path)
                .document(userId)
                .collection(TransactionType.Income.type)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        val category = document.toObject(Transaction::class.java)
                        transactionList.add(category)
                    }
                }
                .addOnFailureListener {
                    result = false
                }
                .await()
            database.collection(DatabasePath.Transaction.path)
                .document(userId)
                .collection(TransactionType.Expense.type)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        val category = document.toObject(Transaction::class.java)
                        transactionList.add(category)
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

    override suspend fun getBalance(userId: String, balanceType: String): Flow<Resource<Double>> {
        return flow {

            var balance = 0.0
            var result = true
            emit(Resource.Loading(true))

            database.collection(DatabasePath.Transaction.path)
                .document(userId)
                .collection(balanceType)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        balance += document.get("amount") as Double
                    }
                }
                .addOnFailureListener {
                    result = false
                }
                .await()

            emit(Resource.Loading(false))
            if(result) emit(Resource.Success(data = balance))
            else emit(Resource.Error("Error getting balance"))
        }
    }

    private suspend fun getCategoriesWithoutType(userId: String): MutableList<Category>{
        val categoryList = mutableListOf<Category>()
        database.collection(DatabasePath.Category.path)
            .document(userId)
            .collection(TransactionType.Income.type)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val category = document.toObject(Category::class.java)
                    categoryList.add(category)
                }
            }
            .await()

        database.collection(DatabasePath.Category.path)
            .document(userId)
            .collection(TransactionType.Expense.type)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val category = document.toObject(Category::class.java)
                    categoryList.add(category)
                }
            }
            .await()

        return categoryList
    }

//    override suspend fun getAllCategories(
//        userId: String
//    ): Flow<Resource<List<Category>>> {
//        return flow {
//            emit(Resource.Loading(true))
//            val categoryList = mutableListOf<Category>()
//            var result = true
//            database.collection(DatabasePath.Category.path)
//                .document(userId)
//                .collection(TransactionType.Expense.type)
//                .orderBy("createdAt", Query.Direction.DESCENDING)
//                .get()
//                .addOnSuccessListener {
//                    for (document in it) {
//                        val category = document.toObject(Category::class.java)
//                        categoryList.add(category)
//                    }
//                    result = true
//                }
//                .addOnFailureListener {
//                    result = false
//                }
//                .await()
//
//            database.collection(DatabasePath.Category.path)
//                .document(userId)
//                .collection(TransactionType.Income.type)
//                .orderBy("createdAt", Query.Direction.DESCENDING)
//                .get()
//                .addOnSuccessListener {
//                    for (document in it) {
//                        val category = document.toObject(Category::class.java)
//                        categoryList.add(category)
//                    }
//                    result = true
//                }
//                .addOnFailureListener {
//                    result = false
//                }
//                .await()
//
//            emit(Resource.Loading(false))
//            if (result) emit(Resource.Success(categoryList))
//            else emit(Resource.Error(message = "Error Fetching data"))
//        }
//    }
}