package com.techconsul.pokemonapp.feature_pokedex.domain.use_case

import com.techconsul.pokemonapp.feature_pokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemons @Inject constructor(
    private val repository: PokemonRepository
){
    suspend operator fun invoke(pageSize:Int, offset:Int) = repository.getPokemonList(pageSize,offset)
}