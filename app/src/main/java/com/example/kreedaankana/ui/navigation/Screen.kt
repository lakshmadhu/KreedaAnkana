package com.example.kreedaankana.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector? = null
) {
    // Auth
    object Login : Screen("login", "Login")
    object Signup : Screen("signup", "Signup")

    // Main app
    object Home : Screen(
        "home",
        "Home",
        Icons.Default.Home
    )

    object Calendar : Screen(
        "calendar",
        "Calendar",
        Icons.Default.CalendarMonth
    )

    object Booking : Screen(
        "booking",
        "Book Slot",
        Icons.Default.AddCircle
    )

    object ChallengeBoard : Screen(
        "challenge",
        "Challenges",
        Icons.Default.Groups
    )

    object ScoreWall : Screen(
        "scores",
        "Scores",
        Icons.Default.Leaderboard
    )

    object Profile : Screen(
        "profile",
        "Profile",
        Icons.Default.Person
    )
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Calendar,
    Screen.Booking,
    Screen.ChallengeBoard,
    Screen.ScoreWall,
    Screen.Profile
)