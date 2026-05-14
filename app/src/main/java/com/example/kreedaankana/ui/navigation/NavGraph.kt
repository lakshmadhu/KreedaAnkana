package com.example.kreedaankana.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kreedaankana.KreedaAnkanaApplication
import com.example.kreedaankana.data.repository.BookingRepository
import com.example.kreedaankana.ui.screens.*
import com.example.kreedaankana.ui.viewmodel.BookingViewModel
import com.example.kreedaankana.ui.viewmodel.BookingViewModelFactory

@Composable
fun NavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val application = context.applicationContext as KreedaAnkanaApplication
    val repository = BookingRepository(application.database.bookingDao())
    val bookingViewModel: BookingViewModel = viewModel(factory = BookingViewModelFactory(repository))

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            // CHANGE 1: Set Signup as the first screen
            startDestination = Screen.Signup.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(400)) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(400)) + fadeOut() },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(400)) + fadeIn() },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(400)) + fadeOut() }
        ) {
            // SIGNUP (First screen)
            composable(Screen.Signup.route) {
                SignupScreen(navController)
            }

            // LOGIN
            composable(Screen.Login.route) {
                LoginScreen(navController)
            }

            // HOME
            composable(
                route = Screen.Home.route + "/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                HomeScreen(navController = navController, userId = userId)
            }

            // CALENDAR (Added userId support to prevent errors)
            composable(
                route = Screen.Calendar.route + "/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                CalendarScreen(viewModel = bookingViewModel)
            }

            // BOOKING (Added userId support to prevent errors)
            composable(
                route = Screen.Booking.route + "/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                BookingScreen(viewModel = bookingViewModel, userId = userId)
            }

            // CHALLENGE BOARD
            composable(
                route = Screen.ChallengeBoard.route + "/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                ChallengeScreen(navController = navController, userId = userId)
            }

            // SCORE WALL
            composable(Screen.ScoreWall.route) {
                ScoreWallScreen()
            }

            // TEAM PROFILE
            composable(
                route = Screen.Profile.route + "/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                TeamProfileScreen(navController = navController, userId = userId)
            }
        }
    }
}