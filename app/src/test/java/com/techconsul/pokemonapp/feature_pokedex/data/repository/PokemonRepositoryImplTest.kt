package com.techconsul.pokemonapp.feature_pokedex.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.techconsul.pokemonapp.feature_pokedex.data.remote.PokeApi
import com.techconsul.pokemonapp.feature_pokedex.data.remote.responses.Pokemon
import com.techconsul.pokemonapp.feature_pokedex.data.remote.responses.PokemonList
import com.techconsul.pokemonapp.feature_pokedex.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonRepositoryImplTest {
    private lateinit var repositoryImpl: PokemonRepositoryImpl
    private val dispatchers = UnconfinedTestDispatcher()

    @Mock
    private lateinit var api: PokeApi

    @get:Rule
    //tell the application to run the tests INSTANTLY, HIGH PRIORITY
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatchers)
        MockitoAnnotations.openMocks(this)
        repositoryImpl = PokemonRepositoryImpl(api)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `check if first emission is Loading`():Unit = runBlocking {
        assert(repositoryImpl.getPokemonInfo("dsf").first() is Resource.Loading)
        assertFalse(repositoryImpl.getPokemonInfo("dsf").first() is Resource.Error)
        assertFalse(repositoryImpl.getPokemonInfo("dsf").first() is Resource.Success)

        assert(repositoryImpl.getPokemonList(1,2).first() is Resource.Loading)
        assertFalse(repositoryImpl.getPokemonList(1,2).first() is Resource.Error)
        assertFalse(repositoryImpl.getPokemonList(1,2).first() is Resource.Success)
    }

    @Test
    fun `simulate IO Connection Exception, Expect Error with message_There is a problem with your Internet Connection`() =
        runBlocking {
            whenever(api.getPokemonList(1,2))
                .doAnswer { throw IOException() }
            whenever(api.getPokemonInfo("test"))
                .doAnswer { throw IOException() }

            //because the first signal is Loading, we will use .filter on flow
            repositoryImpl.getPokemonList(1,1)
                .filter { it is Resource.Error }
                .collect{
                    assert(it is Resource.Error)
                    assert(it.message.equals("There is a problem with your Internet Connection"))
                }

            repositoryImpl.getPokemonInfo("test")
                .filter { it is Resource.Error }
                .collect{
                    assert(it is Resource.Error)
                    assert(it.message.equals("There is a problem with your Internet Connection"))
                }
        }

    @Test
    fun `simulate HttpException with code 654, Expect that code to be emitted`() = runBlocking {
        whenever(api.getPokemonList(1,1))
            .doAnswer { throw HttpException(Response.error<PokemonList>(654,"body".toResponseBody())) }
        whenever(api.getPokemonInfo("test"))
            .doAnswer { throw HttpException(Response.error<PokemonList>(654,"body".toResponseBody())) }

        repositoryImpl.getPokemonList(1,1)
            .filter { it is Resource.Error }
            .collect{
                assert(it is Resource.Error)
                assert(it.message.equals("Error 654.. Please try again later"))
            }
        repositoryImpl.getPokemonInfo("test")
            .filter { it is Resource.Error }
            .collect{
                assert(it is Resource.Error)
                assert(it.message.equals("Error 654.. Please try again later"))
            }

    }

}