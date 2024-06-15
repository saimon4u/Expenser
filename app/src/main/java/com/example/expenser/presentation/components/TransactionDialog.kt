package com.example.expenser.presentation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.expenser.domain.model.TransactionType
import com.example.expenser.domain.model.categoryList
import com.example.expenser.presentation.util.convertMillisToDate
import com.example.expenser.ui.theme.fonts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDialog(
    transactionType: TransactionType,
    onDismissRequest: () -> Unit
) {


    var descriptionVal by remember { mutableStateOf("") }
    var amountVal by remember { mutableDoubleStateOf(0.0) }
    var openDateDialog by remember { mutableStateOf(false) }
    var dateButtonText by remember { mutableStateOf("Select Date") }
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)

    var selectedCategory by remember { mutableStateOf(categoryList[0]) }

    if(openDateDialog){
        TransactionDatePicker(
            onDismissRequest = {
                openDateDialog = false
            },
            onConfirm = {
                openDateDialog = false
                dateButtonText = datePickerState.selectedDateMillis?.convertMillisToDate() ?: ""
            },
            datePickerState = datePickerState
        )
    }
    Dialog(
        onDismissRequest = onDismissRequest,
    ){
        Column(
            modifier = Modifier
                .size(300.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            DialogTitle(
                modifier = Modifier
                    .fillMaxWidth(),
                transactionType = transactionType
            )

            Spacer(modifier = Modifier.height(10.dp))

            FormTextField(
                text = descriptionVal,
                onValueChange = {
                    descriptionVal = it
                },
                label = "Description",
                placeholder = "Optional",
                maxLines = 2,
            )

            Spacer(modifier = Modifier.height(10.dp))

            FormTextField(
                text = if(amountVal == 0.0) "" else amountVal.toString(),
                onValueChange = {
                    amountVal = it.toDouble()
                },
                label = "Amount",
                placeholder = "Enter amount",
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Button(
                    onClick = { openDateDialog = true },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CalendarMonth,
                        contentDescription = "Select Date",
                        tint = MaterialTheme.colorScheme.onTertiary
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = dateButtonText,
                        fontFamily = fonts,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }


                CategoryDropDown(
                    selectedValue = selectedCategory,
                    options = categoryList,
                    label = "Category",
                    onValueChangedEvent = {
                        selectedCategory = it
                    }
                )
            }

        }
    }
}
