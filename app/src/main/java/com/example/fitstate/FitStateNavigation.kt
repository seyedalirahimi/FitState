package com.example.fitstate

sealed class Screen(val route: String) {
    data object AddWeigh : Screen("addWeight")
    data object LogBook : Screen("logBook")
    data object Summary : Screen("summary")
}