package com.techconsul.pokemonapp.feature_authentication.firebase.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.techconsul.pokemonapp.feature_authentication.firebase.data.model.AuthUser
import com.techconsul.pokemonapp.feature_authentication.firebase.data.repository.FakeAuthRepository
import com.techconsul.pokemonapp.feature_authentication.firebase.domain.use_case.AuthorizeUserUseCase
import com.techconsul.pokemonapp.feature_authentication.firebase.presentation.AuthUserState
import com.techconsul.pokemonapp.feature_authentication.firebase.presentation.FirebaseAuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class FirebaseAuthViewModelTest {

    private lateinit var authViewModel: FirebaseAuthViewModel
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
        authViewModel = FirebaseAuthViewModel(authUser)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `call create user with invalid credentials_expect_Error`() {
        //Using double for just to exhaust the possibilities of bypassing the register flow with bad data
        val wrongPasswords = listOf("", "1234", "asdfg12", "12345678")
        val wrongEmails = listOf("seba", "seba@", "test123@gmail")
        for (email in wrongEmails) {
            for (password in wrongPasswords) {
                val user = AuthUser(email, password)
                assert(authViewModel.authState.value.encounteredError == null) // check first time, should be null as there is no error
                authViewModel.createUserWithCredentials(user)
                assert(authViewModel.authState.value.encounteredError != null) // that means an error has happened
                authViewModel.authState.value = AuthUserState()
            }

        }
    }

        @Test
        fun `call login user with invalid credentials_expect_Error`(){
            //Using double for just to exhaust the possibilities of bypassing the register flow with bad data
            val wrongPasswords = listOf("","1234","asdfg12","12345678")
            val wrongEmails = listOf("seba","seba@","test123@gmail")
            for(email in wrongEmails){
                for(password in wrongPasswords){
                    val user = AuthUser(email,password)
                    assert(authViewModel.authState.value.encounteredError == null) // check first time, should be null as there is no error
                    authViewModel.createUserWithCredentials(user)
                    assert(authViewModel.authState.value.encounteredError != null) // that means an error has happened
                    authViewModel.authState.value = AuthUserState()
                }

            }




    }
}