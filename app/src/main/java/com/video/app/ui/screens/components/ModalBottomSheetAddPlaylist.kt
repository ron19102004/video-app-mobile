package com.video.app.ui.screens.components

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import coil.compose.AsyncImage
import com.video.app.R
import com.video.app.api.models.VideoModel
import com.video.app.config.CONSTANT
import com.video.app.states.viewmodels.VideoAndPlaylistViewModel
import com.video.app.ui.theme.AppColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetAddPlaylist(
    isOpen: Boolean, setOpen: () -> Unit,
    videoAndPlaylistViewModel: VideoAndPlaylistViewModel,
    video: VideoModel
) {
    val state = rememberModalBottomSheetState()
    val playlists =
        videoAndPlaylistViewModel.myPlaylist.asFlow().collectAsState(initial = emptyList())
    if (isOpen) {
        ModalBottomSheet(
            onDismissRequest = { setOpen() },
            sheetState = state,
            containerColor = AppColor.background
        ) {
            val modifier = Modifier
                .height(100.dp)
                .width(150.dp)
            val painterError = painterResource(id = R.drawable.video_bg)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp)
            ) {
                Heading(text = "Add to playlist", size = CONSTANT.UI.TEXT_SIZE.MD)
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(10.dp))
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp)
            ) {
                playlists?.value?.size?.let { size ->
                    items(size) { index ->
                        val playlist = playlists?.value!![index]
                        playlist?.let {
                            Card(
                                modifier = modifier,
                                colors = CardDefaults.cardColors(
                                    containerColor = AppColor.background_trans
                                ), onClick = {
                                    video?.id?.let { videoId ->
                                        it?.id?.let { playlistId ->
                                            videoAndPlaylistViewModel.addVideoToPlaylist(
                                                videoId = videoId,
                                                playlistId = playlistId
                                            )
                                        }
                                    }
                                    setOpen()
                                }
                            ) {
                                Box(modifier = modifier) {
                                    AsyncImage(
                                        model = it?.image,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = modifier,
                                        placeholder = painterError,
                                        error = painterError
                                    )
                                    Column(
                                        modifier = modifier.background(
                                            color = AppColor.background_trans,
                                            shape = RoundedCornerShape(CONSTANT.UI.ROUNDED_INPUT_BUTTON)
                                        ),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Add,
                                            contentDescription = null,
                                            tint = AppColor.primary_text
                                        )
                                        Heading(
                                            text = it?.name ?: "Unknown",
                                            size = CONSTANT.UI.TEXT_SIZE.SM,
                                            maxLines = 2
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModalBottomSheetItem(icon: Painter, text: String, onClick: () -> Unit) {
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