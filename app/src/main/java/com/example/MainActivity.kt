package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.BridgeScreen
import com.example.ui.screens.ControllerScreen
import com.example.ui.screens.DiagnosticsScreen
import com.example.ui.screens.LayoutStudioScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.MurtazaShahJiTheme
import com.example.ui.theme.PS5Cyan
import com.example.ui.viewmodel.GamepadViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MurtazaShahJiTheme {
                MainGamepadApp()
            }
        }
    }
}

data class NavTabItem(
    val title: String,
    val icon: ImageVector
)

@Composable
fun MainGamepadApp() {
    val viewModel: GamepadViewModel = viewModel()
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        NavTabItem("Controller", Icons.Default.Gamepad),
        NavTabItem("Layout Studio", Icons.Default.Dashboard),
        NavTabItem("Diagnostics", Icons.Default.CompassCalibration),
        NavTabItem("Bridge", Icons.Default.Wifi),
        NavTabItem("Settings", Icons.Default.Settings)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF0B0F19),
                contentColor = PS5Cyan
            ) {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                tint = if (selectedTab == index) PS5Cyan else Color.Gray
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 10.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) PS5Cyan else Color.Gray
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color(0xFF1E293B)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> ControllerScreen(viewModel = viewModel, onNavigateToStudio = { selectedTab = 1 })
                1 -> LayoutStudioScreen(viewModel = viewModel)
                2 -> DiagnosticsScreen(viewModel = viewModel)
                3 -> BridgeScreen(viewModel = viewModel)
                4 -> SettingsScreen(viewModel = viewModel)
            }
        }
    }
}
