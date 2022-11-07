package com.techconsul.pokemonapp.feature_authentication.firebase.screens

import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.techconsul.pokemonapp.feature_authentication.firebase.screens.util.InputType
import com.techconsul.pokemonapp.feature_pokedex.MainActivity
import com.techconsul.pokemonapp.feature_pokedex.presentation.pokemonlist.PokemonListScreen
import com.techconsul.pokemonapp.feature_pokedex.ui.theme.JetpackComposePokedexTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random


@HiltAndroidTest
class SignUpScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent {
            val navController = rememberNavController()
            var isLogged by remember {
                mutableStateOf(false)
            }
            JetpackComposePokedexTheme {
                NavHost(navController = navController, startDestination = "register_screen") {
                    composable(route = "login_screen") {
                        if (!isLogged)
                            LoginScreen(navController = navController)
                    }
                    composable(route = "register_screen") {
                        if (!isLogged)
                            RegisterScreen(navController = navController)
                    }
                    composable(route = "pokemon_list_screen") {
                        isLogged = true
                        PokemonListScreen(navController = navController)
                    }
                }

            }
        }

    }
    @Test
    fun enterEmailData_ckeckItExists_clearData_checkAgain(){
        val enterEmail = "sebastian@gmail.com"
        composeRule.onNodeWithTag("Email").assertExists().performTextInput(enterEmail)
        composeRule.onNodeWithTag("Email").assert(hasText(enterEmail)).performTextClearance()
        composeRule.onNodeWithTag("Email").assert(hasText(""))
    }

    @Test
    fun enterPasswordData_ckeckItExists_clearData_checkAgain(){
        val enterPassword = "1223"
        composeRule.onNodeWithTag(InputType.Password.label).performTextInput(enterPassword)
    }

    @Test
    fun pressRegisterButton_checkIfNavigationHappened(){
        composeRule.onNodeWithTag("login_screen_nav").assertExists().performClick()
        composeRule.onNodeWithTag("login_screen").assertExists()
    }

    @Test
    fun enterIncorrectData_checkIfTheNavigationDidntOccur(){
        val enterMail = listOf("","test","test@","test@gmail")
        val enterPassword = mutableListOf("thisIsCorrect1","nope","","111111111")


        for(index in enterPassword.indices){
            composeRule.onNodeWithTag("Email").assertExists().performTextInput(enterMail.last())
            composeRule.onNodeWithTag("Password").assertExists().performTextInput(enterPassword[index])
            composeRule.onNodeWithTag("register_button").assertExists().performClick()
            composeRule.onNodeWithTag("register_screen").assertExists()
            composeRule.onNodeWithTag("Email").performTextClearance()
            composeRule.onNodeWithTag("Password").performTextClearance()
        }

    }

    @Test
    fun enterCorrectDataFormatOnce_checkIfWeNavigateToMainScreen(){
        val enterEmail = "test${Random(10)}${Random(20)}@gmail.com" //correct form
        val enterPassword = "123password" // 8+ chars, 1 digit, 1 letter
        composeRule.onNodeWithTag("Email").assertExists().performTextInput(enterEmail)
        composeRule.onNodeWithTag("Password").assertExists().performTextInput(enterPassword)
        composeRule.onNodeWithTag("register_button").assertExists().performClick()

    }

    @Test
    fun clickLoginToRedirectToLoginScreenEnterValidDataToLoginAccount_checkIfMainScreenReached(){
        val enterEmail = "test23@gmail.com" //correct form
        val enterPassword = "123password" // 8+ chars, 1 digit, 1 letter
        composeRule.onNodeWithTag("login_screen_nav").assertExists().performClick()
        composeRule.onNodeWithTag("login_screen").assertExists()
        composeRule.onNodeWithTag("Email").assertExists().performTextInput(enterEmail)
        composeRule.onNodeWithTag("Password").assertExists().performTextInput(enterPassword)
        composeRule.onNodeWithTag("login_button").assertExists().performClick()

    }

}

