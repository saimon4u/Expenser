package com.example.expenser.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expenser.ui.theme.fonts
import com.example.expenser.util.convertMillisToDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActualDateRangePicker(state: DateRangePickerState){
    DateRangePicker(
        state = state,
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        dateFormatter = DatePickerFormatter("yy MM dd", "yy MM dd", "yy MM dd"),
        title = {
            Text(
                text = "Select date range to display balance",
                modifier = Modifier
                    .padding(16.dp),
                fontFamily = fonts,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
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
                    (if(state.selectedStartDateMillis!=null) state.selectedStartDateMillis?.convertMillisToDate() else "Start Date")?.let {
                        Text(
                            text = it,
                            fontFamily = fonts,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                Box(
                    Modifier.weight(1f)
                ) {
                    (if(state.selectedEndDateMillis!=null) state.selectedEndDateMillis?.convertMillisToDate() else "End Date")?.let {
                        Text(
                            text = it,
                            fontFamily = fonts,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        },
        showModeToggle = false,
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            headlineContentColor = MaterialTheme.colorScheme.onBackground,
            weekdayContentColor = MaterialTheme.colorScheme.onBackground,
            subheadContentColor = MaterialTheme.colorScheme.onBackground,
            yearContentColor = MaterialTheme.colorScheme.onBackground,
            currentYearContentColor = MaterialTheme.colorScheme.onBackground,
            selectedYearContainerColor = Color.Green,
            disabledDayContentColor = MaterialTheme.colorScheme.surface,
            todayDateBorderColor = MaterialTheme.colorScheme.primary,
            dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.secondary,
            dayInSelectionRangeContentColor = MaterialTheme.colorScheme.onTertiary,
            selectedDayContainerColor = MaterialTheme.colorScheme.tertiary
        )
    )
}