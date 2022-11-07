package com.techconsul.pokemonapp.feature_pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.techconsul.pokemonapp.feature_authentication.firebase.presentation.screens.AnimatedSplashScreen
import com.techconsul.pokemonapp.feature_authentication.firebase.presentation.screens.LoginScreen
import com.techconsul.pokemonapp.feature_authentication.firebase.presentation.screens.RegisterScreen
import com.techconsul.pokemonapp.feature_pokedex.presentation.pokemondetails.PokemonDetailScreen
import com.techconsul.pokemonapp.feature_pokedex.presentation.pokemonlist.PokemonListScreen
import com.techconsul.pokemonapp.feature_pokedex.presentation.theme.JetpackComposePokedexTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var isLogged by remember { //workaround for a navController bug
                mutableStateOf(false)
            }
            JetpackComposePokedexTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "splash_screen"
                ) {
                    composable("splash_screen"){
                        AnimatedSplashScreen(navController)
                    }

                    composable("login_screen") {
                        if(!isLogged)
                        LoginScreen(navController = navController)
                    }
                    composable("register_screen") {
                        if(!isLogged)
                        RegisterScreen(navController = navController)
                    }

                    composable("pokemon_list_screen",
                    ) {
                            isLogged = true
                            PokemonListScreen(navController = navController)

                    }
                    composable(
                        "pokemon_detail_screen/{dominantColor}/{pokemonName}",
                        arguments = listOf(
                            navArgument("dominantColor") {
                                type = NavType.IntType
                            },
                            navArgument("pokemonName") {
                                type = NavType.StringType
                            }
                        )
                    ) {
                        val dominantColor = remember {
                            val color = it.arguments?.getInt("dominantColor")
                            color?.let { Color(it) } ?: Color.White
                        }
                        val pokemonName = remember {
                            it.arguments?.getString("pokemonName")
                        }

                        PokemonDetailScreen(
                            dominantColor = dominantColor,
                            pokemonName = pokemonName?.lowercase(Locale.ROOT) ?: "",
                            navController = navController
                        )

                    }
                }
            }
        }
    }
}
