package com.video.app.states.objects

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.video.app.api.RetrofitAPI
import com.video.app.states.viewmodels.CategoryAndCountryViewModel
import com.video.app.states.viewmodels.CommentAndReportViewModel
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.states.viewmodels.VideoAndPlaylistViewModel
@SuppressLint("StaticFieldLeak")
object AppInitializerState {
    lateinit var navController: NavHostController
    lateinit var userViewModel: UserViewModel
    lateinit var categoryAndCountryViewModel: CategoryAndCountryViewModel
    lateinit var videoAndPlaylistViewModel: VideoAndPlaylistViewModel
    lateinit var commentAndReportViewModel: CommentAndReportViewModel

    @Composable
    fun Init(context: Context) {
        RetrofitAPI.init(context)
        UiState.init(context)

        categoryAndCountryViewModel = viewModel()
        categoryAndCountryViewModel.init()

        userViewModel = viewModel()
        userViewModel.init(context);

        videoAndPlaylistViewModel = viewModel()
        videoAndPlaylistViewModel.init(context, userViewModel)

        commentAndReportViewModel = viewModel()
        commentAndReportViewModel.init(context)

        navController = rememberNavController()
    }
}