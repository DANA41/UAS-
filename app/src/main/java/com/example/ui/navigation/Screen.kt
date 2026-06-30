package com.example.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Main : Screen("main") // Holds bottom navigation
    object DetailEe : Screen("detail_ee")
    object HowTo : Screen("how_to")
    object LightSensor : Screen("light_sensor")
    object Map : Screen("map")
    object MemberProfile : Screen("member_profile/{memberId}") {
        fun createRoute(memberId: String) = "member_profile/$memberId"
    }
    object Chat : Screen("chat/{memberId}") {
        fun createRoute(memberId: String) = "chat/$memberId"
    }
    object About : Screen("about")
}
