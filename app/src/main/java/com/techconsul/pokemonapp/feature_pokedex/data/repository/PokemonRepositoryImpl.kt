package com.techconsul.pokemonapp.feature_pokedex.data.repository

import com.techconsul.pokemonapp.feature_pokedex.data.remote.PokeApi
import com.techconsul.pokemonapp.feature_pokedex.domain.repository.PokemonRepository
import com.techconsul.pokemonapp.feature_pokedex.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ActivityScoped
class PokemonRepositoryImpl @Inject constructor(
    private val api: PokeApi
) : PokemonRepository {

    override suspend fun getPokemonList(limit: Int, offset: Int) = flow {
        try {
            emit(Resource.Loading())
            val response = api.getPokemonList(limit, offset)
            if (response!= null && response.isSuccessful) {
                if (response.body() != null) {
                    response.body()?.let { emit(Resource.Success(it)) }
                }
            }
        } catch (e: HttpException) {
            val code = e.code()
            emit(Resource.Error("Error $code.. Please try again later"))
        } catch (e: IOException) {
            emit(Resource.Error("There is a problem with your Internet Connection"))
        }

    }

    override suspend fun getPokemonInfo(pokemonName: String) = flow {
        try {
            emit(Resource.Loading())
            val response = api.getPokemonInfo(pokemonName)
            if (response  != null && response.isSuccessful) {
                if (response.body() != null) {
                    response.body()?.let { emit(Resource.Success(it)) }
                }
            }
        } catch (e: HttpException) {
            val code = e.code()
            emit(Resource.Error("Error $code.. Please try again later"))
        } catch (e: IOException) {
            emit(Resource.Error("There is a problem with your Internet Connection"))
        }
    }
}