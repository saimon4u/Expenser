package com.example.expenser.presentation

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.expenser.domain.model.NavigationItem
import com.example.expenser.presentation.components.CustomDrawer
import com.example.expenser.presentation.components.DashboardContent
import com.example.expenser.presentation.components.SettingsContent
import com.example.expenser.presentation.components.TransactionHistory
import com.example.expenser.presentation.sign_in.UserData
import com.example.expenser.presentation.util.coloredShadow
import kotlin.math.roundToInt

@Composable
fun MainScreen(
    onSignOut: () -> Unit,
    userData: UserData?
) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    val drawerState = mainViewModel.drawerState.collectAsState().value
    val selectedNavigationItem = mainViewModel.selectedNavigationItem.collectAsState().value

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density

    val screenWidth = remember {
        derivedStateOf { (configuration.screenWidthDp * density).roundToInt() }
    }
    val offsetValue by remember { derivedStateOf { (screenWidth.value / 4.5).dp } }
    val animatedOffset by animateDpAsState(
        targetValue = if (drawerState.isOpened()) offsetValue else 0.dp,
        label = "Animated Offset"
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpened()) 0.9f else 1f,
        label = "Animated Scale"
    )

    BackHandler(enabled = drawerState.isOpened()) {
        mainViewModel.handleBackPress()
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        CustomDrawer(
            selectedNavigationItem = selectedNavigationItem,
            onNavigationItemClick = mainViewModel::onNavigationItemClick,
            onCloseClick = mainViewModel::handleBackPress
        )
        Content(
            modifier = Modifier
                .offset(x = animatedOffset)
                .scale(scale = animatedScale)
                .coloredShadow(
                    color = Color.Black,
                    alpha = 0.1f,
                    shadowRadius = 50.dp
                ),
            drawerState = drawerState,
            onDrawerClick = mainViewModel::onDrawerClick,
            selectedItem = selectedNavigationItem,
            onSignOut = onSignOut,
            userData = userData
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(
    modifier: Modifier = Modifier,
    drawerState: CustomDrawerState,
    onDrawerClick: (CustomDrawerState) -> Unit,
    selectedItem: NavigationItem,
    onSignOut: () -> Unit,
    userData: UserData?
) {
    Scaffold(
        modifier = modifier
            .clickable(enabled = drawerState == CustomDrawerState.Opened) {
                onDrawerClick(CustomDrawerState.Closed)
            },
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(.7f),
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Savings,
                                contentDescription = "Piggy Bank",
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = "Expenser")
                        }
                        AsyncImage(
                            model = userData?.profilePictureUrl,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onDrawerClick(drawerState.opposite()) }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu Icon"
                        )
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when(selectedItem){
                NavigationItem.Dashboard -> DashboardContent()
                NavigationItem.History -> TransactionHistory()
                NavigationItem.Settings -> SettingsContent()
                NavigationItem.Sign_Out -> onSignOut()
            }
        }
    }
}