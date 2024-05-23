package com.video.app.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.asFlow
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.video.app.R
import com.video.app.api.models.CommentModel
import com.video.app.api.models.CreateCommentDto
import com.video.app.api.models.UserModel
import com.video.app.api.models.VideoModel
import com.video.app.config.CONSTANT
import com.video.app.config.convertStringToLocalDate
import com.video.app.states.objects.AppInitializerState
import com.video.app.states.viewmodels.CommentAndReportViewModel
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.states.viewmodels.VideoAndPlaylistViewModel
import com.video.app.ui.screens.components.Heading
import com.video.app.ui.screens.components.Input
import com.video.app.ui.screens.components.ModalBottomSheetAddPlaylist
import com.video.app.ui.screens.components.ModalBottomSheetItem
import com.video.app.ui.screens.components.PullToRefreshLazyColumn
import com.video.app.ui.screens.components.VideoCard
import com.video.app.ui.theme.AppColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Date


class VideoPlayerScreen {
    object VideoAt {
        const val HOME_SCREEN = "home_screen"
        const val PLAYER_SCREEN_OR_YOUR_PROFILE = "player_screen"
        const val SEARCH_SCREEN = "search_screen"
        const val PLAYLIST_SCREEN = "playlist_screen"
        const val MY_VIDEO_SCREEN = "my_video_screen"
    }

    private lateinit var userViewModel: UserViewModel
    private lateinit var videoAndPlaylistViewModel: VideoAndPlaylistViewModel
    private lateinit var commentAndReportViewModel: CommentAndReportViewModel
    private var videoPlayer = mutableStateOf(VideoModel())
    private lateinit var player: ExoPlayer
    private lateinit var activity: Activity
    private var videoSelected = mutableStateOf(VideoModel())

    //for bottom sheet
    private var openDescription = mutableStateOf(false)
    private var openOnLongClickVideoCard = mutableStateOf(false)
    private var openAddPlaylist = mutableStateOf(false)
    private var openComments = mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    @Composable
    fun Screen(
        userViewModel: UserViewModel = AppInitializerState.userViewModel,
        videoAndPlaylistViewModel: VideoAndPlaylistViewModel = AppInitializerState.videoAndPlaylistViewModel,
        commentAndReportViewModel: CommentAndReportViewModel = AppInitializerState.commentAndReportViewModel,
        indexVideo: Int,
        videoAt: String,
        uploaderId: Long,
        playlistAt: String? = null
    ) {
        videoAndPlaylistViewModel.fetchVideosWithUploaderId(uploaderId = uploaderId)
        activity = LocalContext.current as Activity
        this.userViewModel = userViewModel;
        this.videoAndPlaylistViewModel = videoAndPlaylistViewModel
        this.commentAndReportViewModel = commentAndReportViewModel
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
                    if (!playlistAt.isNullOrEmpty()) {
                        when (playlistAt) {
                            PlaylistVideoScreen.PlaylistAt.MY_PROFILE_SCREEN -> {
                                videoAndPlaylistViewModel.videosOfMyPlaylistId.value?.get(indexVideo)!!
                            }

                            PlaylistVideoScreen.PlaylistAt.USER_PROFILE_SCREEN -> {
                                videoAndPlaylistViewModel.videosOfUserPlaylistId.value?.get(
                                    indexVideo
                                )!!
                            }

                            else -> VideoModel()
                        }
                    } else {
                        VideoModel()
                    }
                }

                VideoAt.MY_VIDEO_SCREEN -> {
                    videoAndPlaylistViewModel.myVideos.value?.get(indexVideo)!!
                }

