package com.techconsul.pokemonapp.feature_authentication.firebase.data.model

data class AuthUser(
    val email: String? = "",
    val password: String? = "",
    val displayName:String? = ""
)