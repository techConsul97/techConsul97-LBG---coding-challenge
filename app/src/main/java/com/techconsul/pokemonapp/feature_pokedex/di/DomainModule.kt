package com.techconsul.pokemonapp.feature_pokedex.di

import com.techconsul.pokemonapp.feature_pokedex.domain.repository.PokemonRepository
import com.techconsul.pokemonapp.feature_pokedex.domain.use_case.GetPokemonInfo
import com.techconsul.pokemonapp.feature_pokedex.domain.use_case.GetPokemons
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun provideGetPokemonsUseCase(repository: PokemonRepository) = GetPokemons(repository)

    @Provides
    fun provideGetPokemonDetailsUseCase(repository: PokemonRepository) = GetPokemonInfo(repository)
}