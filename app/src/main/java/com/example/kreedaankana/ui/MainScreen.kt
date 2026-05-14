package com.example.kreedaankana.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kreedaankana.ui.components.BottomNavigationBar
import com.example.kreedaankana.ui.navigation.NavGraph
import com.example.kreedaankana.ui.navigation.Screen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // This grabs the phone number (userId) from the current screen we are on
    // so we can give it to the bottom bar
    val userId = navBackStackEntry?.arguments?.getString("userId") ?: ""

    val showBottomBar = currentRoute?.let { route ->
        !route.contains(Screen.Login.route) && !route.contains(Screen.Signup.route)
    } ?: false

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                // Pass the userId to the Bottom Bar so it doesn't lose it
                BottomNavigationBar(navController = navController, userId = userId)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            NavGraph(navController = navController)
        }
    }
}