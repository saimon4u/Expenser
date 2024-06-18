package com.example.expenser.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.expenser.ui.theme.Emerald500
import com.example.expenser.util.debug
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar


@Composable
fun TransactionHistory() {
    Overview(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleDatePickerView(){
    val state = rememberDateRangePickerState()
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    if(bottomSheetState.currentValue != SheetValue.Hidden){
        ModalBottomSheet(
            onDismissRequest = {
                debug("Dismissed")
            },
            sheetState = bottomSheetState,
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp)
                        .background(Color.White)
                ) {
                    DateRangePickerSample(state)
                    Button(
                        onClick = {
                            debug(
                                state.selectedStartDateMillis.toString() + "   " + state.selectedEndDateMillis.toString()
                            )
                            coroutineScope.launch {
                                bottomSheetState.hide()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 16.dp)
                    ) {
                        Text("Done", color = Color.White)
                    }
                }
            },
            scrimColor = Color.Black.copy(alpha = 0.5f),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        )
    }

    Column {
        Button(onClick = {
            coroutineScope.launch {
                bottomSheetState.show()
            }
        }, modifier = Modifier.padding(16.dp)) {
            Text(text = "Open Date Picker")
        }
        Text(text = "Start Date:" + if(state.selectedStartDateMillis!=null) state.selectedStartDateMillis?.let { getFormattedDate(it) } else "")
        Text(text = "End Date:" + if(state.selectedEndDateMillis!=null) state.selectedEndDateMillis?.let { getFormattedDate(it) } else "")
    }
}
fun getFormattedDate(timeInMillis: Long): String{
    val calender = Calendar.getInstance()
    calender.timeInMillis = timeInMillis
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    return dateFormat.format(calender.timeInMillis)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerSample(state: DateRangePickerState){
    DateRangePicker(
        state = state,
        modifier = Modifier,
        dateFormatter = DatePickerFormatter("yy MM dd", "yy MM dd", "yy MM dd"),
        title = {
            Text(text = "Select date range to assign the chart", modifier = Modifier
                .padding(16.dp))
        },
        headline = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    Modifier.weight(1f)
                ) {
                    (if(state.selectedStartDateMillis!=null) state.selectedStartDateMillis?.let { getFormattedDate(it) } else "Start Date")?.let { Text(text = it) }
                }
                Box(
                    Modifier.weight(1f)
                ) {
                    (if(state.selectedEndDateMillis!=null) state.selectedEndDateMillis?.let { getFormattedDate(it) } else "End Date")?.let { Text(text = it) }
                }
                Box(
                    Modifier.weight(0.2f)
                ) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = "Okk")
                }

            }
        },
        showModeToggle = false,
//        colors = DatePickerDefaults.colors(
//            containerColor = Color.Blue,
//            titleContentColor = Color.Black,
//            headlineContentColor = Color.Black,
//            weekdayContentColor = Color.Black,
//            subheadContentColor = Color.Black,
//            yearContentColor = Color.Green,
//            currentYearContentColor = Color.Red,
//            selectedYearContainerColor = Color.Red,
//            disabledDayContentColor = Color.Gray,
//            todayDateBorderColor = Color.Blue,
//            dayInSelectionRangeContainerColor = Color.LightGray,
//            dayInSelectionRangeContentColor = Color.White,
//            selectedDayContainerColor = Color.Black
//        )
    )
}







//    Box(
//        modifier = Modifier
//            .fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ){
//        Overview(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(100.dp)
//                .padding(16.dp)
//        )
//    }

//    val snackbarHostState = remember {
//        SnackbarHostState()
//    }
//
//    val scope = rememberCoroutineScope()
//    Scaffold {padd->
//        val p = padd
//        Column(
//            modifier = Modifier
//                .fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Button(
//                onClick = {
//                    scope.launch {
//                        snackbarHostState.showSnackbar("", "", duration = SnackbarDuration.Short)
//                    }
//                }
//            ) {
//                Text(text = "Click")
//            }
//        }
//        CustomSnackbar(
//            snackbarHostState = snackbarHostState,
//            modifier = Modifier,
//            message = "Transaction Added",
//        )
//    }