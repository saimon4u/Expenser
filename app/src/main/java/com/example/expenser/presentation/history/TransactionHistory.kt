package com.example.expenser.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expenser.presentation.components.CustomDateRangePicker
import com.example.expenser.presentation.components.DateRangeView
import com.example.expenser.presentation.components.Divider
import com.example.expenser.presentation.history.components.FilterSection
import com.example.expenser.presentation.history.components.SortBottomSheet
import com.example.expenser.presentation.sign_in.UserData
import com.example.expenser.ui.theme.fonts
import com.example.expenser.util.SortFilterItem
import com.example.expenser.util.TransactionType
import com.example.expenser.util.convertDateToMillis
import com.example.expenser.util.convertMillisToDate
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistory(
    userData: UserData?
) {

    val historyViewModel = hiltViewModel<HistoryViewModel>()
    val historyState = historyViewModel.historyState.collectAsState().value
    val dateRangePickerState = rememberDateRangePickerState()
    val dateRangeSheetState = rememberModalBottomSheetState()
    val filterSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var selectedStartDate by remember { mutableStateOf(Calendar.getInstance(TimeZone.getTimeZone("Asia/Dhaka")).apply { set(Calendar.DAY_OF_MONTH, 1) }.timeInMillis.convertMillisToDate())  }
    var selectedEndDate by remember { mutableStateOf(System.currentTimeMillis().convertMillisToDate()) }
    var rangeSelected by remember{ mutableStateOf(false) }

    LaunchedEffect(key1 = rangeSelected) {
        if(rangeSelected){
            selectedStartDate = dateRangePickerState.selectedStartDateMillis?.convertMillisToDate() ?: selectedStartDate
            selectedEndDate = dateRangePickerState.selectedEndDateMillis?.convertMillisToDate() ?: selectedEndDate
            rangeSelected = false
        }
    }

    LaunchedEffect(key1 = selectedStartDate, key2 = selectedEndDate) {
        historyViewModel.getAllTransaction(
            userData!!.userId,
            selectedStartDate.convertDateToMillis(),
            selectedEndDate.convertDateToMillis()
        )
    }

    LaunchedEffect(key1 = Unit) {
        historyViewModel.getCategoriesByType(userData!!.userId, TransactionType.Income)
        historyViewModel.getCategoriesByType(userData.userId, TransactionType.Expense)
    }

    if (dateRangeSheetState.currentValue != SheetValue.Hidden){
        CustomDateRangePicker(
            dateRangePickerState = dateRangePickerState,
            bottomSheetState = dateRangeSheetState,
            onDismiss = {
                scope.launch {
                    dateRangeSheetState.hide()
                }
            },
            onDateSelect = {
                rangeSelected = true
                scope.launch {
                    dateRangeSheetState.hide()
                }
            }
        )
    }


    val sortItemList = historyState.incomeCategoryList.map {
        SortFilterItem(
            name = it.name,
            type = "category",
            isSelected = false
        )
    } + historyState.expenseCategoryList.map {
        SortFilterItem(
            name = it.name,
            type = "category",
            isSelected = false
        )
    } + listOf(TransactionType.Income, TransactionType.Expense).map {
        SortFilterItem(
            name = it.type,
            type = "transaction_type",
            isSelected = false
        )
    }
    var sortType by remember { mutableStateOf("category") }
    if(filterSheetState.currentValue != SheetValue.Hidden){
        SortBottomSheet(
            sheetState = filterSheetState,
            onDismiss = {
                scope.launch {
                    filterSheetState.hide()
                }
            },
            sortFilterItems = sortItemList,
            onItemClick = {
                it.copy(
                    isSelected = !it.isSelected
                )
            },
            sortType = sortType
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Text(
            text = "Transaction History",
            fontFamily = fonts,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        DateRangeView(
            selectedStartDate = selectedStartDate,
            selectedEndDate = selectedEndDate,
            onClick = {
                scope.launch {
                    dateRangeSheetState.show()
                }
            }
        )
        Divider()
        FilterSection(
            sortFilterItems = sortItemList,
            onTypeClick = {
                sortType = "transaction_type"
                scope.launch {
                    filterSheetState.show()
                }
            },
            onCategoryClick = {
                sortType = "category"
                scope.launch {
                    filterSheetState.show()
                }
            }
        )
    }
}


