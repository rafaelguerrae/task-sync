package com.guerra.tasksync.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.guerra.tasksync.R
import com.guerra.tasksync.data.UserData
import com.guerra.tasksync.viewmodel.GoogleAuthUiClient
import com.guerra.tasksync.viewmodel.SignInViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class BottomNavigationItem(
    val title: String,
    val route: String,
    val selectedItem: ImageVector,
    val unselectedItem: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

@Composable
fun MainScreen(
    context: Context,
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
    coroutineScope: CoroutineScope
) {
    val bottomNavController = rememberNavController()

    val isDarkTheme = isSystemInDarkTheme()

    Scaffold(
        topBar = { TopBar(isDarkTheme) },
        bottomBar = { BottomNavigationBar(isDarkTheme, bottomNavController) }
    ) { padding ->

        NavHost(
            modifier = Modifier.padding(padding),
            navController = bottomNavController, startDestination = Screen.HomeScreen.route
        ) {
            composable(Screen.HomeScreen.route) {
                HomeScreen(
                    userData = googleAuthUiClient.getSignedInUser(),
                    onSignOut = {
                        coroutineScope.launch {
                            googleAuthUiClient.signOut()
                            Toast.makeText(
                                context,
                                "Signed out",
                                Toast.LENGTH_SHORT
                            ).show()

                            navController.navigate("initial") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
            composable(Screen.TeamsScreen.route) { TeamsScreen() }
            composable(Screen.NotificationsScreen.route) { NotificationsScreen() }
            composable(Screen.SettingsScreen.route) { SettingsScreen() }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(isDarkTheme: Boolean) {
    val logoResId =
        if (isDarkTheme) R.drawable.tasksync_nobg_white else R.drawable.tasksync_nobg_black
    TopAppBar(
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
            navigationIconContentColor = MaterialTheme.colorScheme.surface,
            actionIconContentColor = MaterialTheme.colorScheme.surface
        ),
        title = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(logoResId),
                    contentDescription = "TaskSync Logo",
                    modifier = Modifier
                        .size(45.dp)
                        .align(Alignment.Center)
                )
            }
        }
    )
}

@Composable
fun BottomNavigationBar(isDarkTheme: Boolean, bottomNavController: NavHostController) {
    val items by remember {
        mutableStateOf(
            listOf(
                BottomNavigationItem(
                    title = "Home",
                    route = Screen.HomeScreen.route,
                    selectedItem = Icons.Default.Home,
                    unselectedItem = Icons.Outlined.Home,
                    hasNews = false
                ),
                BottomNavigationItem(
                    title = "Teams",
                    route = Screen.TeamsScreen.route,
                    selectedItem = Icons.Default.Person,
                    unselectedItem = Icons.Outlined.Person,
                    hasNews = true
                ),
                BottomNavigationItem(
                    title = "Notifications",
                    route = Screen.NotificationsScreen.route,
                    selectedItem = Icons.Default.Notifications,
                    unselectedItem = Icons.Outlined.Notifications,
                    hasNews = false,
                    badgeCount = 4
                ),
                BottomNavigationItem(
                    title = "Settings",
                    route = Screen.SettingsScreen.route,
                    selectedItem = Icons.Default.Settings,
                    unselectedItem = Icons.Outlined.Settings,
                    hasNews = false
                )
            )
        )
    }

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = if (isDarkTheme) Color.White else Color.Black,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = if (isDarkTheme) Color.White else Color.Black
                ),
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    bottomNavController.navigate(item.route){
                        popUpTo(bottomNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = {
                    Text(text = item.title)
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badgeCount != null) {
                                Badge {
                                    Text(text = item.badgeCount.toString())

                                }
                            } else if (item.hasNews) {
                                Badge()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (index == selectedItemIndex) item.selectedItem else item.unselectedItem,
                            contentDescription = ""
                        )
                    }
                }
            )

        }
    }
}