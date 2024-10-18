package com.sanapplications.cantransit

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sanapplications.cantransit.graphs.RootNavigationGraph
import com.sanapplications.cantransit.screens.AvailableRoutesScreen
import com.sanapplications.cantransit.screens.FavouritesScreen
import com.sanapplications.cantransit.screens.HomeScreen
import com.sanapplications.cantransit.screens.LocationScreen
import com.sanapplications.cantransit.screens.ProfileScreen
import com.sanapplications.cantransit.screens.SettingsScreen
import com.sanapplications.cantransit.ui.theme.CanTransitTheme
import com.sanapplications.cantransit.ui.theme.PrimaryColor
import com.sanapplications.cantransit.ui.theme.PrimaryLightColor
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CanTransitTheme {
                MainScreenWithSplash()  // Show splash and main screen together
            }
        }
    }

    @Composable
    private fun MainScreenWithSplash() {
        // State to control the visibility of the splash screen
        val splashVisible = remember { mutableStateOf(true) }

        // Launch the splash screen effect
        LaunchedEffect(Unit) {
            delay(2000L)  // Show splash for defined duration
            splashVisible.value = false  // Hide the splash screen
        }

        if (splashVisible.value) {
            SplashScreenUI()  // Show the splash screen
        } else {
//            MainScreen()  // Show the main screen after splash
            RootNavigationGraph(navController = rememberNavController())
        }
    }

    @Composable
    private fun SplashScreenUI() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PrimaryLightColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Splash Screen Logo"
            )
        }
    }

    @Composable
    fun MainScreen() {
        val items = listOf(
            BottomNavigationBarItem(
                title = "Favourites",
                selectedIcon = Icons.Filled.Favorite,
                unselectedIcon = Icons.Outlined.FavoriteBorder,
                hasNews = false,
            ),
            BottomNavigationBarItem(
                title = "Location",
                selectedIcon = Icons.Filled.LocationOn,
                unselectedIcon = Icons.Outlined.LocationOn,
                hasNews = false,
            ),
            BottomNavigationBarItem(
                title = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                hasNews = false,
            ),
            BottomNavigationBarItem(
                title = "Profile",
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
                hasNews = false,
                badgeCount = 4
            ),
            BottomNavigationBarItem(
                title = "Settings",
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Outlined.Settings,
                hasNews = true,
            ),
        )

        var selectedIndex by rememberSaveable { mutableStateOf(2) }
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (item.badgeCount != null) {
                                            Text(
                                                text = item.badgeCount.toString(),
                                                color = Color.Red, fontSize = 12.sp
                                            )
                                        } else if (item.hasNews) {
                                            Badge(
                                                contentColor = Color.Red
                                            )
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (index == selectedIndex) {
                                            item.selectedIcon
                                        } else item.unselectedIcon,
                                        contentDescription = item.title,
                                        modifier = Modifier.padding(vertical = 8.dp).size(24.dp)
                                    )
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                indicatorColor = PrimaryColor
                            ),
                            onClick = {
                                selectedIndex = index
                                navController.navigate(item.title)
                            }
                        )
                    }
                }
            }
        ) { padding ->
            Surface(
                modifier = Modifier.padding(padding)
            ) {
                SetupHomeBottomNavGraph(navController = navController)
            }
        }
    }

    @Composable
    fun SetupHomeBottomNavGraph(navController: NavHostController) {
        NavHost(navController = navController, startDestination = Routes.Home.route) {
            composable(Routes.Home.route) { HomeScreen(navController) }
            composable(Routes.Favourites.route) { FavouritesScreen(navController) }
            composable(Routes.Location.route) { LocationScreen(navController) }
            composable(Routes.Profile.route) { ProfileScreen(navController) }
            composable(Routes.Settings.route) { SettingsScreen(navController) }
            composable(Routes.AvailableRoutes.route) { AvailableRoutesScreen(navController) }
        }
    }

    data class BottomNavigationBarItem(
        val title: String,
        val selectedIcon: ImageVector,
        val unselectedIcon: ImageVector,
        val hasNews: Boolean,
        val badgeCount: Int? = null
    )
}

