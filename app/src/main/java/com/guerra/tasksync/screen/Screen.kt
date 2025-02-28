package com.guerra.tasksync.screen

sealed class Screen(val route:String){
    object InitialScreen:Screen("initial")
    object SignInScreen:Screen("sign_in")
    object SignUpScreen:Screen("sign_up")

    object MainScreen:Screen("main_screen")
    object HomeScreen:Screen("home")
    object TeamsScreen:Screen("teams")
    object SettingsScreen:Screen("settings")
    object NotificationsScreen:Screen("notifications")
}