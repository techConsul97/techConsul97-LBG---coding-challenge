package com.techconsul.pokemonapp.feature_authentication.firebase.data.repository

import com.techconsul.pokemonapp.feature_authentication.firebase.data.model.AuthUser
import com.techconsul.pokemonapp.feature_authentication.firebase.domain.repository.AuthRepository
import com.techconsul.pokemonapp.feature_authentication.firebase.emailValidator
import com.techconsul.pokemonapp.feature_authentication.firebase.passwordValidator
import com.techconsul.pokemonapp.feature_pokedex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mockito.kotlin.isNull

class FakeAuthRepository : AuthRepository {
    override suspend fun createUser(auth: AuthUser): Flow<Resource<String>> = flow {
        val email = auth.email
        val password = auth.password
        when(emailValidator(email) ){
            "Email was left empty" -> emit(Resource.Error("Email is blank"))
            "This is not a valid Email Format" -> emit(Resource.Error("Not a proper format"))
            else -> emit(Resource.Success("Correct"))
        }
        when(passwordValidator(password)){
            "Password left blank" -> emit(Resource.Error("Password left blank"))
            "Password too Short" -> emit(Resource.Error("Password too Short"))
            "Password needs to contain a letter and a digit" -> emit(Resource.Error("Password needs to contain a letter and a digit"))
            else -> emit(Resource.Success("Correct"))
        }
    }

    override suspend fun loginUser(auth: AuthUser): Flow<Resource<String>> = flow {
        val email = auth.email
        val password = auth.password
        when(emailValidator(email) ){
            "Email was left empty" -> emit(Resource.Error("Email is blank"))
            "This is not a valid Email Format" -> emit(Resource.Error("Not a proper format"))
            else -> emit(Resource.Success("Correct"))
        }
        when(passwordValidator(password)){
            "Password left blank" -> emit(Resource.Error("Password left blank"))
            "Password too Short" -> emit(Resource.Error("Password too Short"))
            "Password needs to contain a letter and a digit" -> emit(Resource.Error("Password needs to contain a letter and a digit"))
            else -> emit(Resource.Success("Correct"))
        }
    }
}