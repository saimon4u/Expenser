package com.example.expenser.presentation.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expenser.domain.model.Category
import com.example.expenser.presentation.components.ActualDateRangePicker
import com.example.expenser.presentation.dashboard.DashboardState
import com.example.expenser.presentation.history.HistoryState
import com.example.expenser.ui.theme.fonts
import com.example.expenser.util.SortFilterItem
import com.example.expenser.util.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    sortFilterItems: List<SortFilterItem>,
    onItemClick: (SortFilterItem) -> Unit,
    sortType: String,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(sortFilterItems){
                            if(it.isSelected && it.type == sortType){
                                Text(
                                    text = it.name,
                                    fontFamily = fonts,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Light,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(sortFilterItems){
                            if(it.type == sortType){
                                Box(
                                    modifier = Modifier
                                        .clickable {
                                            onItemClick(it)
                                        }
                                ){
                                    FilterSheetItem(
                                        name = it.name.uppercase(),
                                        icon = if(it.isSelected) Icons.Default.Check else Icons.Default.CheckBoxOutlineBlank
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    )
}