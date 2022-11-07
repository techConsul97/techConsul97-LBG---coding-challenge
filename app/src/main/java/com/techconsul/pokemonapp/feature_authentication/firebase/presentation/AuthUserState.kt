package com.techconsul.pokemonapp.feature_authentication.firebase.presentation

import com.techconsul.pokemonapp.feature_authentication.firebase.data.model.AuthUser

data class AuthUserState(
    val user: AuthUser? = null,
    val encounteredError:String? = null,
    val isLoading: Boolean = false
)