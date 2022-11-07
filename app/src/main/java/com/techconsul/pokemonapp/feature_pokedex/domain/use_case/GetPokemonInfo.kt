package com.techconsul.pokemonapp.feature_pokedex.domain.use_case

import com.techconsul.pokemonapp.feature_pokedex.data.remote.responses.Pokemon
import com.techconsul.pokemonapp.feature_pokedex.domain.repository.PokemonRepository
import com.techconsul.pokemonapp.feature_pokedex.util.Resource
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class GetPokemonInfo @Inject constructor(
    private val repository: PokemonRepository
){
    suspend operator fun invoke(pokemonName:String):Resource<Pokemon>{
        var result:Resource<Pokemon> = Resource.Loading()
        repository.getPokemonInfo(pokemonName).collectLatest{
            result = it
        }
        return result
    }

}