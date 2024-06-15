package com.example.expenser

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expenser.presentation.MainScreen
import com.example.expenser.presentation.sign_in.GoogleAuthClient
import com.example.expenser.presentation.sign_in.SignInScreen
import com.example.expenser.presentation.sign_in.SignInViewModel
import com.example.expenser.presentation.util.NavRoute
import com.example.expenser.ui.theme.ExpenserTheme
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenserTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val signInViewModel = viewModel<SignInViewModel>()
                    val state by signInViewModel.state.collectAsStateWithLifecycle()

                    LaunchedEffect(
                        key1 = Unit
                    ) {
                        if(googleAuthUiClient.getSignedInUser() != null){
                            navController.navigate(NavRoute.Dashboard.route)
                        } else{
                            navController.navigate(NavRoute.SIGN_IN.route)
                        }
                    }

                    NavHost(
                        navController = navController,
                        startDestination = NavRoute.SIGN_IN.route
                    ){
                        composable(
                            route = NavRoute.SIGN_IN.route
                        ){

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = {result ->
                                    if(result.resultCode == RESULT_OK){
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            signInViewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )

                            LaunchedEffect(
                                key1 = state.isSignInSuccessful
                            ) {

                                if(state.isSignInSuccessful){
                                    Toast.makeText(applicationContext, "Sign In Successful", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                    navController.navigate(NavRoute.Dashboard.route)
                                    signInViewModel.resetState()
                                }

                            }

                            SignInScreen(
                                state = state,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                }
                            )
                        }


                        composable(
                            route = NavRoute.Dashboard.route
                        ){
                            MainScreen(
                                userData = googleAuthUiClient.getSignedInUser(),
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(applicationContext, "Signed Out", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                        navController.navigate(NavRoute.SIGN_IN.route)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