                else -> {
                    VideoModel()
                }
            }
        commentAndReportViewModel.getCommentsByVideoId(videoPlayer.value.id!!)
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
                if (videoPlayer.value.id != null) {
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
                                videoPlayer.value.name?.let { name ->
                                    Heading(
                                        text = name, size = CONSTANT.UI.TEXT_SIZE.SM,
                                        maxLines = 2
                                    )
                                }
                                Row {
                                    videoPlayer.value.tag?.let { tag ->
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
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = AppColor.background_container
                            ),
                            onClick = {
                                openComments.value = true
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Heading(text = "Comments", size = CONSTANT.UI.TEXT_SIZE.MD)
                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        if (!videoAndPlaylistViewModel.isLoadingVideosWithUploaderId.value) {
                            if (!videoAndPlaylistViewModel.isErrorFetchVideoWithUploaderId.value) {
                                videos.value?.forEachIndexed { index, video ->
                                    VideoCard(
                                        index = index,
                                        videoModel = video,
                                        onClick = { indexOnClick, videoOnClick ->
                                            videoOnClick.uploader?.id?.let { uploaderId ->
                                                Navigate(
                                                    Router.VideoPlayerScreen(
                                                        indexOnClick,
                                                        VideoAt.PLAYER_SCREEN_OR_YOUR_PROFILE,
                                                        uploaderId,
                                                    )
                                                )
                                            }
                                        },
                                        onLongClick = { videoOnLongClick ->
                                            videoSelected.value = videoOnLongClick
                                            openOnLongClickVideoCard.value = true
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

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ModalBottomSheetContainer() {
        val openDescriptionState = rememberModalBottomSheetState()
        val openOnLongClickVideoCardState = rememberModalBottomSheetState()
        val openCommentsState = rememberModalBottomSheetState()
        if (openDescription.value) {
            ModalBottomSheet(
                onDismissRequest = { openDescription.value = false },
                sheetState = openDescriptionState,
                containerColor = AppColor.background,
            ) {
                DescriptionModalContent()
            }
        }
        if (openComments.value) {
            ModalBottomSheet(
                onDismissRequest = { openComments.value = false },
                sheetState = openCommentsState,
                containerColor = AppColor.background,
            ) {
                CommentModalContent()
            }
        }
        if (openOnLongClickVideoCard.value) {
            ModalBottomSheet(
                onDismissRequest = { openOnLongClickVideoCard.value = false },
                sheetState = openOnLongClickVideoCardState,
                containerColor = AppColor.background,
            ) {
                Column(modifier = Modifier) {
                    ModalBottomSheetItem(
                        icon = painterResource(id = R.drawable.save),
                        text = "Save to playlist"
                    ) {
                        if (!userViewModel.isLoggedIn) {
                            Navigate(Router.LoginScreen)
                        } else {
                            openOnLongClickVideoCard.value = false
                            openAddPlaylist.value = true
                        }
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
        ModalBottomSheetAddPlaylist(
            isOpen = openAddPlaylist.value,
            setOpen = { openAddPlaylist.value = false },
            videoAndPlaylistViewModel = videoAndPlaylistViewModel,
            video = videoSelected.value
        )
    }

    @Composable
    private fun DescriptionModalContent() {
        Column(modifier = Modifier.padding(20.dp, 0.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Heading(text = "Description", size = CONSTANT.UI.TEXT_SIZE.MD)
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        videoSelected.value = videoPlayer.value
                        openAddPlaylist.value = true
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.save),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(onClick = { Navigate(Router.HomeScreen) }) {
                        Icon(
                            imageVector = Icons.Rounded.Home,
                            contentDescription = null,
                            tint = AppColor.primary_text
                        )
                    }
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
                            Navigate(
                                Router.UserProfileScreen(
                                    userId = uploaderId
                                )
                            )
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
                videoPlayer.value.uploader?.fullName?.let {
                    Heading(
                        text = it,
                        size = CONSTANT.UI.TEXT_SIZE.SM
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    private fun CommentModalContent(
        context: Context = LocalContext.current,
        isLoadingComments: MutableState<Boolean> = mutableStateOf(false)
    ) {
        val comments =
            commentAndReportViewModel.comments.asFlow().collectAsState(initial = emptyList())
        val userCurrent = userViewModel.userCurrent.asFlow().collectAsState(initial = UserModel())
        var parentComment by remember {
            mutableStateOf(CommentModel())
        }
        val commentContent = rememberSaveable {
            mutableStateOf("")
        }
        var enabledSubmitBtn by remember {
            mutableStateOf(true)
        }
        Column(modifier = Modifier.padding(20.dp, 0.dp)) {
            Column {
                Heading(text = "Comments", size = CONSTANT.UI.TEXT_SIZE.MD)
                Spacer(modifier = Modifier.height(10.dp))
                PullToRefreshLazyColumn<Any>(
                    modifier = Modifier
                        .fillMaxWidth(),
                    isRefreshing = isLoadingComments.value,
                    onRefresh = {
                        isLoadingComments.value = true
                        commentAndReportViewModel.getCommentsByVideoId(videoPlayer.value.id!!) {
                            isLoadingComments.value = false
                        }
                    },
                    contentFix = {
                        comments.value!!.forEachIndexed { _, commentModel ->
                            CommentCard(commentModel = commentModel, replyOnClick = {

                                commentContent.value = if (it.user!!.id == userCurrent.value!!.id)
                                    "Reply me: " else "Reply ${commentModel.user!!.fullName}: "
                                parentComment = it
                            })
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Input(
                    value = commentContent.value,
                    onValueChange = {
                        commentContent.value = it
                    },
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(58.dp),
                )
                IconButton(enabled = enabledSubmitBtn, onClick = {
                    if (userViewModel.isLoggedIn) {
                        if (commentContent.value.isNotBlank()) {
                            enabledSubmitBtn = false
                            commentAndReportViewModel.addComment(
                                createCommentDto = CreateCommentDto(
                                    content = commentContent.value,
                                    videoId = videoPlayer.value.id!!,
                                    parentCommentId = parentComment.id!!
                                ),
                                success = {
                                    commentContent.value = ""
                                    parentComment = CommentModel()
                                    commentAndReportViewModel.getCommentsByVideoId(videoPlayer.value.id!!)
                                },
                                action = {
                                    enabledSubmitBtn = true
                                }
                            )
                        } else {
                            Toast.makeText(context, "Comment is empty", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Navigate(Router.LoginScreen)
                    }
                }, modifier = Modifier.padding(top = 5.dp)) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        tint = AppColor.primary_text
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun CommentCard(
        modifier: Modifier = Modifier,
        commentModel: CommentModel,
        replyOnClick: (commentModel: CommentModel) -> Unit
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            val avatarModifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
            val painterError = painterResource(id = R.drawable.user_icon1)
            Box(modifier = avatarModifier) {
                AsyncImage(
                    model = commentModel.user!!.imageURL,
                    contentDescription = null,
                    placeholder = painterError,
                    error = painterError,
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(2.dp))
            Column {
                Heading(
                    text = commentModel.user!!.fullName ?: "Unknown",
                    size = CONSTANT.UI.TEXT_SIZE.SM,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    text = commentModel.content!!, style = TextStyle(
                        fontStyle = FontStyle.Normal,
                        color = AppColor.primary_text
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Heading(
                        text = "${convertStringToLocalDate(commentModel.createdAt!!)}",
                        size = CONSTANT.UI.TEXT_SIZE.SM_
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    TextButton(
                        onClick = { replyOnClick(commentModel) },
                        modifier = Modifier.height(30.dp)
                    ) {
                        Heading(
                            text = "Reply",
                            size = CONSTANT.UI.TEXT_SIZE.SM_
                        )
                    }
                }
            }
        }
        var viewMoreComments by remember {
            mutableStateOf(false)
        }
        if (commentModel.replies!!.isNotEmpty() && !viewMoreComments) {
            TextButton(
                onClick = { viewMoreComments = true },
                modifier = Modifier.height(30.dp)
            ) {
                Heading(
                    modifier = Modifier.padding(start = 20.dp),
                    text = "View ${commentModel.replies.size} comments",
                    size = CONSTANT.UI.TEXT_SIZE.SM_
                )
            }
        }
        if (viewMoreComments) {
            commentModel.replies.forEachIndexed { _, commentModelChild ->
                CommentCard(
                    modifier = Modifier.padding(start = 20.dp),
                    commentModel = commentModelChild,
                    replyOnClick = {
                        replyOnClick(commentModel)
                    }
                )
            }
            TextButton(
                onClick = { viewMoreComments = false },
                modifier = Modifier.height(30.dp)
            ) {
                Heading(
                    modifier = Modifier.padding(start = 20.dp),
                    text = "Close comments",
                    size = CONSTANT.UI.TEXT_SIZE.SM_
                )
            }
        }
    }

    @Composable
    private fun PlayVideoContainer(context: Context = LocalContext.current) {
        var isFullScreen by remember {
            mutableStateOf(false)
        }
        player = ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoPlayer.value.src ?: ""))
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