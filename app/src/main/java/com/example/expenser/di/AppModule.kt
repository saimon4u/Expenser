package com.example.expenser.di

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.example.expenser.App
import com.example.expenser.data.repository.RepositoryImpl
import com.example.expenser.domain.repository.Repository
import com.example.expenser.presentation.sign_in.GoogleAuthClient
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
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

    @Provides
    @Singleton
    fun providesAuthClient(): GoogleAuthClient?{
        return App.getContext()?.let {
            GoogleAuthClient(
                context = it,
                oneTapClient = Identity.getSignInClient(it)
            )
        }
    }

}