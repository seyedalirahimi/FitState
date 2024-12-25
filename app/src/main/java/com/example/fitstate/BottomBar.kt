package com.example.fitstate

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import java.util.Locale

data class BottomNavigationItem(
    val screen: Screen,
    val showTitle: Boolean = true,
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector
)

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val items = listOf(
        BottomNavigationItem(
            screen = Screen.Summary,
            title = "Summary",
            selectedIcon = Icons.Filled.Home,
            unSelectedIcon = Icons.Outlined.Home
        ), BottomNavigationItem(
            screen = Screen.AddWeigh,
            showTitle = false,
            title = "Add",
            selectedIcon = Icons.Filled.AddCircle,
            unSelectedIcon = Icons.Filled.AddCircle
        ), BottomNavigationItem(
            screen = Screen.LogBook,
            title = "LogBook",
            selectedIcon = Icons.Filled.Info,
            unSelectedIcon = Icons.Outlined.Info
        )
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->
            val selected =
                currentDestination?.hierarchy?.any { it.route == item.screen.route } == true
            NavigationBarItem(
                label = {
                    Text(item.screen.route.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    })
                },
                icon = {
                    val iconToDisplay = if (selected) item.selectedIcon else item.unSelectedIcon
                    Icon(iconToDisplay, contentDescription = item.title)
                },
                selected = selected,
                onClick = {
                    navController.navigate(item.screen.route) {
                        launchSingleTop = true
                        restoreState = true
                    }

                },
            )
        }

    }
}