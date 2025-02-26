package com.guerra.tasksync.screen

sealed class Screen(val route:String){
    object SignInScreen:Screen("sign_in")
    object HomeScreen:Screen("home")
}