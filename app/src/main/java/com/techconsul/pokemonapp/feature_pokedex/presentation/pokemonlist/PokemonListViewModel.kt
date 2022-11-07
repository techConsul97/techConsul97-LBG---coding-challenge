package com.techconsul.pokemonapp.feature_pokedex.presentation.pokemonlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.techconsul.pokemonapp.feature_pokedex.data.models.PokedexListEntry
import com.techconsul.pokemonapp.feature_pokedex.domain.use_case.GetPokemonInfo
import com.techconsul.pokemonapp.feature_pokedex.domain.use_case.GetPokemons
import com.techconsul.pokemonapp.feature_pokedex.util.Constants.PAGE_SIZE
import com.techconsul.pokemonapp.feature_pokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemons: GetPokemons,
) : ViewModel() {

    private var curPage = 0

    var pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)



init {
    loadPokemonListPaginated()
}

     fun loadPokemonListPaginated() {

        viewModelScope.launch {
            isLoading.value = true
            getPokemons(PAGE_SIZE, curPage * PAGE_SIZE).collect{result ->
                when(result) {
                    is Resource.Success -> {
                       endReached.value = curPage * PAGE_SIZE >= result.data!!.count
                        val pokedexEntries = result.data.results.map {entry ->
                            val number = if(entry.url.endsWith("/")) {
                                entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                            } else {
                                entry.url.takeLastWhile { it.isDigit() }
                            }
                            val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                            PokedexListEntry(entry.name.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.ROOT
                                ) else it.toString()
                            }, url, number.toInt())
                        }
                        curPage++

                        loadError.value = ""
                        isLoading.value = false
                        pokemonList.value += pokedexEntries
                    }
                    is Resource.Error -> {
                        loadError.value = result.message!!
                        isLoading.value = false
                    }
                }
            }

        }


    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}
