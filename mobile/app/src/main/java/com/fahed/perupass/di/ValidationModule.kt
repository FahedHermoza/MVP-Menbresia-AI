package com.fahed.perupass.di

import com.fahed.perupass.data.repository.ValidationRepositoryImpl
import com.fahed.perupass.domain.repository.ValidationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ValidationModule {

    @Binds
    @Singleton
    abstract fun bindValidationRepository(impl: ValidationRepositoryImpl): ValidationRepository
}
