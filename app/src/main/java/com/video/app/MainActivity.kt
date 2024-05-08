package com.video.app

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import com.video.app.states.viewmodels.VideoAndPlaylistViewModel
import com.video.app.ui.screens.VideoPlayerScreen

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
    private lateinit var videoAndPlaylistViewModel: VideoAndPlaylistViewModel

    @OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
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
                categoryAndCountryViewModel.init()

                userViewModel = viewModel()
                userViewModel.init(this);

                videoAndPlaylistViewModel = viewModel()
                videoAndPlaylistViewModel.init(this, userViewModel)

                navController = rememberNavController()
                initialized.value = true
            }
            NavHost(
                navController = navController, startDestination = Router.HomeScreen.route
            ) {
                composable(route = Router.HomeScreen.route) {
                    HomeScreen().Screen(
                        userViewModel = userViewModel,
                        categoryAndCountryViewModel = categoryAndCountryViewModel,
                        videoAndPlaylistViewModel = videoAndPlaylistViewModel
                    );
                }
                composable(route = Router.LoginScreen.route) {
                    LoginScreen().Screen(userViewModel = userViewModel)
                }
                composable(route = Router.VideoPlayerScreen.route + "/{index}/{videoAt}",
                    arguments = listOf(
                        navArgument("index") {
                            nullable = false
                            type = NavType.IntType
                        },
                        navArgument("videoAt") {
                            nullable = false
                            type = NavType.StringType
                        }
                    )
                ) {
                    it?.arguments?.getInt("index")?.let { index ->
                        it?.arguments?.getString("videoAt")?.let { videoAt ->
                            VideoPlayerScreen().Screen(
                                userViewModel = userViewModel,
                                indexVideo = index,
                                videoAndPlaylistViewModel = videoAndPlaylistViewModel,
                                videoAt = videoAt
                            )
                        }
                    }
                }
                composable(
                    route = Router.OTPScreen.route + "/{email}/{token}",
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
                    ProfileScreen().Screen(
                        userViewModel = userViewModel,
                        videoAndPlaylistViewModel = videoAndPlaylistViewModel
                    )
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
