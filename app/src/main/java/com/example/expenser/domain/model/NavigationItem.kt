package com.example.expenser.domain.model
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Subscriptions
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationItem(
    val title: String,
    val icon: ImageVector
) {
    Dashboard(
        icon = Icons.Rounded.Dashboard,
        title = "Dashboard"
    ),
    History(
        icon = Icons.Rounded.History,
        title = "Transaction History"
    ),
    Settings(
        icon = Icons.Rounded.Settings,
        title = "Settings"
    ),
    Sign_Out(
        icon = Icons.Rounded.Logout,
        title = "Sign Out"
    ),
}