package com.example.expenser.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.WavingHand
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expenser.presentation.components.CustomSnackbar
import com.example.expenser.util.TransactionType
import com.example.expenser.presentation.components.TransactionDialog
import com.example.expenser.presentation.sign_in.UserData
import com.example.expenser.ui.theme.Emerald500
import com.example.expenser.ui.theme.Red500
import com.example.expenser.ui.theme.fonts
import kotlinx.coroutines.launch


@Composable
fun Dashboard(
    modifier: Modifier = Modifier,
    userData: UserData?
){

    val dashboardViewModel = hiltViewModel<DashboardViewModel>()
    val dashboardState = dashboardViewModel.dashboardState.collectAsState().value

    val openTransactionDialog = remember { mutableStateOf(false) }
    val selectedTransactionType = remember { mutableStateOf(TransactionType.Income) }






    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()



    LaunchedEffect(
        dashboardState.showSnackbar
    ) {
        if(dashboardState.showSnackbar){
            scope.launch {
                if(snackbarHostState.currentSnackbarData != null) snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar("", "", duration = SnackbarDuration.Short)
            }
            dashboardViewModel.updateSnackbarState()
        }
    }



    Scaffold {padding->
        if(openTransactionDialog.value){
            TransactionDialog(
                transactionType = selectedTransactionType.value,
                onDismissRequest = {
                    openTransactionDialog.value = false
                },
                onCategoryCreate = dashboardViewModel::onCategoryCreate,
                dashboardState = dashboardState,
                getAllCategories = dashboardViewModel::getAllCategories,
                onCreateTransaction = dashboardViewModel::onTransactionCreate,
                showSnackbar = dashboardViewModel::showSnackbar
            )
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
            ){
                Text(
                    text = ("Hello ${userData?.userName ?: "User"}!"),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = Icons.Rounded.WavingHand,
                    contentDescription = "Waving Hand",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Emerald500
                    ),
                    onClick = {
                        openTransactionDialog.value = true
                        selectedTransactionType.value = TransactionType.Income
                    }
                ) {
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            imageVector = Icons.Rounded.AddCircleOutline,
                            contentDescription = "Add Icon",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(15.dp)
                        )
                        Text(
                            text = "New Income 💰",
                            fontSize = 10.sp,
                            fontFamily = fonts,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                    }
                }
                Button(
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red500
                    ),
                    onClick = {
                        openTransactionDialog.value = true
                        selectedTransactionType.value = TransactionType.Expense
                    }
                ) {
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            imageVector = Icons.Rounded.AddCircleOutline,
                            contentDescription = "Add Icon",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(15.dp)
                        )
                        Text(
                            text = "New Expense 😤",
                            fontSize = 10.sp,
                            fontFamily = fonts,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                    }
                }
            }
        }

        CustomSnackbar(
            snackbarHostState = snackbarHostState,
            modifier = Modifier,
            message = dashboardState.snackbarMessage,
        )
    }
}