package com.techconsul.pokemonapp.feature_pokedex.data.fake_repository

import com.techconsul.pokemonapp.feature_pokedex.data.remote.responses.Pokemon
import com.techconsul.pokemonapp.feature_pokedex.data.remote.responses.PokemonList
import com.techconsul.pokemonapp.feature_pokedex.domain.repository.PokemonRepository
import com.techconsul.pokemonapp.feature_pokedex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FakePokemonRepositoryImpl:PokemonRepository{
    override suspend fun getPokemonList(limit: Int, offset: Int): Flow<Resource<PokemonList>> = flow {
        when(offset){
            1 -> emit(Resource.Loading())
            2 -> emit(Resource.Error("Test test test"))
            3 -> emit(Resource.Success(PokemonList(1,"1",1, listOf())))
        }
    }

    override suspend fun getPokemonInfo(pokemonName: String): Flow<Resource<Pokemon>> = flow {
        when(pokemonName){
            "Loading" -> emit(Resource.Loading())
            "Failed" -> emit(Resource.Error("test"))
        }
    }

}