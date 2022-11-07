package com.techconsul.pokemonapp.feature_pokedex.domain.repository

import com.techconsul.pokemonapp.feature_pokedex.data.remote.responses.Pokemon
import com.techconsul.pokemonapp.feature_pokedex.data.remote.responses.PokemonList
import com.techconsul.pokemonapp.feature_pokedex.util.Resource
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    suspend fun getPokemonList(limit: Int, offset: Int): Flow<Resource<PokemonList>>

    suspend fun getPokemonInfo(pokemonName: String): Flow<Resource<Pokemon>>

}