package com.techconsul.pokemonapp.feature_pokedex.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.techconsul.pokemonapp.feature_pokedex.data.fake_repository.FakePokemonRepositoryImpl
import com.techconsul.pokemonapp.feature_pokedex.data.remote.responses.PokemonList
import com.techconsul.pokemonapp.feature_pokedex.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class GetPokemonsTest {
    private lateinit var getPokemons: GetPokemons
    private lateinit var fakePokemonImpl: FakePokemonRepositoryImpl

    private val dispatchers = UnconfinedTestDispatcher()
    @get:Rule
    //tell the application to run the tests INSTANTLY, HIGH PRIORITY
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatchers)
        fakePokemonImpl = FakePokemonRepositoryImpl()
        getPokemons = GetPokemons(fakePokemonImpl)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /* I created the fake repository as following:
    *   -  with offset = 1 ->Loading
    *   -  with offest = 2 -> Error
    *   -  with offset = 4 -> Success*/


    @Test
    fun `call the repository with offset = 1, expect Loading state`()  = runBlocking {
        getPokemons(0,1).collect{
            assert(it is Resource.Loading)
        }
    }

    @Test
    fun `call the repository with offset = 2, expect Error state`()  = runBlocking {
        getPokemons(0,2).collect{
            assert(it is Resource.Error)
            assert(it.message.equals("Test test test"))
        }
    }
    @Test
    fun `call the repository with offset = 3, expect Success state`()  = runBlocking {
        getPokemons(0,3).collect{
            assert(it is Resource.Success)
            assert(it.data!! == PokemonList(1,"1",1, listOf()))
        }
    }
}