package com.video.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.video.app.R
import com.video.app.api.models.VideoModel
import com.video.app.config.CONSTANT
import com.video.app.states.objects.AppInitializerState
import com.video.app.ui.screens.layouts.MainLayout
import com.video.app.states.viewmodels.CategoryAndCountryViewModel
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.states.viewmodels.VideoAndPlaylistViewModel
import com.video.app.ui.screens.components.BtnText
import com.video.app.ui.screens.components.Heading
import com.video.app.ui.screens.components.ModalBottomSheetAddPlaylist
import com.video.app.ui.screens.components.ModalBottomSheetItem
import com.video.app.ui.screens.components.PullToRefreshLazyColumn
import com.video.app.ui.screens.components.VideoCard
import com.video.app.ui.theme.AppColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeScreen {
    private lateinit var categoryAndCountryViewModel: CategoryAndCountryViewModel
    private lateinit var videoAndPlaylistViewModel: VideoAndPlaylistViewModel
    private lateinit var userViewModel: UserViewModel
    private var btnTagSelected = mutableIntStateOf(-1)
    private var videoBeOnLongClick = mutableStateOf(VideoModel());

    //for bottom sheet
    private var openOptionVideoOnLongClick = mutableStateOf(false)
    private var openAddPlaylist = mutableStateOf(false)

    @Composable
    fun Screen(
        userViewModel: UserViewModel = AppInitializerState.userViewModel,
        categoryAndCountryViewModel: CategoryAndCountryViewModel = AppInitializerState.categoryAndCountryViewModel,
        videoAndPlaylistViewModel: VideoAndPlaylistViewModel = AppInitializerState.videoAndPlaylistViewModel
    ) {
        this.userViewModel = userViewModel
        this.categoryAndCountryViewModel = categoryAndCountryViewModel
        this.videoAndPlaylistViewModel = videoAndPlaylistViewModel
        var isRefreshingHome by remember {
            mutableStateOf(false)
        }
        val scope = rememberCoroutineScope()
        var pageCurrent by remember {
            mutableIntStateOf(0)
        }
        videoAndPlaylistViewModel.fetchVideosHome()
        val videos =
            videoAndPlaylistViewModel.videosOnHomeScreen.asFlow().collectAsState(
                initial = emptyList()
            )
        MainLayout(userViewModel = userViewModel) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(0.dp, 10.dp)
                    .clip(shape = CircleShape)
                    .clickable {
                        Navigate(Router.SearchScreen)
                    },
                shape = CircleShape,
                placeholder = { Text(text = "Search...", color = Color.DarkGray) },
                trailingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                enabled = false,
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = AppColor.background,
                    disabledTrailingIconColor = Color.DarkGray,
                    disabledIndicatorColor = Color.DarkGray
                )
            )
            Column {
                CategoriesTagContainer()
                Spacer(modifier = Modifier.height(10.dp))
                PullToRefreshLazyColumn<Any>(
                    isRefreshing = isRefreshingHome,
                    onRefresh = {
                        scope.launch {
                            isRefreshingHome = true
                            delay(1000L)
                            if (btnTagSelected.intValue == -1) {
                                videoAndPlaylistViewModel.fetchVideosHome {
                                    isRefreshingHome = false
                                }
                            } else {
                                videoAndPlaylistViewModel
                                    .fetchVideosHomeWithCategoryId(
                                        categoryId = btnTagSelected.intValue.toLong(),
                                        action = { isRefreshingHome = false })
                            }
                            pageCurrent = 0
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    contentFix = {
                        if (!videos.value.isNullOrEmpty()) {
                            videos.value!!.forEachIndexed { index, video ->
                                VideoCard(
                                    index = index,
                                    videoModel = video,
                                    onClick = { idx, videoChild ->
                                        videoChild.uploader?.id?.let {
                                            Navigate(
                                                Router.VideoPlayerScreen(
                                                    index = idx,
                                                    videoAt = VideoPlayerScreen.VideoAt.HOME_SCREEN,
                                                    uploaderId = it,
                                                    PlaylistVideoScreen.PlaylistAt.MY_PROFILE_SCREEN
                                                )
                                            )
                                        }
                                    },
                                    onLongClick = {
                                        videoBeOnLongClick.value = it
                                        if (videoBeOnLongClick.value.id != null)
                                            openOptionVideoOnLongClick.value = true
                                    }
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                            BtnText(onClick = {
                                scope.launch {
                                    pageCurrent++
                                    delay(1000L)
                                    if (btnTagSelected.intValue == -1) {
                                        videoAndPlaylistViewModel.fetchVideosHome(page = pageCurrent)
                                    } else {
                                        videoAndPlaylistViewModel
                                            .fetchVideosHomeWithCategoryId(
                                                categoryId = btnTagSelected.intValue.toLong(),
                                                action = { },
                                                page = pageCurrent
                                            )
                                    }
                                }
                            }, text = "More", height = 40.dp)
                            Spacer(modifier = Modifier.height(20.dp))
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Heading(
                                    text = "Empty",
                                    size = CONSTANT.UI.TEXT_SIZE.LG,
                                    color = AppColor.background_container
                                )
                            }
                        }
                    }
                )
            }
        }
        ModalBottomSheetContainer()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ModalBottomSheetContainer() {
        val openOptionVideoOnLongClickState = rememberModalBottomSheetState()
        if (openOptionVideoOnLongClick.value) {
            ModalBottomSheet(
                onDismissRequest = { openOptionVideoOnLongClick.value = false },
                sheetState = openOptionVideoOnLongClickState, containerColor = AppColor.background
            ) {
                val nameUploader = videoBeOnLongClick.value.uploader?.fullName?.split(" ")
                Column(modifier = Modifier) {
                    if (nameUploader != null) {
                        ModalBottomSheetItem(
                            icon = painterResource(id = R.drawable.user_icon1),
                            text = "Go to ${nameUploader[nameUploader.size - 1]} account"
                        ) {
                            videoBeOnLongClick.value.uploader?.id?.let {
                                Navigate(
                                    Router.UserProfileScreen(
                                        userId = it
                                    )
                                )
                            }
                        }
                    }
                    ModalBottomSheetItem(
                        icon = painterResource(id = R.drawable.save),
                        text = "Save to playlist"
                    ) {
                        if (!userViewModel.isLoggedIn) {
                            Navigate(Router.LoginScreen)
                        } else {
                            openOptionVideoOnLongClick.value = false
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
            video = videoBeOnLongClick.value
        )
    }


    @Composable
    private fun CategoriesTagContainer() {
        val categories =
            categoryAndCountryViewModel.categories.asFlow().collectAsState(initial = emptyList())
        val Tag: @Composable (index: Int, text: String, onClick: () -> Unit) -> Unit =
            { index, text, onClick ->
                ElevatedButton(
                    onClick = onClick,
                    modifier = Modifier,
                    shape = CircleShape,
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = if (btnTagSelected.intValue == index) AppColor.primary_content
                        else Color.Transparent
                    )
                ) {
                    Text(
                        text = text, style = TextStyle(
                            color = AppColor.primary_text,
                            fontWeight = FontWeight.SemiBold,
                        )
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
            }
        LazyRow {
            item {
                Tag(-1, "All") {
                    btnTagSelected.intValue = -1
                    videoAndPlaylistViewModel
                        .fetchVideosHome()
                }
                Spacer(modifier = Modifier.width(5.dp))
            }
            categories.value?.let { list ->
                items(list.size) {
                    Tag(it, categories.value!![it].name) {
                        btnTagSelected.intValue = it
                        videoAndPlaylistViewModel
                            .fetchVideosHomeWithCategoryId(categoryId = categories.value!![it].id)
                    }
                }
            }
        }
    }
}
