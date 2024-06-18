package com.example.expenser.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.expenser.ui.theme.Emerald500
import kotlinx.coroutines.launch


@Composable
fun TransactionHistory() {

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()
    Scaffold {padd->
        val p = padd
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("", "", duration = SnackbarDuration.Short)
                    }
                }
            ) {
                Text(text = "Click")
            }
        }
        CustomSnackbar(
            snackbarHostState = snackbarHostState,
            modifier = Modifier,
            message = "Transaction Added",
        )
    }
}