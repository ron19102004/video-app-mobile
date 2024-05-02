package com.video.app

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.video.app.api.RetrofitAPI
import com.video.app.api.URL
import com.video.app.ui.screens.HomeScreen
import com.video.app.ui.screens.ProfileScreen
import com.video.app.ui.screens.ReportScreen
import com.video.app.ui.screens.Router
import com.video.app.ui.screens.SearchScreen
import com.video.app.ui.screens.SettingScreen
import com.video.app.ui.screens.VIPRegisterScreen
import com.video.app.ui.screens.auth.LoginScreen
import com.video.app.ui.screens.auth.OTPScreen
import com.video.app.ui.screens.auth.RegisterScreen
import com.video.app.states.objects.NavigationState
import com.video.app.states.objects.UiState
import com.video.app.states.viewmodels.CategoryAndCountryViewModel
import com.video.app.states.viewmodels.UserViewModel

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
    private var initialized = mutableStateOf(false)
    private lateinit var userViewModel: UserViewModel
    private lateinit var categoryAndCountryViewModel: CategoryAndCountryViewModel

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
            if (!initialized.value) {
                URL.init(this)
                RetrofitAPI.init(this)
                UiState.init(this)
                categoryAndCountryViewModel = viewModel()
                userViewModel = viewModel()
                userViewModel.init(this);
                navController = rememberNavController()
                initialized.value = true
            }
            NavHost(
                navController = navController, startDestination = Router.HomeScreen.route
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
                    arguments = listOf(navArgument("email") {
                        type = NavType.StringType;
                        nullable = false
                    }, navArgument("token") {
                        type = NavType.StringType;
                        nullable = false
                    })
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
