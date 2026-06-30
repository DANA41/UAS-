package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.AppDatabase
import com.example.repository.EcoRepository
import com.example.ui.AppViewModel
import com.example.ui.navigation.Screen
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Room Database & Repository
        val database = AppDatabase.getDatabase(this)
        val repository = EcoRepository(database.ecoDao())

        // Initialize Shared ViewModel
        val viewModel = ViewModelProvider(
            this,
            AppViewModel.Factory(application, repository)
        )[AppViewModel::class.java]

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val userProfile by viewModel.userProfile.collectAsState()
                
                // Determine initial log state
                val isLoggedIn = userProfile != null

                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 1. SPLASH SCREEN
                    composable(Screen.Splash.route) {
                        SplashScreen(
                            isLoggedIn = isLoggedIn,
                            onNavigateNext = { route ->
                                navController.navigate(route) {
                                    popUpTo(Screen.Splash.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    // 2. LOGIN SCREEN
                    composable(Screen.Login.route) {
                        LoginScreen(
                            onLoginClick = { name, phone, city ->
                                viewModel.login(name, phone, city)
                                navController.navigate(Screen.Main.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    // 3. MAIN CONTAINER (Beranda, Kalkulator, Komunitas, Saya)
                    composable(Screen.Main.route) {
                        MainContainerScreen(
                            viewModel = viewModel,
                            onNavigate = { route ->
                                navController.navigate(route)
                            }
                        )
                    }

                    // 4. DETAIL APA ITU EE & MANFAAT
                    composable(Screen.DetailEe.route) {
                        DetailEeScreen(
                            initialTab = 0,
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    // 5. CARA MEMBUAT STEPS
                    composable(Screen.HowTo.route) {
                        DetailEeScreen(
                            initialTab = 1,
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    // 6. SENSOR CAHAYA
                    composable(Screen.LightSensor.route) {
                        LightSensorScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    // 7. LOKASI ANGGOTA (PETA)
                    composable(Screen.Map.route) {
                        MapScreen(
                            viewModel = viewModel,
                            onBackClick = { navController.popBackStack() },
                            onMemberProfileClick = { memberId ->
                                navController.navigate(Screen.MemberProfile.createRoute(memberId))
                            }
                        )
                    }

                    // 8. PROFIL ANGGOTA
                    composable(
                        route = Screen.MemberProfile.route,
                        arguments = listOf(navArgument("memberId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val memberId = backStackEntry.arguments?.getString("memberId") ?: ""
                        MemberProfileScreen(
                            memberId = memberId,
                            viewModel = viewModel,
                            onBackClick = { navController.popBackStack() },
                            onChatClick = { id ->
                                navController.navigate(Screen.Chat.createRoute(id))
                            }
                        )
                    }

                    // 9. CHAT SCREEN
                    composable(
                        route = Screen.Chat.route,
                        arguments = listOf(navArgument("memberId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val memberId = backStackEntry.arguments?.getString("memberId") ?: ""
                        ChatScreen(
                            memberId = memberId,
                            viewModel = viewModel,
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    // 10. ABOUT SCREEN (Tentang Aplikasi)
                    composable(Screen.About.route) {
                        AboutScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
