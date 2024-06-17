package com.example.expenser.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Copyright
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.expenser.ui.theme.Emerald500
import com.example.expenser.ui.theme.fonts

@Composable
fun CustomSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    message: String,
    color: Color,
){
    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val snackbar = createRef()

        SnackbarHost(
            modifier = modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .constrainAs(snackbar) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            hostState = snackbarHostState,
            snackbar = {
                Snackbar(
                    shape = RoundedCornerShape(15.dp),
                    containerColor = color,
                    contentColor = Color.Black,
                    action = {
                        Icon(
                            imageVector = Icons.Rounded.Cancel,
                            contentDescription = null,
                            modifier = modifier
                                .padding(10.dp)
                                .size(25.dp)
                                .clip(CircleShape)
                                .clickable {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                }
                        )
                    }
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Copyright,
                            contentDescription = null,
                            modifier = modifier
                                .padding(10.dp)
                                .size(25.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = message,
                            fontFamily = fonts,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        )
    }
}