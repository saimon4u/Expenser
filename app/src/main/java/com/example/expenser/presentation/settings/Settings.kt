package com.example.expenser.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expenser.domain.model.Currency
import com.example.expenser.presentation.components.CurrencyBox
import com.example.expenser.presentation.components.CurrencyListSheet
import com.example.expenser.presentation.components.Divider
import com.example.expenser.presentation.dashboard.components.CustomSnackbar
import com.example.expenser.presentation.settings.components.CategoryBox
import com.example.expenser.presentation.settings.components.Header
import com.example.expenser.presentation.sign_in.UserData
import com.example.expenser.ui.theme.Emerald500
import com.example.expenser.ui.theme.Red500
import com.example.expenser.util.TransactionType
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    userData: UserData?,
){
    val scrollState = rememberScrollState()
    val settingViewModel = hiltViewModel<SettingViewModel>()
    val settingState = settingViewModel.settingState.collectAsState().value

    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
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

    LaunchedEffect(key1 = Unit) {
        settingViewModel.getUserSettings(userData?.userId!!)
        settingViewModel.getCategoriesByType(userData.userId, TransactionType.Income)
        settingViewModel.getCategoriesByType(userData.userId, TransactionType.Expense)
    }

    var selectedCurrency by remember {
        mutableStateOf(
            settingState.userSettings?.currency ?: Currency("Taka", "৳")
        )
    }

    LaunchedEffect(
        settingState.showSnackbar
    ) {
        if(settingState.showSnackbar){
            scope.launch {
                if(snackbarHostState.currentSnackbarData != null) snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar("", "", duration = SnackbarDuration.Short)
            }
            settingViewModel.updateSnackbarState()
        }
    }

    Scaffold {paddingValues ->
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
                    settingViewModel.updateUserSettings(userData!!.userId, item)
                }
            )
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .verticalScroll(scrollState)
        ) {
            Header(
                modifier = Modifier
                    .fillMaxWidth()
            )

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

            CategoryBox(
                categoryList = settingState.incomeCategoryList,
                bgColor = Emerald500,
                iconTint = Color.Green,
                icon = Icons.Default.TrendingUp,
                title = "Income categories",
                type = "income",
                transactionType = TransactionType.Income,
                showSnackbar = settingViewModel::showSnackbar,
                onCategoryCreate = settingViewModel::onCategoryCreate,
                userId = userData!!.userId,
                onCategoryDelete = settingViewModel::onCategoryDelete
            )

            Spacer(modifier = Modifier.height(16.dp))

            CategoryBox(
                categoryList = settingState.expenseCategoryList,
                bgColor = Red500,
                iconTint = Color.Red,
                icon = Icons.Default.TrendingDown,
                title = "Expense categories",
                type = "expense",
                transactionType = TransactionType.Expense,
                showSnackbar = settingViewModel::showSnackbar,
                onCategoryCreate = settingViewModel::onCategoryCreate,
                userId = userData.userId,
                onCategoryDelete = settingViewModel::onCategoryDelete
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
                message = settingState.errorMessage,
            )
        }
    }
}