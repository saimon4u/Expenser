package com.example.expenser.domain.model
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Subscriptions
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationItem(
    val title: String,
    val icon: ImageVector
) {
    Home(
        icon = Icons.Rounded.Home,
        title = "Home"
    ),
    Profile(
        icon = Icons.Rounded.Person,
        title = "Profile"
    ),
    Premium(
        icon = Icons.Rounded.Subscriptions,
        title = "Premium"
    ),
    Settings(
        icon = Icons.Rounded.Settings,
        title = "Settings"
    )
}