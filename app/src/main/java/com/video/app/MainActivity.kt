package com.video.app

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.video.app.api.RetrofitAPI
import com.video.app.api.URL
import com.video.app.screens.HomeScreen
import com.video.app.screens.ProfileScreen
import com.video.app.screens.ReportScreen
import com.video.app.screens.Router
import com.video.app.screens.SearchScreen
import com.video.app.screens.SettingScreen
import com.video.app.screens.VIPRegisterScreen
import com.video.app.screens.auth.LoginScreen
import com.video.app.screens.auth.OTPScreen
import com.video.app.screens.auth.RegisterScreen
import com.video.app.states.objects.NavigationState
import com.video.app.states.objects.UiState
import com.video.app.states.viewmodels.CategoryAndCountryViewModel
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.ui.theme.MobileTheme

@SuppressLint("StaticFieldLeak")
lateinit var navController: NavHostController
fun Navigate(router: Router) {
    navController.navigate(route = router.route)
    NavigationState.navSelected = router.id
}

fun Navigate(route: String) {
    navController.navigate(route = route)
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //PermissionState
            val notificationPermissionState =
                rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
            LaunchedEffect(key1 = true) {
                if (!notificationPermissionState.status.isGranted) {
                    notificationPermissionState.launchPermissionRequest()
                }
            }
            //ViewModel init
            URL.init(this)
            RetrofitAPI.init(this)
            UiState.init(this)
            val categoryAndCountryViewModel: CategoryAndCountryViewModel = viewModel()
            val userViewModel: UserViewModel = viewModel()
            userViewModel.init(this);
            navController = rememberNavController()
            //Navigation Graph
            MobileTheme(darkTheme = UiState.darkMode) {
                NavHost(
                    navController = navController,
                    startDestination = Router.HomeScreen.route
                ) {
                    composable(route = Router.HomeScreen.route) {
                        HomeScreen().Screen(
                            userViewModel = userViewModel,
                            categoryAndCountryViewModel = categoryAndCountryViewModel
                        );
                    }
                    composable(route = Router.LoginScreen.route) {
                        LoginScreen().Screen(userViewModel = userViewModel)
                    }
                    composable(route = Router.OTPScreen.route + "/{email}/{token}",
                        arguments = listOf(
                            navArgument("email") {
                                type = NavType.StringType;
                                nullable = false
                            },
                            navArgument("token") {
                                type = NavType.StringType;
                                nullable = false
                            }
                        )
                    ) {
                        OTPScreen().Screen(
                            userViewModel = userViewModel,
                            email = it.arguments?.getString("email")!!,
                            token = it.arguments?.getString("token")!!
                        )
                    }
                    composable(route = Router.RegisterScreen.route) {
                        RegisterScreen().Screen(userViewModel = userViewModel)
                    }
                    composable(route = Router.SettingScreen.route) {
                        SettingScreen().Screen(userViewModel = userViewModel)
                    }
                    composable(route = Router.ProfileScreen.route) {
                        ProfileScreen().Screen(userViewModel = userViewModel)
                    }
                    composable(route = Router.SearchScreen.route) {
                        SearchScreen().Screen(userViewModel = userViewModel)
                    }
                    composable(route = Router.ReportScreen.route) {
                        ReportScreen().Screen(userViewModel = userViewModel)
                    }
                    composable(route = Router.VIPRegisterScreen.route) {
                        VIPRegisterScreen().Screen(userViewModel = userViewModel)
                    }
                }
            }
        }
    }
}
