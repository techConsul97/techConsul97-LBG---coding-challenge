package com.techconsul.pokemonapp.feature_authentication.firebase.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.techconsul.pokemonapp.R
import com.techconsul.pokemonapp.feature_authentication.firebase.FirebaseAuthViewModel
import com.techconsul.pokemonapp.feature_authentication.firebase.data.model.AuthUser
import com.techconsul.pokemonapp.feature_authentication.firebase.emailValidator
import com.techconsul.pokemonapp.feature_authentication.firebase.passwordValidator
import com.techconsul.pokemonapp.feature_authentication.firebase.screens.util.InputType
import com.techconsul.pokemonapp.feature_authentication.firebase.screens.util.TextInput
import com.techconsul.pokemonapp.feature_pokedex.ui.theme.Shapes
import kotlinx.coroutines.delay


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: FirebaseAuthViewModel = hiltViewModel()
) {
    if (viewModel.isLogged.value) {
        navController.navigate("pokemon_list_screen")
    } else {

        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val passwordFocusRequester = FocusRequester()
        val focusManager = LocalFocusManager.current
        val keyboard = LocalSoftwareKeyboardController.current
        val context = LocalContext.current


        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(horizontal = 32.dp)
                .testTag("register_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = modifier.height(30.dp))

                Box(modifier = modifier.padding(bottom = 120.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                        null,
                        Modifier
                            .background(Color.Transparent)
                            .clip(Shapes.large),
                        contentScale = ContentScale.Crop,

                        ) //DONT FORGET

            }


            TextInput(
                InputType.Name,
                getValue = { email.value = it },
                keyboardActions = KeyboardActions(
                    onNext = {
                        passwordFocusRequester.requestFocus()
                    })
            )
            TextInput(
                InputType.Password,
                getValue = { password.value = it },
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    viewModel.createUserWithCredentials(AuthUser(email.value, password.value))

                }),
                focusRequester = passwordFocusRequester
            )


            Button(
                onClick = {
                    keyboard?.hide()

                    viewModel.createUserWithCredentials(
                        AuthUser(email.value, password.value)
                    )

                }, modifier = modifier
                    .fillMaxWidth()
                    .testTag("register_button"),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(text = "REGISTER", modifier = modifier.padding(vertical = 8.dp))
            }
            Divider(
                color = Color.White.copy(alpha = 0.3f),
                thickness = 1.dp,
                modifier = modifier.padding(top = 40.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Already have an account?", color = Color.White)
                TextButton(
                    onClick =
                    {
                        navController.navigate("login_screen")
                    }, modifier = Modifier.testTag("login_screen_nav")
                ) {
                    Text("SIGN IN")
                }
            }
            if (viewModel.authState.value.encounteredError != null) {
                val error = viewModel.authState.value.encounteredError
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }

        }
    }

}