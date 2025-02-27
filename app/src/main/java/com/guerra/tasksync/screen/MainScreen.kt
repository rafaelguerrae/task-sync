package com.guerra.tasksync.screen

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
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.guerra.tasksync.R
import com.guerra.tasksync.data.UserData

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
    userData: UserData?,
    onSignOut: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()



    Scaffold(
        topBar = { TopBar(isDarkTheme) },
        bottomBar = { BottomNavigationBar(isDarkTheme) }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (userData?.profilePictureUrl != null) {
                    AsyncImage(
                        model = userData.profilePictureUrl,
                        contentDescription = "My profile picture",
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (userData?.username != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Welcome,",
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.size(4.dp))

                        Text(
                            text = "${userData.username.split(" ").firstOrNull()}!",
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.blue)
                        )


                    }


                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onSignOut,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Sign out",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(isDarkTheme: Boolean) {
    val logoResId = if (isDarkTheme)  R.drawable.tasksync_nobg_white else R.drawable.tasksync_nobg_black
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
            }}
    )
}

@Composable
fun BottomNavigationBar(isDarkTheme: Boolean){
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
                    unselectedIconColor = if(isDarkTheme) Color.White else Color.Black,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = if(isDarkTheme) Color.White else Color.Black
                ),
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    //TODO navController.navigate
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