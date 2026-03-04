package com.fahed.perupass.di

import com.fahed.perupass.data.repository.AuthRepositoryImpl
import com.fahed.perupass.data.source.remote.WebClientId
import com.fahed.perupass.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    companion object {

        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

        @Provides
        @Singleton
        @WebClientId
        // Web OAuth client ID (client_type: 3) from google-services.json
        fun provideWebClientId(): String =
            "495440823897-i58mk73v9j7p36mpjk26pb1oibh0m4nu.apps.googleusercontent.com"
    }
}
