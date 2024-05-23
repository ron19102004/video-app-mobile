package com.video.app.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.twotone.Share
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import coil.compose.AsyncImage
import com.video.app.R
import com.video.app.api.models.Privacy
import com.video.app.api.models.VideoModel
import com.video.app.config.CONSTANT
import com.video.app.states.objects.AppInitializerState
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.states.viewmodels.VideoAndPlaylistViewModel
import com.video.app.ui.screens.components.BtnText
import com.video.app.ui.screens.components.Heading
import com.video.app.ui.screens.components.NavigationBarBottom
import com.video.app.ui.screens.components.PullToRefreshLazyColumn
import com.video.app.ui.screens.components.VideoCardRow
import com.video.app.ui.theme.AppColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistVideoScreen {
    object PlaylistAt {
        const val MY_PROFILE_SCREEN = "my_profile_screen"
        const val USER_PROFILE_SCREEN = "user_profile_screen"
    }

    private var openShareDialog = mutableStateOf(false)
    private var isPlaylistPublic = mutableStateOf(false)

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun Screen(
        playlistAt: String,
        playlistIndex: Int,
        playlistId: Long,
        userViewModel: UserViewModel = AppInitializerState.userViewModel,
        videoAndPlaylistViewModel: VideoAndPlaylistViewModel = AppInitializerState.videoAndPlaylistViewModel
    ) {
        var isRefreshing by remember {
            mutableStateOf(true)
        }
        val playlist = when (playlistAt) {
            PlaylistAt.MY_PROFILE_SCREEN -> {
                videoAndPlaylistViewModel.getMyVideosPlaylist(playlistId = playlistId, action = {
                    isRefreshing = false
                })
                videoAndPlaylistViewModel.myPlaylist.value?.get(
                    playlistIndex
                )
            }

            PlaylistAt.USER_PROFILE_SCREEN -> {
                videoAndPlaylistViewModel.getUserVideosPlaylist(
                    playlistId = playlistId,
                    action = {
                        isRefreshing = false
                    })
                videoAndPlaylistViewModel.userPlaylist.value?.get(
                    playlistIndex
                )
            }

            else -> null
        }
        isPlaylistPublic.value = playlist?.privacy == Privacy.PUBLIC
        val videos = when (playlistAt) {
            PlaylistAt.MY_PROFILE_SCREEN -> {
                videoAndPlaylistViewModel.videosOfMyPlaylistId.asFlow()
                    .collectAsState(initial = emptyList())
            }

            else -> MutableLiveData<List<VideoModel>>(emptyList()).asFlow()
                .collectAsState(initial = emptyList())
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
                    IconButton( onClick = {
                        AppInitializerState.navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = AppColor.primary_text
                        )
                    }
                }
            },
            bottomBar = { NavigationBarBottom(userViewModel) },
        ) {
            val painterErrorVideo = painterResource(id = R.drawable.video_bg)
            PullToRefreshLazyColumn<Any>(
                modifier = Modifier.padding(it),
                isRefreshing = isRefreshing,
                onRefresh = {
                    scope.launch {
                        isRefreshing = true
                        when (playlistAt) {
                            PlaylistAt.MY_PROFILE_SCREEN -> {
                                videoAndPlaylistViewModel.getMyVideosPlaylist(
                                    playlistId = playlistId,
                                    action = {
                                        isRefreshing = false

                                    }
                                )
                            }
                        }
                    }
                },
                contentFix = {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = playlist?.image,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(340.dp)
                                .blur(15.dp),
                            placeholder = painterErrorVideo,
                            error = painterErrorVideo,
                            alpha = 0.5f,
                        )
                        Column(modifier = Modifier.padding(15.dp)) {
                            AsyncImage(
                                model = playlist?.image,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(CONSTANT.UI.ROUNDED_INPUT_BUTTON)),
                                placeholder = painterErrorVideo,
                                error = painterErrorVideo
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Heading(
                                text = playlist?.name ?: "Unknown",
                                size = CONSTANT.UI.TEXT_SIZE.MD,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier,
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${videos?.value?.size ?: 0} videos",
                                        style = TextStyle(
                                            color = AppColor.primary_text
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Row(
                                        modifier = Modifier,
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(
                                                id = if (playlist?.privacy == Privacy.PRIVATE)
                                                    R.drawable.private_icon else R.drawable.public_icon
                                            ),
                                            contentDescription = null,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Text(
                                            text = if (playlist?.privacy == Privacy.PRIVATE)
                                                "private" else "public",
                                            style = TextStyle(
                                                color = AppColor.primary_text
                                            )
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = { openShareDialog.value = true },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = AppColor.background_trans1
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.TwoTone.Share,
                                        contentDescription = null, tint = AppColor.primary_text
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(35.dp))
                            if (isRefreshing) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer(modifier = Modifier.height(30.dp))
                                    CircularProgressIndicator(color = AppColor.primary_text)
                                    Spacer(modifier = Modifier.height(30.dp))
                                }
                            } else {
                                videos?.value?.forEachIndexed { index, video ->
                                    VideoCardRow(
                                        index = index,
                                        videoModel = video,
                                        onClick = { index, video ->
                                            video?.uploader?.id?.let { uploaderId ->
                                                Navigate(
                                                    Router.VideoPlayerScreen(
                                                        index,
                                                        VideoPlayerScreen.VideoAt.PLAYLIST_SCREEN,
                                                        uploaderId,
                                                        playlistAt
                                                    )
                                                )
                                            }
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }
                    }
                }
            )
            DialogContainer()
        }
    }

    @Composable
    fun DialogContainer() {
        if (openShareDialog.value) {
            var isPublic by remember {
                mutableStateOf(isPlaylistPublic.value)
            }
            val PrivacyCard: @Composable (title: String, value: Boolean, changeValue: (Boolean) -> Unit) -> Unit =
                { title, value, changeValue ->
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = value,
                            onCheckedChange = {
                                changeValue(it)
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = AppColor.primary_content
                            )
                        )
                        Heading(text = title, size = CONSTANT.UI.TEXT_SIZE.SM)
                    }
                }
            Dialog(onDismissRequest = { openShareDialog.value = false }) {
                Column(
                    modifier = Modifier
                        .width(300.dp)
                        .background(
                            color = AppColor.background,
                            shape = RoundedCornerShape(CONSTANT.UI.ROUNDED_INPUT_BUTTON)
                        )
                ) {
                    Heading(
                        text = "PRIVACY",
                        size = CONSTANT.UI.TEXT_SIZE.MD,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    )
                    HorizontalDivider()
                    PrivacyCard("Public", isPublic) {
                        isPublic = it
                    }
                    PrivacyCard("Private", !isPublic) {
                        isPublic = !it
                    }
                    BtnText(
                        enabled = isPublic != isPlaylistPublic.value,
                        onClick = { /*TODO*/ },
                        text = "Change",
                        buttonColor = AppColor.primary_content,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp, 0.dp),
                        height = 40.dp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}