package com.video.app.ui.screens

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
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
import com.video.app.Navigate
import com.video.app.R
import com.video.app.api.models.VideoModel
import com.video.app.config.CONSTANT
import com.video.app.ui.screens.layouts.MainLayout
import com.video.app.states.viewmodels.CategoryAndCountryViewModel
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.states.viewmodels.VideoAndPlaylistViewModel
import com.video.app.ui.screens.components.Heading
import com.video.app.ui.screens.components.PullToRefreshLazyColumn
import com.video.app.ui.screens.components.VideoCard
import com.video.app.ui.theme.AppColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeScreen {
    private lateinit var categoryAndCountryViewModel: CategoryAndCountryViewModel
    private lateinit var videoAndPlaylistViewModel: VideoAndPlaylistViewModel
    private lateinit var userViewModel: UserViewModel
    private var btnTagSelected = mutableIntStateOf(0)
    private var openOptionVideoOnLongClick = mutableStateOf(false)
    private var videoBeOnLongClick = mutableStateOf(VideoModel());

    @Composable
    fun Screen(
        userViewModel: UserViewModel,
        categoryAndCountryViewModel: CategoryAndCountryViewModel,
        videoAndPlaylistViewModel: VideoAndPlaylistViewModel
    ) {
        this.userViewModel = userViewModel
        this.categoryAndCountryViewModel = categoryAndCountryViewModel
        this.videoAndPlaylistViewModel = videoAndPlaylistViewModel
        var isRefreshingHome by remember {
            mutableStateOf(false)
        }
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
                CategoriesTagBar()
                Spacer(modifier = Modifier.height(10.dp))
                PullToRefreshLazyColumn<Any>(
                    isRefreshing = isRefreshingHome,
                    onRefresh = {
                        userViewModel.viewModelScope.launch {
                            isRefreshingHome = true
                            delay(1000L)
                            videoAndPlaylistViewModel.fetchVideosHome { isRefreshingHome = false }
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    contentFix = {
                        val videos =
                            videoAndPlaylistViewModel.videosOnHomeScreen.asFlow().collectAsState(
                                initial = emptyList()
                            )
                        if (videos?.value?.isNullOrEmpty() == false) {
                            videos?.value?.forEachIndexed { index, video ->
                                VideoCard(
                                    index = index,
                                    videoModel = video,
                                    onClick = { index, video ->
                                        video?.uploader?.id?.let {
                                            Navigate(
                                                Router.VideoPlayerScreen.setArgs(
                                                    index,
                                                    VideoPlayerScreen.VideoAt.HOME_SCREEN,
                                                    it
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
                            videoBeOnLongClick?.value?.uploader?.id?.let {
                                userViewModel.fetchInfoUserConfirmed(id = it)
                                Navigate(Router.YourProfileScreen.setArgs(it))
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
                            //
                        }
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun ModalBottomSheetItem(icon: Painter, text: String, onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = {}
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconModifier = Modifier.size(20.dp)
            Spacer(modifier = Modifier.width(20.dp))
            Box(modifier = iconModifier) {
                Image(
                    painter = icon,
                    contentDescription = null,
                    modifier = iconModifier,
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Heading(text = text, size = CONSTANT.UI.TEXT_SIZE.SM)
        }
    }


    @Composable
    private fun CategoriesTagBar() {
        val categories =
            categoryAndCountryViewModel.categories.asFlow().collectAsState(initial = emptyList())
        if (categories != null) {
            LazyRow {
                categories.value?.let { list ->
                    items(list.size) { it ->
                        ElevatedButton(
                            onClick = {
                                btnTagSelected.value = it
                            },
                            modifier = Modifier,
                            shape = CircleShape,
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = if (btnTagSelected.value == it) AppColor.background_container
                                else Color.Transparent
                            )
                        ) {
                            Text(
                                text = "${categories.value!![it].name}", style = TextStyle(
                                    color = AppColor.primary_text,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen().Screen(
        userViewModel = viewModel(),
        categoryAndCountryViewModel = viewModel(),
        videoAndPlaylistViewModel = viewModel()
    )
}