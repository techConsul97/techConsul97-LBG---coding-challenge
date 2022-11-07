package com.techconsul.pokemonapp.feature_authentication.firebase.domain.use_case

import com.techconsul.pokemonapp.feature_authentication.firebase.data.model.AuthUser
import com.techconsul.pokemonapp.feature_authentication.firebase.domain.repository.AuthRepository
import com.techconsul.pokemonapp.feature_authentication.firebase.emailValidator
import com.techconsul.pokemonapp.feature_authentication.firebase.passwordValidator
import com.techconsul.pokemonapp.feature_pokedex.util.Resource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthorizeUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun createUser(authUser: AuthUser) = flow {
        val checkCredentials = checkUserData(authUser.email,authUser.password)
       if( checkCredentials == null) {
                coroutineScope {
                    authRepository.createUser(authUser).collect {
                        emit(it)
                    }
                }
            } else {
                emit(Resource.Error(message = checkCredentials))
            }

    }

    suspend fun loginUser(authUser: AuthUser) = flow{

        val checkCredentials = checkUserData(authUser.email,authUser.password)
        if( checkCredentials == null) {
            coroutineScope {
                authRepository.loginUser(authUser).collect {
                    emit(it)
                }
            }
        } else {
            emit(Resource.Error(message = checkCredentials))
        }
    }

    private fun checkUserData(email: String?, password: String?): String? {
        val emailValidatorMessage = emailValidator(email)
        val passwordValidatorMessage = passwordValidator(password)
        if (emailValidatorMessage == null) {
            if (passwordValidatorMessage == null) {
                return null
            } else {
                return passwordValidatorMessage
            }
        } else {
            return emailValidatorMessage
        }
    }
}