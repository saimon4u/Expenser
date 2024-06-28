package com.example.expenser.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.expenser.domain.model.Category
import com.example.expenser.presentation.dashboard.DashboardState
import com.example.expenser.presentation.dashboard.DashboardViewModel
import com.example.expenser.ui.theme.Emerald500
import com.example.expenser.ui.theme.Purple40
import com.example.expenser.ui.theme.Purple80
import com.example.expenser.ui.theme.Red500
import com.example.expenser.ui.theme.fonts
import com.example.expenser.util.TransactionType
import com.example.expenser.util.convertMillisToDate
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Overview(
    modifier: Modifier = Modifier,
    dashboardState: DashboardState,
){

    val dateRangePickerState = rememberDateRangePickerState()
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var selectedStartDate by remember {
        mutableStateOf(Calendar.getInstance().apply { set(Calendar.DAY_OF_MONTH, 2) }.timeInMillis.convertMillisToDate())
    }

    var selectedEndDate by remember {
        mutableStateOf(System.currentTimeMillis().convertMillisToDate())
    }


    if (bottomSheetState.currentValue != SheetValue.Hidden){
        CustomDateRangePicker(
            dateRangePickerState = dateRangePickerState,
            bottomSheetState = bottomSheetState,
            onDismiss = {
                scope.launch {
                    bottomSheetState.hide()
                }
            }
        )
    }


    LaunchedEffect(key1 = dateRangePickerState.selectedStartDateMillis, key2 = dateRangePickerState.selectedEndDateMillis) {
        selectedStartDate = dateRangePickerState.selectedStartDateMillis?.convertMillisToDate() ?: selectedStartDate
        selectedEndDate = dateRangePickerState.selectedEndDateMillis?.convertMillisToDate() ?: selectedEndDate
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
            isLoading = dashboardState.isBalanceFetching,
            contentAfterLoading = {
                BalanceBox(
                    balance = dashboardState.incomeBalance,
                    transactionType = "Income",
                    icon = Icons.Default.TrendingUp,
                    iconTint = Color.Green,
                    bgColor = Emerald500
                )
            },
        )

        Spacer(modifier = Modifier.height(10.dp))

        BalanceView(
            isLoading = dashboardState.isBalanceFetching,
            contentAfterLoading = {
                BalanceBox(
                    balance = dashboardState.expenseBalance,
                    transactionType = "Expense",
                    iconTint = Color.Red,
                    icon = Icons.Default.TrendingDown,
                    bgColor = Red500
                )
            },
        )

        Spacer(modifier = Modifier.height(10.dp))

        BalanceView(
            isLoading = dashboardState.isBalanceFetching,
            contentAfterLoading = {
                BalanceBox(
                    balance = dashboardState.incomeBalance - dashboardState.expenseBalance,
                    transactionType = "Balance",
                    icon = Icons.Default.Wallet,
                    iconTint = Color.White,
                    bgColor = Purple40
                )
            },
        )

        Spacer(modifier = Modifier.height(10.dp))

        TransactionsByCategoryView(
            isLoading = dashboardState.isCategoryFetching || dashboardState.isTransactionFetching,
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
                        if(amount.isNaN()) 0.000001
                        else amount
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(horizontal = 8.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        TransactionsByCategoryView(
            isLoading = dashboardState.isCategoryFetching || dashboardState.isTransactionFetching,
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
                        .height(250.dp)
                        .padding(horizontal = 8.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(horizontal = 8.dp)
        )
    }

}