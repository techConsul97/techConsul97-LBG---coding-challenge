package com.techconsul.pokemonapp.feature_pokedex.presentation.pokemondetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techconsul.pokemonapp.feature_pokedex.data.remote.responses.Pokemon
import com.techconsul.pokemonapp.feature_pokedex.domain.use_case.GetPokemonInfo
import com.techconsul.pokemonapp.feature_pokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemonDetails: GetPokemonInfo
) : ViewModel() {



    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        return getPokemonDetails(pokemonName)
    }

}