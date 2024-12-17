package com.example.fitstate

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fitstate.ui.screen.AddBodyStateDialog
import com.example.fitstate.ui.screen.BodyStateScreenRoot
import com.example.fitstate.ui.theme.FitStateTheme
import com.example.fitstate.ui.viewModel.AddBodyStateAction
import com.example.fitstate.ui.viewModel.AddBodyStateViewModel
import com.example.fitstate.ui.viewModel.BodyStateViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        var isSplashScreenVisible = true
        splashScreen.setKeepOnScreenCondition { isSplashScreenVisible }
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            isSplashScreenVisible = false
        }

        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                var selectedItem by rememberSaveable { mutableIntStateOf(0) }
                var showAddDialog by rememberSaveable { mutableStateOf(false) }
                Scaffold(bottomBar = {
                    NavigationBar {
                        bottomNavigationItems.forEachIndexed { index, item ->
                            NavigationBarItem(selected = selectedItem == index, onClick = {
                                if (index == 1) {
                                    showAddDialog = true
                                } else {
                                    selectedItem = index
                                }
                            }, label = {
                                if (item.showTitle && item.title.isNotBlank()) {
                                    Text(item.title)
                                }
                            }, icon = {
                                val iconToDisplay =
                                    if (selectedItem == index) item.selectedIcon else item.unSelectedIcon
                                Icon(iconToDisplay, contentDescription = item.title)
                            })
                        }
                    }

                }) { padding ->
                    when (selectedItem) {
                        0 -> {
                            val bodyStateViewModel: BodyStateViewModel = hiltViewModel()
                            BodyStateScreenRoot(
                                Modifier.padding(padding), bodyStateViewModel
                            )
                        }

                        2 -> {
                            Text("Setting Page")
                        }
                    }

                    if (showAddDialog) {
                        val addBodyStateViewModel: AddBodyStateViewModel = hiltViewModel()

                        AddBodyStateDialog(onDismiss = {
                            showAddDialog = false
                            Log.i("x1x1x1", "onDismissRequest: ")
                        }, onSave = { weight, note ->
                            showAddDialog = false
                            addBodyStateViewModel.onAction(
                                AddBodyStateAction.OnSave(
                                    weight, note
                                )
                            )

                        })


                    }


                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FitStateTheme {
        Greeting("Android")
    }
}


val bottomNavigationItems = listOf(
    BottomNavigationItem(
        title = "Summary", selectedIcon = Icons.Filled.Home, unSelectedIcon = Icons.Outlined.Home
    ), BottomNavigationItem(
        showTitle = false,
        selectable = false,
        title = "Add",
        selectedIcon = Icons.Filled.AddCircle,
        unSelectedIcon = Icons.Filled.AddCircle
    ), BottomNavigationItem(
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unSelectedIcon = Icons.Outlined.Settings
    )

)

data class BottomNavigationItem(
    val showTitle: Boolean = true,
    val selectable: Boolean = true,
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector
)