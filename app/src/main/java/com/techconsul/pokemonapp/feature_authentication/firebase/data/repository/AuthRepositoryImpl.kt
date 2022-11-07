package com.techconsul.pokemonapp.feature_authentication.firebase.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.techconsul.pokemonapp.feature_authentication.firebase.data.model.AuthUser
import com.techconsul.pokemonapp.feature_authentication.firebase.domain.repository.AuthRepository
import com.techconsul.pokemonapp.feature_pokedex.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,

    ) : AuthRepository {


    override suspend fun createUser(auth: AuthUser): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading())

        firebaseAuth.createUserWithEmailAndPassword(auth.email!!, auth.password!!)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(Resource.Success("User Created Successfully"))
                }
            }.addOnFailureListener {
            trySend(Resource.Error(it.localizedMessage?:"Register Failed.. Try again later"))
        }
        awaitClose { close() }

    }


    override suspend fun loginUser(auth: AuthUser): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading())
        firebaseAuth.signInWithEmailAndPassword(auth.email!!, auth.password!!)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    trySend(Resource.Success("Login successful"))
            }.addOnFailureListener {
                trySend(Resource.Error(it.localizedMessage?:"Login Failed..Check your credentials!"))
            }
        awaitClose {
            close()
        }

    }

}