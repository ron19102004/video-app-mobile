package com.video.app.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.asFlow
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.video.app.Navigate
import com.video.app.R
import com.video.app.api.models.VideoModel
import com.video.app.config.CONSTANT
import com.video.app.navController
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.states.viewmodels.VideoAndPlaylistViewModel
import com.video.app.ui.screens.components.Heading
import com.video.app.ui.screens.components.VideoCard
import com.video.app.ui.theme.AppColor


class VideoPlayerScreen {
    object VideoAt {
        const val HOME_SCREEN = "home_screen"
        const val PLAYER_SCREEN_OR_YOUR_PROFILE = "player_screen"
        const val SEARCH_SCREEN = "search_screen"
        const val PLAYLIST_SCREEN = "playlist_screen"
    }

    private lateinit var userViewModel: UserViewModel
    private lateinit var videoAndPlaylistViewModel: VideoAndPlaylistViewModel
    private var videoPlayer = mutableStateOf(VideoModel())
    private lateinit var player: ExoPlayer
    private lateinit var activity: Activity
    private var openDescription = mutableStateOf(false)

    @kotlin.OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    @Composable
    fun Screen(
        userViewModel: UserViewModel,
        videoAndPlaylistViewModel: VideoAndPlaylistViewModel,
        indexVideo: Int,
        videoAt: String,
        uploaderId: Long
    ) {
        videoAndPlaylistViewModel.fetchVideosWithUploaderId(uploaderId = uploaderId)
        activity = LocalContext.current as Activity
        this.userViewModel = userViewModel;
        this.videoAndPlaylistViewModel = videoAndPlaylistViewModel
        videoPlayer.value =
            when (videoAt) {
                VideoAt.HOME_SCREEN -> {
                    videoAndPlaylistViewModel.videosOnHomeScreen.value?.get(indexVideo)!!
                }

                VideoAt.PLAYER_SCREEN_OR_YOUR_PROFILE -> {
                    videoAndPlaylistViewModel.videosWithUploaderId.value?.get(indexVideo)!!
                }

                VideoAt.SEARCH_SCREEN -> {
                    videoAndPlaylistViewModel.videosOnSearchScreen.value?.get(indexVideo)!!
                }

                VideoAt.PLAYLIST_SCREEN -> {
                    videoAndPlaylistViewModel.videosOfPlaylistId.value?.get(indexVideo)!!
                }

                else -> {
                    VideoModel()
                }
            }
        val videos = videoAndPlaylistViewModel.videosWithUploaderId.asFlow()
            .collectAsState(initial = emptyList())
        Scaffold(
            modifier = Modifier, containerColor = AppColor.background,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp, it.calculateTopPadding(), 0.dp, it.calculateBottomPadding())
            ) {
                if (videoPlayer != null) {
                    PlayVideoContainer()
                } else {
                    Row(
                        modifier = Modifier
                            .background(color = Color.Black)
                            .fillMaxWidth()
                            .height(200.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Heading(text = "Video error!!!", size = CONSTANT.UI.TEXT_SIZE.MD)
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .padding(10.dp, 0.dp),
                ) {
                    item {
                        Spacer(modifier = Modifier.height(5.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = AppColor.background_container
                            ),
                            onClick = { openDescription.value = true }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp)
                            ) {
                                videoPlayer?.value?.name?.let { name ->
                                    Heading(
                                        text = name, size = CONSTANT.UI.TEXT_SIZE.SM,
                                        maxLines = 2
                                    )
                                }
                                Row {
                                    videoPlayer?.value?.tag?.let { tag ->
                                        Text(
                                            text = "#${tag} ", style = TextStyle(
                                                color = AppColor.primary_content,
                                                fontWeight = FontWeight.Normal
                                            )
                                        )
                                    }
                                    Text(
                                        text = " ...more", style = TextStyle(
                                            color = AppColor.primary_content,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }

                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        if (!videoAndPlaylistViewModel.isLoadingVideosWithUploaderId.value) {
                            if (!videoAndPlaylistViewModel.isErrorFetchVideoWithUploaderId.value) {
                                videos.value?.forEachIndexed { index, video ->
                                    VideoCard(
                                        index = index,
                                        videoModel = video,
                                        onClick = { index, video ->
                                            video?.uploader?.id?.let { uploaderId ->
                                                Navigate(
                                                    Router.VideoPlayerScreen.setArgs(
                                                        index,
                                                        VideoAt.PLAYER_SCREEN_OR_YOUR_PROFILE,
                                                        uploaderId
                                                    )
                                                )
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
                                CircularProgressIndicator(color = AppColor.primary_text)
                                Spacer(modifier = Modifier.height(30.dp))
                            }
                        }
                    }
                }
            }
        }
        ModalBottomSheetContainer()
    }

    @kotlin.OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ModalBottomSheetContainer() {
        val openDescriptionState = rememberModalBottomSheetState()
        if (openDescription.value) {
            ModalBottomSheet(
                onDismissRequest = { openDescription.value = false },
                sheetState = openDescriptionState,
                containerColor = AppColor.background,
            ) {
                Column(modifier = Modifier.padding(20.dp, 0.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Heading(text = "Description", size = CONSTANT.UI.TEXT_SIZE.MD)
                        IconButton(onClick = { Navigate(Router.HomeScreen) }) {
                            Icon(
                                imageVector = Icons.Rounded.Home,
                                contentDescription = null,
                                tint = AppColor.primary_text
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(10.dp))
                    videoPlayer.value.name?.let {
                        Heading(
                            text = it,
                            size = CONSTANT.UI.TEXT_SIZE.SM
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow {
                        item {
                            videoPlayer.value.tag?.let {
                                TextButton(
                                    onClick = { /*TODO*/ },
                                    modifier = Modifier.height(30.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AppColor.background_container
                                    )
                                ) {
                                    Text(
                                        text = "#${it}", style = TextStyle(
                                            color = AppColor.primary_text,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = CONSTANT.UI.TEXT_SIZE.SM_
                                        )
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            TextButton(
                                onClick = { /*TODO*/ },
                                modifier = Modifier.height(30.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AppColor.background_container
                                )
                            ) {
                                Text(
                                    text = "Release: ${videoPlayer.value.release ?: "Unknown"}",
                                    style = TextStyle(
                                        color = AppColor.primary_text,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = CONSTANT.UI.TEXT_SIZE.SM_
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    videoPlayer.value.description?.let {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = AppColor.background_container,
                                    shape = RoundedCornerShape(CONSTANT.UI.ROUNDED_INPUT_BUTTON)
                                ),
                        ) {
                            Text(
                                text = it,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                style = TextStyle(
                                    color = AppColor.primary_text
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    val painterAvatarError = painterResource(id = R.drawable.user)
                    val imageAvatarModifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val uploaderId = videoPlayer?.value?.uploader?.id
                                if (uploaderId != null) {
                                    userViewModel.fetchInfoUserConfirmed(id = uploaderId)
                                    videoAndPlaylistViewModel.fetchVideosWithUploaderId(uploaderId = uploaderId)
                                    Navigate(Router.YourProfileScreen.setArgs(uploaderId))
                                }
                            },
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = imageAvatarModifier) {
                            AsyncImage(
                                model = videoPlayer?.value?.uploader?.imageURL,
                                contentDescription = null,
                                modifier = imageAvatarModifier,
                                contentScale = ContentScale.Fit,
                                placeholder = painterAvatarError,
                                error = painterAvatarError
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        videoPlayer?.value?.uploader?.fullName?.let {
                            Heading(
                                text = it,
                                size = CONSTANT.UI.TEXT_SIZE.SM
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    @Composable
    private fun PlayVideoContainer(context: Context = LocalContext.current) {
        var isFullScreen by remember {
            mutableStateOf(false)
        }
        player = ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoPlayer.value?.src ?: ""))
//            setMediaItem(MediaItem.fromUri("https://ia801209.us.archive.org/30/items/ts-eras-tour/TS%20ERAS%20TOUR.mp4"))
            prepare()
        }
        val playerView = PlayerView(context)
        val fullScreenHandle: (Boolean) -> Unit = {
            if (it) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
                isFullScreen = true
            } else {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
                isFullScreen = false
            }
        }
        playerView.setFullscreenButtonClickListener { fullScreenHandle(it) }
        playerView.player = player
        DisposableEffect(Unit) {
            onDispose {
                player.stop()
                player.release()
            }
        }
        AndroidView(
            factory = { playerView },
            modifier = if (!isFullScreen) Modifier
                .fillMaxWidth()
                .height(200.dp)
            else Modifier
                .fillMaxSize()
        )
    }
}