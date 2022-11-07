package com.techconsul.pokemonapp.feature_pokedex.presentation.pokemonlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.techconsul.pokemonapp.feature_pokedex.data.fake_repository.FakePokemonRepositoryImpl
import com.techconsul.pokemonapp.feature_pokedex.data.models.PokedexListEntry
import com.techconsul.pokemonapp.feature_pokedex.data.remote.responses.PokemonList
import com.techconsul.pokemonapp.feature_pokedex.data.remote.responses.Result
import com.techconsul.pokemonapp.feature_pokedex.domain.use_case.GetPokemons
import com.techconsul.pokemonapp.feature_pokedex.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class PokemonListViewModelUnitTest {

    private lateinit var viewModel: PokemonListViewModel

    @Mock
    private lateinit var getPokemons: GetPokemons

    private val dispatchers = UnconfinedTestDispatcher()

    @get:Rule
    //tell the application to run the tests INSTANTLY, HIGH PRIORITY
    val rule: TestRule = InstantTaskExecutorRule()


    @Before
    fun setUp(){
        Dispatchers.setMain(dispatchers)
        MockitoAnnotations.openMocks(this)
        viewModel = PokemonListViewModel(getPokemons)
    }


    /*By calling the function getPokemons(20.0) I am able to override the response
    *   by using Mockito, because those 2 values, 20, 0, are the first values
    *   that the function uses. With this, I get the desired response after first emission
    * */

    @Test
    fun `mock the useCase to return Loading, check if the state updates accordingly`() = runBlocking{

        whenever(getPokemons(20,0)).thenReturn(flow { Resource.Loading<PokemonList>() })
        viewModel.loadPokemonListPaginated()
        assertTrue(viewModel.isLoading.value)
        assertEquals(viewModel.pokemonList.value, listOf<PokedexListEntry>())
        assertTrue(viewModel.loadError.value == "")

    }

    @Test
    fun `mock the useCase to return Error, check if the states are updated accordingly`() = runBlocking {
        whenever(getPokemons(20,0)).thenReturn(flow {emit(Resource.Error("Error"))  })
        viewModel.loadPokemonListPaginated()
        assertFalse(viewModel.isLoading.value)
        assertEquals(viewModel.pokemonList.value, listOf<PokedexListEntry>())
        assertTrue(viewModel.loadError.value == "Error")
    }

    @Test
    fun `mock the useCase to return Succes, check if the states are updated accordingly`() = runBlocking {
        whenever(getPokemons(20,0)).thenReturn(flow {emit(Resource.Success(data = PokemonList(1,"next",1,
               listOf(Result("seba","url1")))))})
        viewModel.loadPokemonListPaginated()
        assertFalse(viewModel.isLoading.value)
        assertEquals(viewModel.pokemonList.value.size,1)
        assertTrue(viewModel.loadError.value == "")
    }


}