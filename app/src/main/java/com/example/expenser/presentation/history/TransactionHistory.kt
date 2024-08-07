package com.example.expenser.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expenser.domain.model.Category
import com.example.expenser.presentation.components.CustomDateRangePicker
import com.example.expenser.presentation.components.DateRangeView
import com.example.expenser.presentation.components.Divider
import com.example.expenser.presentation.dashboard.components.CustomSnackbar
import com.example.expenser.presentation.history.components.FilterSection
import com.example.expenser.presentation.history.components.SortBottomSheet
import com.example.expenser.presentation.history.components.TransactionList
import com.example.expenser.presentation.sign_in.UserData
import com.example.expenser.ui.theme.fonts
import com.example.expenser.util.SortFilterItem
import com.example.expenser.util.TransactionType
import com.example.expenser.util.convertDateToMillis
import com.example.expenser.util.convertMillisToDate
import com.example.expenser.util.debug
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
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
    val snackbarHostState = remember {
        SnackbarHostState()
    }

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
        historyState.showSnackbar
    ) {
        if(historyState.showSnackbar){
            scope.launch {
                if(snackbarHostState.currentSnackbarData != null) snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar("", "", duration = SnackbarDuration.Short)
            }
            historyViewModel.updateSnackbarState()
        }
    }

    LaunchedEffect(key1 = selectedStartDate, key2 = selectedEndDate) {
        historyViewModel.getAllTransaction(
            userData!!.userId,
            selectedStartDate.convertDateToMillis(),
            selectedEndDate.convertDateToMillis()
        )
    }

    LaunchedEffect(key1 = rangeSelected) {
        if(rangeSelected){
            selectedStartDate = dateRangePickerState.selectedStartDateMillis?.convertMillisToDate() ?: selectedStartDate
            selectedEndDate = dateRangePickerState.selectedEndDateMillis?.convertMillisToDate() ?: selectedEndDate
            rangeSelected = false
        }
    }


    LaunchedEffect(key1 = Unit) {
        historyViewModel.getCategoriesByType(userData!!.userId, TransactionType.Income)
        historyViewModel.getCategoriesByType(userData.userId, TransactionType.Expense)
        historyViewModel.getUserSettings(userData.userId)
    }

    LaunchedEffect(key1 = historyState.categoryFetchingError) {
        if(historyState.categoryFetchingError) historyViewModel.getCategoriesByType(userData!!.userId, historyState.categoryErrorType)
    }


    var sortType by remember { mutableStateOf("category") }

    var selectedTypes by remember {
        mutableStateOf(
            historyState.sortList.filter { it.type == "transaction_type" && it.isSelected }
        )
    }

    var selectedCategory by remember {
        mutableStateOf(
            historyState.sortList.filter { it.type == "category" && it.isSelected }
        )
    }

    LaunchedEffect(key1 = selectedTypes, key2 = historyState.isCategoryFetching, key3 = selectedCategory) {
        var categories: List<Category> = emptyList()
        when(selectedTypes.size){
            0 , 2-> categories = historyState.incomeCategoryList + historyState.expenseCategoryList
            1 -> {
                when(selectedTypes[0].name){
                    "income" -> categories = historyState.incomeCategoryList
                    "expense" -> categories = historyState.expenseCategoryList
                }
            }
        }
        historyViewModel.updateSortList(
            selectedTypes,
            categories
        )
        historyViewModel.filterList(selectedTypes, selectedCategory)
    }

    LaunchedEffect(key1 = historyState.transactionList) {
        historyViewModel.filterList(selectedTypes, selectedCategory)
    }





    Scaffold {_ ->
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



        if(filterSheetState.currentValue != SheetValue.Hidden){
            SortBottomSheet(
                sheetState = filterSheetState,
                onDismiss = {
                    selectedTypes = historyState.sortList.filter { it.type == "transaction_type" && it.isSelected }
                    selectedCategory = historyState.sortList.filter { it.type == "category" && it.isSelected }
                    scope.launch {
                        filterSheetState.hide()
                    }
                },
                sortFilterItems = historyState.sortList,
                onItemClick = historyViewModel::onSortItemClick,
                sortType = sortType,
                isLoading = historyState.isCategoryFetching || historyState.sortList.size == 2,
                onClearClick = {
                    selectedTypes = historyState.sortList.filter { it.type == "transaction_type" && it.isSelected }
                    selectedCategory = historyState.sortList.filter { it.type == "category" && it.isSelected }
                    scope.launch {
                        filterSheetState.hide()
                    }
                }
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
                sortFilterItems = historyState.sortList,
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

            Spacer(modifier = Modifier.height(16.dp))

            TransactionList(
                list = historyState.filteredList,
                categories = historyState.incomeCategoryList + historyState.expenseCategoryList,
                isLoading = historyState.isTransactionFetching,
                moneyIcon = historyState.userSettings?.currency?.icon,
                onDelete = {
                    historyViewModel.onTransactionDelete(
                        userId = historyState.userSettings!!.userId,
                        transaction = it
                    )
                }
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
                message = historyState.snackbarMessage,
            )
        }
    }
}


