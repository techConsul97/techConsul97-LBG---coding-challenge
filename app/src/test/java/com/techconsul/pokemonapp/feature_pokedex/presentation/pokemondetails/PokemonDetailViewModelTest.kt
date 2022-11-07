package com.techconsul.pokemonapp.feature_pokedex.presentation.pokemondetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.techconsul.pokemonapp.feature_pokedex.domain.use_case.GetPokemonInfo
import com.techconsul.pokemonapp.feature_pokedex.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonDetailViewModelTest{
    private lateinit var viewModel: PokemonDetailViewModel

    @Mock
    private lateinit var getPokemonDetails: GetPokemonInfo

    private val dispatchers = UnconfinedTestDispatcher()

    @get:Rule
    //tell the application to run the tests INSTANTLY, HIGH PRIORITY
    val rule: TestRule = InstantTaskExecutorRule()


    @Before
    fun setUp(){
        Dispatchers.setMain(dispatchers)
        MockitoAnnotations.openMocks(this)
        viewModel = PokemonDetailViewModel(getPokemonDetails)
    }

    @After
    fun teardown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `simulate Loading from useCase, check if it is as expected`()= runBlocking {
        whenever(getPokemonDetails("test")).thenReturn(Resource.Loading())
       assert(viewModel.getPokemonInfo("test") is Resource.Loading)

    }

    @Test
    fun `simulate Error from useCase, check if it is as expected`()= runBlocking {
        whenever(getPokemonDetails("test")).thenReturn(Resource.Error(message = "test"))
        assert(viewModel.getPokemonInfo("test") is Resource.Error)
        assertTrue(viewModel.getPokemonInfo("test").message.equals("test"))
    }

}