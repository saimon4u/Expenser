package com.example.expenser.di

import com.example.expenser.data.repository.RepositoryImpl
import com.example.expenser.domain.repository.Repository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesFireStore(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun providesRepository(
        database: FirebaseFirestore
    ): Repository{
        return RepositoryImpl(database)
    }

}