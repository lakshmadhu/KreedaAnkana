package com.example.kreedaankana.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.kreedaankana.ui.navigation.Screen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

@Composable
fun BottomNavigationBar(navController: NavController, userId: String) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        Screen.Home to "Home",
        Screen.Calendar to "Calendar",
        Screen.Booking to "Book",
        Screen.ChallengeBoard to "Arena",
        Screen.ScoreWall to "Scores",
        Screen.Profile to "Profile"
    )

    NavigationBar(
        containerColor = Color(0xFF0B1020),
        tonalElevation = 8.dp
    ) {
        items.forEach { (screen, label) ->
            // Matches routes even if they have the /userId suffix
            val isSelected = currentRoute?.startsWith(screen.route) == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        val routeWithId = listOf(
                            Screen.Home.route,
                            Screen.Calendar.route,
                            Screen.Booking.route,
                            Screen.ChallengeBoard.route,
                            Screen.Profile.route
                        )

                        // FIX: Safely grab the active dynamic user ID parameter from the backstack argument map
                        val activeUserId = navBackStackEntry?.arguments?.getString("userId") ?: userId

                        val finalRoute = if (screen.route in routeWithId) {
                            "${screen.route}/$activeUserId"
                        } else {
                            screen.route // For ScoreWall or other static screens
                        }

                        navController.navigate(finalRoute) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                alwaysShowLabel = false,
                label = {
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color(0xFF00F2FF) else Color.Gray
                    )
                },
                icon = {
                    val icon = when(label) {
                        "Home" -> Icons.Default.Home
                        "Calendar" -> Icons.Default.CalendarMonth
                        "Book" -> Icons.Default.AddCircle
                        "Arena" -> Icons.Default.Groups
                        "Scores" -> Icons.Default.Leaderboard
                        else -> Icons.Default.Person
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (isSelected) Color(0xFF00F2FF) else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0xFF00F2FF).copy(alpha = 0.1f)
                )
            )
        }
    }
}