package com.video.app.ui.screens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import coil.compose.AsyncImage
import com.video.app.R
import com.video.app.api.models.UserModel
import com.video.app.config.CONSTANT
import com.video.app.states.objects.AppInitializerState
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.states.viewmodels.VideoAndPlaylistViewModel
import com.video.app.ui.screens.components.BtnText
import com.video.app.ui.screens.components.Heading
import com.video.app.ui.screens.components.PullToRefreshLazyColumn
import com.video.app.ui.screens.components.VideoCard
import com.video.app.ui.theme.AppColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserProfileScreen {
    private lateinit var videoAndPlaylistViewModel: VideoAndPlaylistViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var infoUserConfirmed: State<UserModel?>
    private val tabTitles = listOf("Videos", "Playlists")
    private var tabSelected = mutableIntStateOf(0)

    //for bottom sheet
    private var openUnsubscribeSheet = mutableStateOf(false)

    private fun init(userId: Long) {
        if (userViewModel?.isLoggedIn == true)
            userViewModel.fetchInfoUserConfirmedWhenLoggedIn(id = userId)
        else userViewModel.fetchInfoUserConfirmed(id = userId)
        videoAndPlaylistViewModel.fetchVideosWithUploaderId(uploaderId = userId)
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Screen(
        userId: Long,
        videoAndPlaylistViewModel: VideoAndPlaylistViewModel= AppInitializerState.videoAndPlaylistViewModel,
        userViewModel: UserViewModel=AppInitializerState.userViewModel
    ) {
        this.userViewModel = userViewModel
        this.videoAndPlaylistViewModel = videoAndPlaylistViewModel
        init(userId)
        infoUserConfirmed =
            userViewModel.infoUserConfirmed.asFlow().collectAsState(initial = UserModel())
        var enabledButtonSubscribe by remember {
            mutableStateOf(true)
        }
        var isRefreshing by remember {
            mutableStateOf(false)
        }
        val scope = rememberCoroutineScope()
        //ui
        Scaffold(containerColor = AppColor.background, topBar = {
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
                Spacer(modifier = Modifier.width(10.dp))
                Heading(
                    text = infoUserConfirmed?.value?.fullName ?: "Unknown",
                    size = CONSTANT.UI.TEXT_SIZE.MD,
                    maxLines = 1
                )
            }
        }) {
            PullToRefreshLazyColumn<Any>(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, it.calculateTopPadding(), 10.dp, 0.dp),
                isRefreshing = isRefreshing,
                onRefresh = {
                    scope.launch {
                        isRefreshing = true
                        delay(1000L)
                        init(userId)
                        isRefreshing = false
                    }
                },
                contentFix = {
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
                        enabled = enabledButtonSubscribe,
                        onClick = {
                            if (!userViewModel.isLoggedIn) {
                                Navigate(Router.LoginScreen)
                            } else {
                                if (!userViewModel.isSubscribing.value) {
                                    enabledButtonSubscribe = false
                                    userViewModel.subscribe(userId) {
                                        enabledButtonSubscribe = true
                                    }
                                } else {
                                    openUnsubscribeSheet.value = true
                                }
                            }
                        },
                        text = if (userViewModel.isSubscribing.value) "Subscribed" else "Subscribe",
                        height = 40.dp,
                        shape = CircleShape,
                        buttonColor = if (userViewModel.isSubscribing.value) AppColor.background_container else AppColor.primary_text,
                        textColor = if (userViewModel.isSubscribing.value) AppColor.primary_text else AppColor.background
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
            )
        }
        ModalBottomSheetContainer()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ModalBottomSheetContainer() {
        val unsubscribeState = rememberModalBottomSheetState()
        if (openUnsubscribeSheet.value) {
            ModalBottomSheet(
                onDismissRequest = { openUnsubscribeSheet.value = false },
                sheetState = unsubscribeState,
                containerColor = AppColor.background
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Heading(text = "Unsubscribe?", size = CONSTANT.UI.TEXT_SIZE.MD)

                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BtnText(
                            onClick = { openUnsubscribeSheet.value = false },
                            text = "No",
                            modifier = Modifier,
                            height = 35.dp,
                            buttonColor = AppColor.error
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        BtnText(
                            onClick = {
                                infoUserConfirmed?.value?.id?.let {
                                    userViewModel.unsubscribe(id = it) {
                                        openUnsubscribeSheet.value = false
                                    }
                                }
                            },
                            text = "Yes",
                            modifier = Modifier,
                            height = 35.dp,
                            buttonColor = AppColor.primary_content

                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
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
                        VideoCard(index = index, videoModel = video, onClick = { index, video ->
                            video?.uploader?.id?.let { uploaderId ->
                                Navigate(Router.VideoPlayerScreen(
                                    index,
                                    VideoPlayerScreen.VideoAt.PLAYER_SCREEN_OR_YOUR_PROFILE,
                                    uploaderId
                                ))
                            }

                        }, onLongClick = {})
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