package com.techconsul.pokemonapp.feature_pokedex.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.techconsul.pokemonapp.feature_pokedex.data.fake_repository.FakePokemonRepositoryImpl
import com.techconsul.pokemonapp.feature_pokedex.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class GetPokemonInfoTest {

    private lateinit var getPokemonInfo: GetPokemonInfo
    private lateinit var fakePokemonImpl: FakePokemonRepositoryImpl

    private val dispatchers = UnconfinedTestDispatcher()

    @get:Rule
    //tell the application to run the tests INSTANTLY, HIGH PRIORITY
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatchers)
        fakePokemonImpl = FakePokemonRepositoryImpl()
        getPokemonInfo = GetPokemonInfo(fakePokemonImpl)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `calling the function with String 'Loading', expect Loading State`() = runBlocking {
        val state = getPokemonInfo("Loading")
        assert(state is Resource.Loading)
    }

    @Test
    fun `calling the function with String 'Failed', expect Error State`() = runBlocking {
        val state = getPokemonInfo("Failed")
        assert(state is Resource.Error)
        assert(state.message.equals("test"))
    }
}