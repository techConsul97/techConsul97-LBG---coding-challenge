package com.techconsul.pokemonapp.feature_authentication.firebase

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techconsul.pokemonapp.feature_authentication.firebase.data.model.AuthUser
import com.techconsul.pokemonapp.feature_authentication.firebase.domain.use_case.AuthorizeUserUseCase
import com.techconsul.pokemonapp.feature_pokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseAuthViewModel @Inject constructor(
    private val authorizeUserUseCase: AuthorizeUserUseCase
) : ViewModel() {

    private val _authState: MutableState<AuthUserState> = mutableStateOf(value = AuthUserState())
    val authState get() = _authState

    val isLogged = mutableStateOf(false)


    fun createUserWithCredentials(authUser: AuthUser) {

        viewModelScope.launch {
            authorizeUserUseCase.createUser(authUser).collect {
                when (it) {
                    is Resource.Success -> {
                        loginUser(authUser)
                    }
                    is Resource.Error -> {
                        _authState.value = AuthUserState(encounteredError = it.message)
                    }
                    is Resource.Loading -> {
                        _authState.value = AuthUserState(isLoading = true)
                    }
                }
            }
        }
    }

    fun loginUser(authUser: AuthUser) {


        viewModelScope.launch {

            authorizeUserUseCase.loginUser(authUser).collect {
                when (it) {
                    is Resource.Success -> {

                        isLogged.value = true
                    }
                    is Resource.Error -> {
                        _authState.value = AuthUserState(encounteredError = it.message)
                    }
                    is Resource.Loading -> {
                        _authState.value = AuthUserState(isLoading = true)
                    }
                }
            }
        }
    }

    suspend  fun googleSignIn(email:String, displayName:String){
        delay(2000)
        _authState.value = AuthUserState(user = AuthUser(email = email, displayName = displayName))
    }


}