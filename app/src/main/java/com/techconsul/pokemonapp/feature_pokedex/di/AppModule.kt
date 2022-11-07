package com.techconsul.pokemonapp.feature_pokedex.di

import com.techconsul.pokemonapp.feature_pokedex.data.remote.PokeApi
import com.techconsul.pokemonapp.feature_pokedex.data.repository.PokemonRepositoryImpl
import com.techconsul.pokemonapp.feature_pokedex.domain.repository.PokemonRepository
import com.techconsul.pokemonapp.feature_pokedex.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun providePokeApi(): PokeApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PokeApi::class.java)
    }
    @Singleton
    @Provides
    fun providePokemonRepository(
        api: PokeApi
    ) :PokemonRepository = PokemonRepositoryImpl(api)




}