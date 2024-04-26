package com.video.app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.video.app.screens.HomeScreen
import com.video.app.screens.Router
import com.video.app.screens.auth.LoginScreen
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.ui.theme.MobileTheme

@SuppressLint("StaticFieldLeak")
lateinit var navController: NavHostController
fun Navigate(router: Router) {
    navController.navigate(route = router.route)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val userViewModel: UserViewModel = viewModel()
            userViewModel.init(this);
            navController = rememberNavController()
            MobileTheme {
                NavHost(
                    navController = navController,
                    startDestination = Router.LoginScreen.route
                ) {
                    composable(route = Router.HomeScreen.route) {
                        HomeScreen().Screen(userViewModel = userViewModel);
                    }
                    composable(route = Router.LoginScreen.route) {
                        LoginScreen().Screen(userViewModel = userViewModel)
                    }
                }
            }
        }
    }
}
