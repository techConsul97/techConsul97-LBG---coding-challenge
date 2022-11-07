package com.techconsul.pokemonapp.feature_authentication.firebase.domain.repository

import com.techconsul.pokemonapp.feature_authentication.firebase.data.model.AuthUser
import com.techconsul.pokemonapp.feature_pokedex.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun createUser(auth: AuthUser): Flow<Resource<String>>

    suspend fun loginUser(auth: AuthUser): Flow<Resource<String>>


}