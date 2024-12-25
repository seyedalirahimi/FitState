package com.example.fitstate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.fitstate.ui.screen.AddWightDialogRoot
import com.example.fitstate.ui.screen.LogBookScreenRoot
import com.example.fitstate.ui.screen.SummaryScreenRoot
import com.example.fitstate.ui.viewModel.AddBodyStateViewModel
import com.example.fitstate.ui.viewModel.LogBookViewModel
import com.example.fitstate.ui.viewModel.SummaryViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun FitStateNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = Screen.Summary.route,
) {

    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier
    ) {

        composable(Screen.Summary.route) {
            val viewModel: SummaryViewModel = hiltViewModel()
            SummaryScreenRoot(
                viewModel = viewModel
            )
        }

        composable(Screen.LogBook.route) {
            val viewModel: LogBookViewModel = hiltViewModel()
            LogBookScreenRoot(
                viewModel = viewModel
            )
        }

        dialog(Screen.AddWeigh.route) {
            val addBodyStateViewModel: AddBodyStateViewModel = hiltViewModel()
            AddWightDialogRoot(
                viewModel = addBodyStateViewModel,
                onDismissRequest = { navController.navigateUp() },
            )
        }

    }
}