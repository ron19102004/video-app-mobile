package com.video.app.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.video.app.R
import com.video.app.api.models.VideoModel
import com.video.app.config.CONSTANT
import com.video.app.states.objects.AppInitializerState
import com.video.app.states.objects.UiState
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.states.viewmodels.VideoAndPlaylistViewModel
import com.video.app.ui.screens.components.Heading
import com.video.app.ui.screens.components.VideoCardRow
import com.video.app.ui.theme.AppColor
import kotlinx.coroutines.launch

class SearchScreen {
    private lateinit var userViewModel: UserViewModel;
    private var isLoadingSearchVideo = mutableStateOf(false)

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun Screen(
        userViewModel: UserViewModel = AppInitializerState.userViewModel,
        videoAndPlaylistViewModel: VideoAndPlaylistViewModel = AppInitializerState.videoAndPlaylistViewModel
    ) {
        this.userViewModel = userViewModel
        val scope = rememberCoroutineScope()
        SearchBar(
            enabled = !isLoadingSearchVideo.value,
            colors = SearchBarDefaults.colors(
                containerColor = AppColor.background,
                inputFieldColors = TextFieldDefaults.colors(
                    focusedTextColor = AppColor.primary_text,
                    unfocusedTextColor = AppColor.primary_text
                )
            ),
            query = videoAndPlaylistViewModel.queryOnSearchScreen.value,
            onQueryChange = { videoAndPlaylistViewModel.queryOnSearchScreen.value = it },
            onSearch = {
                scope.launch {
                    isLoadingSearchVideo.value = true
                    videoAndPlaylistViewModel.searchVideosByName(videoAndPlaylistViewModel.queryOnSearchScreen.value) {
                        isLoadingSearchVideo.value = false
                    }
                }
            },
            active = true,
            onActiveChange = {

            },
            placeholder = { Text(text = "Search...", color = AppColor.second_text) },
            trailingIcon = {
                IconButton(
                    enabled = !isLoadingSearchVideo.value,
                    onClick = {
                        videoAndPlaylistViewModel.searchVideosByName(videoAndPlaylistViewModel.queryOnSearchScreen.value)
                    }) {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = null,
                        tint = AppColor.primary_text
                    )
                }
            },
            leadingIcon = {
                IconButton(onClick = { AppInitializerState.navController.popBackStack() }) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        contentDescription = null,
                        tint = AppColor.primary_text
                    )
                }
            }
        ) {
            if (isLoadingSearchVideo.value) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(color = AppColor.primary_text)
                }
            } else {
                val result = videoAndPlaylistViewModel.videosOnSearchScreen.asFlow().collectAsState(
                    initial = emptyList()
                )
                LazyColumn(modifier = Modifier.padding(10.dp)) {
                    item {
                        Heading(
                            text = "${result?.value?.size} results",
                            size = CONSTANT.UI.TEXT_SIZE.MD
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    result.value?.size?.let { size ->
                        items(size) {
                            result?.value?.get(it)
                                ?.let { video ->
                                    VideoCardRow(
                                        index = it,
                                        videoModel = video,
                                        onClick = { index, video ->
                                            video?.uploader?.id?.let { uploaderId ->
                                                Navigate(
                                                    Router.VideoPlayerScreen(
                                                        index,
                                                        VideoPlayerScreen.VideoAt.SEARCH_SCREEN,
                                                        uploaderId,
                                                        PlaylistVideoScreen.PlaylistAt.MY_PROFILE_SCREEN
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
        }
    }
}
