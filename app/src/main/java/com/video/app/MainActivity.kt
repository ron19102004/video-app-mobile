package com.video.app

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import androidx.navigation.toRoute
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.video.app.api.RetrofitAPI
import com.video.app.api.URL
import com.video.app.states.objects.AppInitializerState
import com.video.app.ui.screens.HomeScreen
import com.video.app.ui.screens.MyProfileScreen
import com.video.app.ui.screens.ReportScreen
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
import com.video.app.ui.screens.MyVideoScreen
import com.video.app.ui.screens.PlaylistVideoScreen
import com.video.app.ui.screens.Router
import com.video.app.ui.screens.UpdateAvatarScreen
import com.video.app.ui.screens.VideoPlayerScreen
import com.video.app.ui.screens.UserProfileScreen

class MainActivity : ComponentActivity() {
    private var initialized = mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
                AppInitializerState.Init(this)
                initialized.value = true
            }
            NavHost(
                navController = AppInitializerState.navController,
                startDestination = Router.HomeScreen
            ) {
                composable<Router.HomeScreen> {
                    HomeScreen().Screen();
                }
                composable<Router.LoginScreen> {
                    LoginScreen().Screen()
                }

                composable<Router.UpdateAvatarScreen> {
                    UpdateAvatarScreen().Screen()
                }
                composable<Router.MyVideoScreen> {
                    MyVideoScreen().Screen()
                }
                composable<Router.RegisterScreen> {
                    RegisterScreen().Screen()
                }
                composable<Router.SettingScreen> {
                    SettingScreen().Screen()
                }
                composable<Router.MyProfileScreen> {
                    MyProfileScreen().Screen()
                }
                composable<Router.SearchScreen> {
                    SearchScreen().Screen(
                    )
                }
                composable<Router.ReportScreen> {
                    ReportScreen().Screen()
                }
                composable<Router.VIPRegisterScreen> {
                    VIPRegisterScreen().Screen()
                }
                composable<Router.PlaylistVideoScreen> {
                    val args = it.toRoute<Router.PlaylistVideoScreen>()
                    PlaylistVideoScreen().Screen(
                        playlistId = args.playlistId,
                        playlistIndex = args.playlistIndex,
                        playlistAt = args.playlistAt,
                    )
                }
                composable<Router.OTPScreen> {
                    val args = it.toRoute<Router.OTPScreen>()
                    OTPScreen().Screen(
                        email = args.email,
                        token = args.token
                    )
                }
                composable<Router.UserProfileScreen> {
                    val args = it.toRoute<Router.UserProfileScreen>()
                    UserProfileScreen().Screen(
                        userId = args.userId,
                    )
                }
                composable<Router.VideoPlayerScreen> {
                    val args = it.toRoute<Router.VideoPlayerScreen>()
                    VideoPlayerScreen().Screen(
                        indexVideo = args.index,
                        videoAt = args.videoAt,
                        uploaderId = args.uploaderId,
                        playlistAt = args.playlistAt
                    )
                }
            }
        }
    }
}
