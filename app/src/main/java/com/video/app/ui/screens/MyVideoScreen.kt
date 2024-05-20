package com.video.app.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import com.video.app.config.CONSTANT
import com.video.app.states.objects.AppInitializerState
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.states.viewmodels.VideoAndPlaylistViewModel
import com.video.app.ui.screens.components.Heading
import com.video.app.ui.screens.components.PullToRefreshLazyColumn
import com.video.app.ui.screens.components.VideoCard
import com.video.app.ui.theme.AppColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyVideoScreen {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun Screen(
        videoAndPlaylistViewModel: VideoAndPlaylistViewModel = AppInitializerState.videoAndPlaylistViewModel,
        userViewModel: UserViewModel = AppInitializerState.userViewModel
    ) {
        videoAndPlaylistViewModel.getMyVideos()
        var videos =
            videoAndPlaylistViewModel.myVideos.asFlow().collectAsState(initial = emptyList())
        var isRefreshing by remember {
            mutableStateOf(false)
        }
        val scope = rememberCoroutineScope()
        Scaffold(
            containerColor = AppColor.background,
            topBar = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        AppInitializerState.navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = AppColor.primary_text
                        )
                    }
                    Heading(text = "My Videos", size = CONSTANT.UI.TEXT_SIZE.MD)
                }
            }
        ) {
            PullToRefreshLazyColumn<Any>(
                isRefreshing = isRefreshing,
                onRefresh = {
                    scope.launch {
                        isRefreshing = true
                        delay(500L)
                        videoAndPlaylistViewModel.getMyVideos {
                            isRefreshing = false
                        }
                    }
                },
                modifier = Modifier.padding(10.dp, it.calculateTopPadding(), 10.dp, 0.dp),
                contentFix = {
                    if (isRefreshing) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(30.dp))
                            CircularProgressIndicator(color = AppColor.primary_text)
                            Spacer(modifier = Modifier.height(30.dp))
                        }
                    } else {
                        if (videos?.value?.isNullOrEmpty() == false) {
                            Column {
                                videos?.value?.forEachIndexed { index, video ->
                                    VideoCard(
                                        index = index,
                                        videoModel = video,
                                        onClick = { index, video ->
                                            video?.uploader?.id?.let { uploaderId ->
                                                Navigate(Router.VideoPlayerScreen(
                                                    index = index,
                                                    videoAt = VideoPlayerScreen.VideoAt.MY_VIDEO_SCREEN,
                                                    uploaderId = uploaderId,
                                                    "null"
                                                ))
                                            }
                                        },
                                        onLongClick = {}
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.height(30.dp))
                                Heading(
                                    text = "List empty",
                                    size = CONSTANT.UI.TEXT_SIZE.MD,
                                    color = AppColor.background_container
                                )
                                Spacer(modifier = Modifier.height(30.dp))
                            }
                        }
                    }
                }
            )
        }
    }
}