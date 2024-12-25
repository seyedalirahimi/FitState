package com.example.fitstate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.fitstate.ui.screen.AddWightDialogRoot
import com.example.fitstate.ui.screen.LogBookScreenRoot
import com.example.fitstate.ui.screen.SummaryScreenRoot
import com.example.fitstate.ui.theme.FitStateTheme
import com.example.fitstate.ui.viewModel.AddBodyStateViewModel
import com.example.fitstate.ui.viewModel.LogBookViewModel
import com.example.fitstate.ui.viewModel.SummaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                val navController = rememberNavController()
                Scaffold(bottomBar = { BottomBar(navController = navController) },
                    content = { innerPadding ->
                        FitStateNavGraph(
                            navController = navController, modifier = Modifier.padding(innerPadding)
                        )
                    })
            }
        }
    }
}