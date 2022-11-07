package com.techconsul.pokemonapp.feature_authentication.firebase.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.techconsul.pokemonapp.feature_authentication.firebase.data.model.AuthUser
import com.techconsul.pokemonapp.feature_authentication.firebase.data.repository.FakeAuthRepository
import com.techconsul.pokemonapp.feature_pokedex.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
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
class AuthorizeUserUseCaseTest {

    private lateinit var authUser: AuthorizeUserUseCase
    private val fakeRepo by lazy { FakeAuthRepository() }
    private val dispatcher = UnconfinedTestDispatcher()

    @get:Rule
    //tell the application to run the tests INSTANTLY, HIGH PRIORITY
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        authUser = AuthorizeUserUseCase(fakeRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    /*
    We use .first() and .last() as when email gets checked, there is going to be an emission, the first one.
    The password check happens after, which is going to be the last emission.
    If we want to check if just the password is correct, and we give it a wrong email, using .first will cause test to fail.
    That's why we want to grab the second emission, the password check one.
    */

    @Test
    fun `test create user with wrong email format, expect Failed`() = runBlocking {
        val result = authUser.createUser(AuthUser("", "")).first()
        assert(authUser.createUser(AuthUser("", "")).first() is Resource.Error)
        assert(authUser.createUser(AuthUser("sebastanopol", "")).first() is Resource.Error)
        assert(authUser.createUser(AuthUser("@gmail.com", "")).first() is Resource.Error)
        assert(authUser.createUser(AuthUser("mytest@gmail.com", "1234test")).first() is Resource.Success)

    }

    @Test
    fun `test login user with wrong email format, expect Failed`() = runBlocking {

        assert(authUser.loginUser(AuthUser("", "")).first() is Resource.Error)
        assert(authUser.loginUser(AuthUser("sebastanopol", "")).first() is Resource.Error)
        assert(authUser.loginUser(AuthUser("@gmail.com", "")).first() is Resource.Error)
        assert(authUser.loginUser(AuthUser("mytest@gmail.com", "1234test")).first() is Resource.Success)
    }

    @Test
    fun `test create user with different passwords, expect Failed except for the last one`()= runBlocking {
        assert(authUser.createUser(AuthUser("", "")).last() is Resource.Error)
        assert(authUser.createUser(AuthUser("", "Short")).last() is Resource.Error)
        assert(authUser.createUser(AuthUser("", "noDigits")).last() is Resource.Error)
        assert(authUser.createUser(AuthUser("test@gmail.com", "Digits123")).last() is Resource.Success)
    }

    @Test
    fun `test login user with different passwords, expect Failed except for the last one`()= runBlocking {
        assert(authUser.loginUser(AuthUser("", "")).last() is Resource.Error)
        assert(authUser.loginUser(AuthUser("", "Short")).last() is Resource.Error)
        assert(authUser.loginUser(AuthUser("", "noDigits")).last() is Resource.Error)
        assert(authUser.loginUser(AuthUser("test@gmail.com", "Digits123")).last() is Resource.Success)
    }


}