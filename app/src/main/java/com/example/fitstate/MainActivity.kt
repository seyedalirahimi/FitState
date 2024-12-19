package com.example.fitstate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fitstate.ui.screen.AddBodyStateDialog
import com.example.fitstate.ui.screen.SummaryScreen
import com.example.fitstate.ui.screen.SummaryScreenRoot
import com.example.fitstate.ui.theme.FitStateTheme
import com.example.fitstate.ui.viewModel.AddBodyStateAction
import com.example.fitstate.ui.viewModel.AddBodyStateViewModel
import com.example.fitstate.ui.viewModel.SummaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        var isSplashScreenVisible = true
        CoroutineScope(Dispatchers.Main).launch {
            delay(300)
            isSplashScreenVisible = false
        }
        splashScreen.setKeepOnScreenCondition { isSplashScreenVisible }
        enableEdgeToEdge()

        setContent {
            FitStateTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    var showAddDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            FitStateBottomNavigation(
                selectedItem = selectedItem,
                onItemSelected = { index ->
                    if (index == 1) showAddDialog = true else selectedItem = index
                }
            )
        }
    ) { padding ->
        ContentScreen(
            selectedItem = selectedItem,
            padding = padding,
            onDialogDismiss = { showAddDialog = false },
            showAddDialog = showAddDialog
        )
    }
}

@Composable
fun FitStateBottomNavigation(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar {
        bottomNavigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { onItemSelected(index) },
                label = { if (item.showTitle) Text(item.title) },
                icon = {
                    val iconToDisplay =
                        if (selectedItem == index) item.selectedIcon else item.unSelectedIcon
                    Icon(iconToDisplay, contentDescription = item.title)
                }
            )
        }
    }
}

@Composable
fun ContentScreen(
    selectedItem: Int,
    padding: PaddingValues,
    onDialogDismiss: () -> Unit,
    showAddDialog: Boolean
) {
    when (selectedItem) {
        0 -> {
            val summaryViewModel: SummaryViewModel = hiltViewModel()
            SummaryScreenRoot(Modifier, summaryViewModel)
        }

        2 -> {
            Text("Settings Page", Modifier.padding(padding))
        }
    }

    if (showAddDialog) {
        val addBodyStateViewModel: AddBodyStateViewModel = hiltViewModel()
        AddBodyStateDialog(
            onDismiss = { onDialogDismiss() },
            onSave = { weight, note ->
                onDialogDismiss()
                addBodyStateViewModel.onAction(AddBodyStateAction.OnSave(weight, note))
            }
        )
    }
}

val bottomNavigationItems = listOf(
    BottomNavigationItem(
        title = "Summary", selectedIcon = Icons.Filled.Home, unSelectedIcon = Icons.Outlined.Home
    ),
    BottomNavigationItem(
        showTitle = false,
        title = "Add",
        selectedIcon = Icons.Filled.AddCircle,
        unSelectedIcon = Icons.Filled.AddCircle
    ),
    BottomNavigationItem(
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unSelectedIcon = Icons.Outlined.Settings
    )
)

data class BottomNavigationItem(
    val showTitle: Boolean = true,
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector
)
