package com.techconsul.pokemonapp.feature_authentication.firebase.screens


import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.*
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
import com.google.android.gms.common.api.ApiException
import com.techconsul.pokemonapp.R
import com.techconsul.pokemonapp.feature_authentication.firebase.FirebaseAuthViewModel
import com.techconsul.pokemonapp.feature_authentication.firebase.data.model.AuthUser
import com.techconsul.pokemonapp.feature_authentication.firebase.domain.util.AuthResultContract
import com.techconsul.pokemonapp.feature_authentication.firebase.emailValidator
import com.techconsul.pokemonapp.feature_authentication.firebase.passwordValidator
import com.techconsul.pokemonapp.feature_authentication.firebase.screens.util.ButtonInput
import com.techconsul.pokemonapp.feature_authentication.firebase.screens.util.InputType
import com.techconsul.pokemonapp.feature_authentication.firebase.screens.util.TextInput
import com.techconsul.pokemonapp.feature_pokedex.ui.theme.Shapes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: FirebaseAuthViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    if (viewModel.isLogged.value) {
        navController.navigate("pokemon_list_screen")

    } else {
        var visible by remember {
            mutableStateOf(false)
        }
        val coroutineScope = rememberCoroutineScope()
        val keyboard = LocalSoftwareKeyboardController.current
        var text by remember { mutableStateOf<String?>(null) }
        val user by remember(viewModel) { viewModel.authState }
        val userf = user.user
        val signInRequestCode = 1
        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val passwordFocusRequester = FocusRequester()
        val focusManager = LocalFocusManager.current

        val authResultLauncher =
            rememberLauncherForActivityResult(contract = AuthResultContract()) { task ->
                try {
                    val account = task?.getResult(ApiException::class.java)
                    if (account == null) {
                        text = "Google sign in failed"
                    } else {
                        coroutineScope.launch {
                            viewModel.googleSignIn(
                                email = account.email!!,
                                displayName = account.displayName!!
                            )
                        }
                    }
                } catch (e: ApiException) {
                    text = "Google sign in failed"
                }
            }
        LaunchedEffect(key1 = true) {
            delay(1000)
            visible = true
        }
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(horizontal = 32.dp)
                .testTag("login_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Box(modifier = modifier.padding(bottom = 80.dp)) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                        null,
                        Modifier
                            .background(Color.Transparent)
                            .clip(Shapes.large),
                        contentScale = ContentScale.Crop,
                    ) //DONT FORGET
                }
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
                    viewModel.loginUser(AuthUser(email.value, password.value))
                }),
                focusRequester = passwordFocusRequester
            )


            Button(
                onClick = {
                    keyboard?.hide()
                    viewModel.loginUser(AuthUser(email.value, password.value))

                },
                modifier = modifier
                    .fillMaxWidth()
                    .testTag("login_button"),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(text = "SIGN IN", modifier = modifier.padding(vertical = 8.dp))
            }

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    ButtonInput(
                        imageRes = R.drawable.google_icon,
                        callbackFunction = {
                            //   authResultLauncher.launch(signInRequestCode)
                            navController.navigate("pokemon_list_screen")
                        },
                        modifier = modifier
                    )//GOOGLE


                }


            }
            Divider(
                color = Color.White.copy(alpha = 0.3f),
                thickness = 1.dp,
                modifier = modifier.padding(top = 40.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have an account?", color = Color.Black)
                TextButton(
                    onClick =
                    {
                        navController.navigate("register_screen")
                    }, modifier = Modifier.testTag("register_screen_nav")
                ) {
                    Text("SIGN UP")
                }
            }


        }
    }
    if (viewModel.authState.value.encounteredError != null) {
        val error = viewModel.authState.value.encounteredError
        Toast.makeText(context, error, Toast.LENGTH_SHORT)
            .show()
    }

}


