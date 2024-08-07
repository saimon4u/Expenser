package com.example.expenser.presentation.dashboard.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expenser.presentation.components.CustomDateRangePicker
import com.example.expenser.presentation.components.DateRangeView
import com.example.expenser.presentation.dashboard.DashboardState
import com.example.expenser.ui.theme.Emerald500
import com.example.expenser.ui.theme.Purple40
import com.example.expenser.ui.theme.Red500
import com.example.expenser.ui.theme.fonts
import com.example.expenser.util.TransactionType
import com.example.expenser.util.convertDateToMillis
import com.example.expenser.util.convertMillisToDate
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Overview(
    modifier: Modifier = Modifier,
    dashboardState: DashboardState,
    onCategoryFetchingError: (String, TransactionType) -> Unit,
    getAllTransaction: (String, Long, Long) -> Unit,
    getBalance: () -> Unit
){

    val dateRangePickerState = rememberDateRangePickerState()
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var selectedStartDate by remember {
        mutableStateOf(Calendar.getInstance(TimeZone.getTimeZone("Asia/Dhaka")).apply { set(Calendar.DAY_OF_MONTH, 1) }.timeInMillis.convertMillisToDate())
    }

    var selectedEndDate by remember {
        mutableStateOf(System.currentTimeMillis().convertMillisToDate())
    }

    var rangeSelected by remember{
        mutableStateOf(false)
    }


    if (bottomSheetState.currentValue != SheetValue.Hidden){
        CustomDateRangePicker(
            dateRangePickerState = dateRangePickerState,
            bottomSheetState = bottomSheetState,
            onDismiss = {
                scope.launch {
                    bottomSheetState.hide()
                }
            },
            onDateSelect = {
                rangeSelected = true
                scope.launch {
                    bottomSheetState.hide()
                }
            }
        )
    }


    LaunchedEffect(key1 = rangeSelected) {
        if(rangeSelected){
            selectedStartDate = dateRangePickerState.selectedStartDateMillis?.convertMillisToDate() ?: selectedStartDate
            selectedEndDate = dateRangePickerState.selectedEndDateMillis?.convertMillisToDate() ?: selectedEndDate
            rangeSelected = false
        }
    }

    LaunchedEffect(key1 = dashboardState.categoryFetchingError) {
        if(dashboardState.categoryFetchingError){
            onCategoryFetchingError(dashboardState.userData!!.userId, dashboardState.categoryErrorType)
        }
    }

    LaunchedEffect(key1 = selectedStartDate, key2 = selectedEndDate, key3 = dashboardState.isTransactionCreated) {
        getAllTransaction(
            dashboardState.userData!!.userId,
            selectedStartDate.convertDateToMillis(),
            selectedEndDate.convertDateToMillis()
        )
    }

    LaunchedEffect(key1 = dashboardState.isTransactionFetching) {
        if(!dashboardState.isTransactionFetching){
            getBalance()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Overview",
            fontFamily = fonts,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        DateRangeView(
            selectedStartDate = selectedStartDate,
            selectedEndDate = selectedEndDate,
            onClick = {
                scope.launch {
                    bottomSheetState.show()
                }
            }
        )
        
        Spacer(modifier = Modifier.height(10.dp))

        BalanceView(
            isLoading = dashboardState.isTransactionFetching,
            contentAfterLoading = {
                BalanceBox(
                    balance = dashboardState.incomeBalance,
                    transactionType = "Income",
                    icon = Icons.Default.TrendingUp,
                    iconTint = Color.Green,
                    bgColor = Emerald500,
                    moneyIcon = dashboardState.userSettings?.currency?.icon
                )
            },
        )

        Spacer(modifier = Modifier.height(10.dp))

        BalanceView(
            isLoading = dashboardState.isTransactionFetching,
            contentAfterLoading = {
                BalanceBox(
                    balance = dashboardState.expenseBalance,
                    transactionType = "Expense",
                    iconTint = Color.Red,
                    icon = Icons.Default.TrendingDown,
                    bgColor = Red500,
                    moneyIcon = dashboardState.userSettings?.currency?.icon
                )
            },
        )

        Spacer(modifier = Modifier.height(10.dp))

        BalanceView(
            isLoading = dashboardState.isTransactionFetching,
            contentAfterLoading = {
                BalanceBox(
                    balance = dashboardState.incomeBalance - dashboardState.expenseBalance,
                    transactionType = "Balance",
                    icon = Icons.Default.Wallet,
                    iconTint = Color.White,
                    bgColor = Purple40,
                    moneyIcon = dashboardState.userSettings?.currency?.icon
                )
            },
        )

        Spacer(modifier = Modifier.height(10.dp))

        TransactionsByCategoryView(
            isLoading = dashboardState.isCategoryFetching || dashboardState.isTransactionFetching || dashboardState.categoryFetchingError,
            contentAfterLoading ={
                TransactionByCategoryBox(
                    categoryList = dashboardState.incomeCategoryList,
                    heading = "Incomes",
                    totalAmount = dashboardState.incomeBalance,
                    getAmount = {category->
                        var amount = 0.0
                        dashboardState.transactionList.map {transaction->
                            if(transaction.category == category.name && transaction.type == category.type){
                                amount += transaction.amount
                            }
                        }
                        amount
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    moneyIcon = dashboardState.userSettings?.currency?.icon
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        TransactionsByCategoryView(
            isLoading = dashboardState.isCategoryFetching || dashboardState.isTransactionFetching || dashboardState.categoryFetchingError,
            contentAfterLoading ={
                TransactionByCategoryBox(
                    categoryList = dashboardState.expenseCategoryList,
                    heading = "Expenses",
                    totalAmount = dashboardState.expenseBalance,
                    getAmount = {category->
                        var amount = 0.0
                        dashboardState.transactionList.map {transaction->
                            if(transaction.category == category.name && transaction.type == category.type){
                                amount += transaction.amount
                            }
                        }
                        amount
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    moneyIcon = dashboardState.userSettings?.currency?.icon
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
    }

}