package com.example.needhands

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.needhands.ui.chat.JobChatScreen
import com.example.needhands.ui.chat.JobChatViewModel
import com.example.needhands.ui.recruiter.RecruiterHomeScreen
import com.example.needhands.ui.recruiter.RecruiterHomeViewModel
import com.example.needhands.ui.theme.NeedHandsTheme
import com.example.needhands.ui.welcome.WelcomeScreen
import com.example.needhands.ui.welcome.WelcomeViewModel
import com.example.needhands.ui.welcome.UserRole
import com.example.needhands.ui.worker.WorkerProfileScreen
import com.example.needhands.ui.worker.WorkerProfileViewModel
import com.example.needhands.ui.worker.WorkerHomeScreen
import com.example.needhands.ui.worker.WorkerHomeViewModel

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

data class ThemeToggleContext(
    val isDarkTheme: Boolean,
    val toggleTheme: () -> Unit
)

val LocalThemeToggle = compositionLocalOf<ThemeToggleContext> {
    error("No ThemeToggleContext provided")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }
            val themeToggle = ThemeToggleContext(
                isDarkTheme = isDarkTheme,
                toggleTheme = { isDarkTheme = !isDarkTheme }
            )

            CompositionLocalProvider(LocalThemeToggle provides themeToggle) {
                NeedHandsTheme(darkTheme = isDarkTheme) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            val welcomeViewModel: WelcomeViewModel = viewModel()
            WelcomeScreen(
                viewModel = welcomeViewModel,
                onAuthSuccess = { role ->
                    if (role == UserRole.RECRUITER) {
                        navController.navigate("recruiter_home") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    } else {
                        navController.navigate("worker_home") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    }
                }
            )
        }

        composable("recruiter_home") {
            val recruiterHomeViewModel: RecruiterHomeViewModel = viewModel()
            RecruiterHomeScreen(
                viewModel = recruiterHomeViewModel,
                onWorkerSelected = { worker ->
                    navController.navigate("job_chat/${worker.id}/recruiter")
                },
                onLogout = {
                    navController.navigate("welcome") {
                        popUpTo("welcome") { inclusive = true }
                    }
                },
                onSettingsClick = {
                    navController.navigate("settings")
                }
            )
        }

        composable("worker_home") {
            val workerHomeViewModel: WorkerHomeViewModel = viewModel()
            WorkerHomeScreen(
                viewModel = workerHomeViewModel,
                onEditProfileClick = {
                    navController.navigate("worker_profile")
                },
                onChatListClick = {
                    navController.navigate("worker_chat_list")
                },
                onLogout = {
                    navController.navigate("welcome") {
                        popUpTo("welcome") { inclusive = true }
                    }
                },
                onSettingsClick = {
                    navController.navigate("settings")
                }
            )
        }

        composable("settings") {
            val settingsViewModel: com.example.needhands.ui.settings.SettingsViewModel = viewModel()
            com.example.needhands.ui.settings.SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("worker_profile") {
            val workerProfileViewModel: WorkerProfileViewModel = viewModel()
            WorkerProfileScreen(
                viewModel = workerProfileViewModel,
                onNavigateBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate("welcome")
                    }
                }
            )
        }

        composable("worker_chat_list") {
            val workerChatListViewModel: com.example.needhands.ui.worker.WorkerChatListViewModel = viewModel()
            com.example.needhands.ui.worker.WorkerChatListScreen(
                viewModel = workerChatListViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRecruiterSelected = { recruiterId ->
                    navController.navigate("job_chat/$recruiterId/worker")
                }
            )
        }

        composable(
            route = "job_chat/{targetUserId}/{role}",
            arguments = listOf(
                navArgument("targetUserId") { type = NavType.StringType },
                navArgument("role") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val targetUserId = backStackEntry.arguments?.getString("targetUserId") ?: ""
            val role = backStackEntry.arguments?.getString("role") ?: "recruiter"
            val jobChatViewModel: JobChatViewModel = viewModel()
            
            // Initialize the viewmodel with the target user ID and role
            jobChatViewModel.initChat(targetUserId, role)

            JobChatScreen(
                viewModel = jobChatViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
