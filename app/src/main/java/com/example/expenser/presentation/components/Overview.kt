package com.example.expenser.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expenser.ui.theme.fonts
import com.example.expenser.util.convertMillisToDate
import com.example.expenser.util.debug
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Overview(
    modifier: Modifier = Modifier,
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
    }
}