package com.techconsul.pokemonapp.feature_authentication.firebase.di

import com.google.firebase.auth.FirebaseAuth
import com.techconsul.pokemonapp.feature_authentication.firebase.data.repository.AuthRepositoryImpl
import com.techconsul.pokemonapp.feature_authentication.firebase.domain.repository.AuthRepository
import com.techconsul.pokemonapp.feature_authentication.firebase.domain.use_case.AuthorizeUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun providesAuthRepository(
        firebaseAuth: FirebaseAuth
    ): AuthRepository = AuthRepositoryImpl(firebaseAuth)

    @Provides
    fun provideAuthUseCase(repository: AuthRepository): AuthorizeUserUseCase = AuthorizeUserUseCase(repository)




}