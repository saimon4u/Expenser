package com.example.expenser.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expenser.presentation.dashboard.components.CustomSnackbar
import com.example.expenser.presentation.dashboard.components.Overview
import com.example.expenser.presentation.dashboard.components.TransactionCreateBox
import com.example.expenser.util.TransactionType
import com.example.expenser.presentation.dashboard.components.TransactionDialog
import com.example.expenser.presentation.sign_in.UserData
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox


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
    val scrollState = rememberScrollState()

    val scope = rememberCoroutineScope()

    val action = SwipeAction(
        onSwipe = {
            snackbarHostState.currentSnackbarData?.dismiss()
        },
        icon = {

        },
        background = Color.Transparent,
        isUndo = false,
    )



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

    LaunchedEffect(key1 = Unit) {
        dashboardViewModel.getBalance()
        dashboardViewModel.getAllTransaction(dashboardState.userData!!.userId)
        dashboardViewModel.getCategoriesByType(dashboardState.userData.userId, TransactionType.Income)
        dashboardViewModel.getCategoriesByType(dashboardState.userData.userId, TransactionType.Expense)
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
                getAllCategories = dashboardViewModel::getCategoriesByType,
                onCreateTransaction = dashboardViewModel::onTransactionCreate,
                showSnackbar = dashboardViewModel::showSnackbar
            )
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = ("Hello ${userData?.userName ?: "User"}! 👋"),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TransactionCreateBox(
                onIncomeClick = {
                    openTransactionDialog.value = true
                    selectedTransactionType.value = TransactionType.Income
                },
                onExpenseClick = {
                    openTransactionDialog.value = true
                    selectedTransactionType.value = TransactionType.Expense
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Overview(
                modifier = Modifier
                    .fillMaxWidth(),
                dashboardState = dashboardState,
                onCategoryFetchingError = dashboardViewModel::getCategoriesByType
            )
        }
        SwipeableActionsBox(
            startActions = listOf(action),
            endActions = listOf(action),
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 5.dp),
            backgroundUntilSwipeThreshold = Color.Transparent
        ) {
            CustomSnackbar(
                snackbarHostState = snackbarHostState,
                modifier = Modifier,
                message = dashboardState.snackbarMessage,
            )
        }
    }
}