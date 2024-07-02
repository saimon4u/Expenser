package com.example.expenser.presentation.splash

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expenser.MainActivity
import com.example.expenser.presentation.sign_in.UserData
import com.example.expenser.presentation.components.CurrencyBox
import com.example.expenser.presentation.components.CurrencyListSheet
import com.example.expenser.presentation.components.Divider
import com.example.expenser.presentation.splash.components.Heading
import com.example.expenser.presentation.splash.components.currencyList
import com.example.expenser.ui.theme.fonts
import com.example.expenser.util.NavRoute
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingScreen(
    navController: NavController,
    userData: UserData?,
    context: MainActivity
){

    var selectedCurrency by remember {
        mutableStateOf(currencyList[0])
    }

    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val viewModel = hiltViewModel<SplashViewModel>()

    if(bottomSheetState.isVisible){
        CurrencyListSheet(
            bottomSheetState = bottomSheetState,
            onDismiss = {
                scope.launch {
                    bottomSheetState.hide()
                }
            },
            onItemClick = {item ->
                selectedCurrency = item
                scope.launch {
                    bottomSheetState.hide()
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Heading(userName = userData?.userName!!)

        Divider()

        CurrencyBox(
            currency = selectedCurrency,
            onCurrencyClick = {
                scope.launch {
                    bottomSheetState.show()
                }
            }
        )

        Divider()

        Button(
            onClick = {
                viewModel.updateUserSettings(userData.userId, selectedCurrency)
                onBoardingIsFinished(context)
                navController.navigate(NavRoute.Dashboard.route)
            },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                MaterialTheme.colorScheme.onBackground
            )
        ) {
            Text(
                text = "I'm done! Take me to the dashboard",
                fontFamily = fonts,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}

private fun onBoardingIsFinished(context: MainActivity) {
    val sharedPreferences = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("isFinished", true)
    editor.apply()
}