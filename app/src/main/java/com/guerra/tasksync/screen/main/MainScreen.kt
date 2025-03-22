package com.guerra.tasksync.screen.main

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.guerra.tasksync.R
import com.guerra.tasksync.data.User
import com.guerra.tasksync.screen.Screen
import com.guerra.tasksync.viewmodel.AuthViewModel
import com.guerra.tasksync.viewmodel.GoogleAuthUiClient
import com.guerra.tasksync.viewmodel.TeamsViewModel
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
    appTheme: String,
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
    coroutineScope: CoroutineScope,
    authViewModel: AuthViewModel,
    teamsViewModel: TeamsViewModel,
    onLoaded: () -> Unit
) {

    val bottomNavController = rememberNavController()
    var screenName by remember { mutableStateOf("") }

    val context = LocalContext.current

    val isDarkTheme = if(appTheme == "dark") true else if(appTheme == "light") false else isSystemInDarkTheme()
    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    val userData by authViewModel.userData.collectAsStateWithLifecycle()
    val teamsData by teamsViewModel.teamsData.collectAsStateWithLifecycle()
    val firebaseUser = authViewModel.currentUser

    LaunchedEffect(googleAuthUiClient.getSignedInUser()?.userId) {
        googleAuthUiClient.getSignedInUser()?.userId?.let { userId ->
            authViewModel.loadUserData(userId)
            teamsViewModel.getTeams(userId)
        }
    }

    LaunchedEffect(userData){
        if(userData != null) onLoaded()
    }

    Scaffold(
        topBar = { TopBar(screenName) },
        bottomBar = { BottomNavigationBar(isDarkTheme, bottomNavController) }
    ) { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = bottomNavController, startDestination = Screen.HomeScreen.route
        ) {
            composable(Screen.HomeScreen.route) {
                screenName = stringResource(R.string.app_name)
                HomeScreen(
                    userData = userData ?: User(),
                    teamsData = teamsData
                )
            }
            composable(Screen.TeamsScreen.route) {
                screenName = stringResource(R.string.teams)
                TeamsScreen(
                    teamsData = teamsData
                )
            }
            composable(Screen.NotificationsScreen.route) {
                screenName = stringResource(R.string.notifications)
                NotificationsScreen()
            }

            composable(Screen.SettingsScreen.route) {
                screenName = stringResource(R.string.settings)
                SettingsScreen(
                    userData = userData ?: User(),
                    onSignOut = {
                        coroutineScope.launch {
                            clearSharedPreferences(prefs)
                            resetThemeToDeviceDefault()
                            resetLanguageToDeviceDefault(context)

                            googleAuthUiClient.signOut()
                            navController.navigate("initial") {
                                popUpTo(navController.graph.id) { inclusive = true }
                                launchSingleTop = true
                            }

                            (context as? Activity)?.recreate()
                        }
                    },
                    onDelete = {
                        firebaseUser?.let { user ->
                            authViewModel.deleteUser(user) {
                                if (it) {
                                    coroutineScope.launch {
                                        googleAuthUiClient.signOut()
                                        navController.navigate("initial") {
                                            popUpTo(navController.graph.id) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    }
                                } else {
                                    //TODO show error
                                }

                            }
                        }

                    },
                    viewModel = authViewModel
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(screenName: String) {
    TopAppBar(
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.background,
            actionIconContentColor = MaterialTheme.colorScheme.background
        ),
        title = {
            Text(
                text = screenName,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
    )
}

@Composable
fun BottomNavigationBar(isDarkTheme: Boolean, bottomNavController: NavHostController) {
    val items = listOf(
        BottomNavigationItem(
            title = stringResource(R.string.home),
            route = Screen.HomeScreen.route,
            selectedItem = Icons.Default.Home,
            unselectedItem = Icons.Outlined.Home,
            hasNews = false
        ),
        BottomNavigationItem(
            title = stringResource(R.string.teams),
            route = Screen.TeamsScreen.route,
            selectedItem = Icons.Default.Person,
            unselectedItem = Icons.Outlined.Person,
            hasNews = true
        ),
        BottomNavigationItem(
            title = stringResource(R.string.notifications),
            route = Screen.NotificationsScreen.route,
            selectedItem = Icons.Default.Notifications,
            unselectedItem = Icons.Outlined.Notifications,
            hasNews = false,
            badgeCount = 4
        ),
        BottomNavigationItem(
            title = stringResource(R.string.settings),
            route = Screen.SettingsScreen.route,
            selectedItem = Icons.Default.Settings,
            unselectedItem = Icons.Outlined.Settings,
            hasNews = false
        )
    )

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
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
                    bottomNavController.navigate(item.route) {
                        popUpTo(bottomNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        minLines = 1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badgeCount != null) {
                                Badge(
                                    contentColor = Color.White,
                                    containerColor = Color.Red
                                ) {
                                    Text(text = item.badgeCount.toString())

                                }
                            } else if (item.hasNews) {
                                Badge(
                                    containerColor = Color.Red
                                )
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

private fun clearSharedPreferences(prefs: SharedPreferences){
    with(prefs.edit()) {
        clear()
        commit()
    }
}

private fun resetThemeToDeviceDefault(){
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
}

private fun resetLanguageToDeviceDefault(context: Context){
    val defaultLocale = Resources.getSystem().configuration.locales[0]
    val config = context.resources.configuration
    config.setLocale(defaultLocale)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
    } else {
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}