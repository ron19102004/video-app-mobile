package com.video.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import coil.compose.AsyncImage
import com.video.app.Navigate
import com.video.app.R
import com.video.app.api.models.UserModel
import com.video.app.config.CONSTANT
import com.video.app.navController
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.states.viewmodels.VideoAndPlaylistViewModel
import com.video.app.ui.screens.components.BtnText
import com.video.app.ui.screens.components.Heading
import com.video.app.ui.screens.components.VideoCard
import com.video.app.ui.theme.AppColor

class YourProfileScreen {
    private lateinit var videoAndPlaylistViewModel: VideoAndPlaylistViewModel
    private lateinit var infoUserConfirmed: State<UserModel?>
    private val tabTitles = listOf("Videos", "Playlists")
    private var tabSelected = mutableIntStateOf(0)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Screen(
        userId: Long,
        videoAndPlaylistViewModel: VideoAndPlaylistViewModel,
        userViewModel: UserViewModel
    ) {
        videoAndPlaylistViewModel.fetchVideosWithUploaderId(uploaderId = userId)
        this.videoAndPlaylistViewModel = videoAndPlaylistViewModel
        infoUserConfirmed =
            userViewModel.infoUserConfirmed.asFlow().collectAsState(initial = UserModel())
        Scaffold(containerColor = AppColor.background,
            topBar = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = AppColor.primary_text
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Heading(
                        text = infoUserConfirmed?.value?.fullName ?: "Unknown",
                        size = CONSTANT.UI.TEXT_SIZE.MD,
                        maxLines = 1
                    )
                }
            }
        ) {
            LazyColumn(modifier = Modifier.padding(10.dp, it.calculateTopPadding(), 10.dp, 0.dp)) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val avatarPainterError = painterResource(id = R.drawable.account_icon)
                        val avatarModifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                        Box(modifier = avatarModifier) {
                            AsyncImage(
                                model = infoUserConfirmed?.value?.imageURL,
                                contentDescription = null,
                                modifier = avatarModifier,
                                contentScale = ContentScale.Fit,
                                placeholder = avatarPainterError,
                                error = avatarPainterError
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Heading(
                                text = infoUserConfirmed?.value?.fullName ?: "Unknown",
                                size = CONSTANT.UI.TEXT_SIZE.LG,
                                maxLines = 2
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.confirmed),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "@${infoUserConfirmed?.value?.username ?: "unknown"}",
                                    style = TextStyle(
                                        color = AppColor.primary_text,
                                        fontWeight = FontWeight.Normal
                                    )
                                )
                            }

                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    BtnText(
                        onClick = { /*TODO*/ },
                        text = "Subscribe",
                        height = 40.dp,
                        shape = CircleShape,
                        buttonColor = AppColor.primary_text,
                        textColor = AppColor.background
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    PrimaryTabRow(
                        selectedTabIndex = tabSelected.value,
                        containerColor = AppColor.background,
                        contentColor = AppColor.background_container,
                        divider = { HorizontalDivider(color = AppColor.background_container) },
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            Tab(
                                selected = index == tabSelected.value,
                                onClick = { tabSelected.value = index },
                                text = { Text(text = title) },
                                selectedContentColor = AppColor.primary_text,
                                unselectedContentColor = AppColor.background_container,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    when (tabSelected.value) {
                        0 -> Videos()
                        1 -> Playlists()
                    }
                }
            }
        }
    }

    @Composable
    private fun Videos() {
        val videos = videoAndPlaylistViewModel.videosWithUploaderId.asFlow()
            .collectAsState(initial = emptyList())
        if (!videoAndPlaylistViewModel.isLoadingVideosWithUploaderId.value) {
            if (videos?.value?.isNullOrEmpty() == false) {
                Column {
                    videos?.value?.forEachIndexed { index, video ->
                        VideoCard(
                            index = index,
                            videoModel = video,
                            onClick = { index, video ->
                                video?.uploader?.id?.let {uploaderId->
                                    Navigate(
                                        Router.VideoPlayerScreen.setArgs(
                                            index,
                                            VideoPlayerScreen.VideoAt.PLAYER_SCREEN_OR_YOUR_PROFILE,
                                            uploaderId
                                        )
                                    )
                                }

                            },
                            onLongClick = {
                            }
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

    @Composable
    private fun Playlists() {

    }
}