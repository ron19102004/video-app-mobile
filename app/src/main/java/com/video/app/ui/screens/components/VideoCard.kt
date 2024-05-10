package com.video.app.ui.screens.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.video.app.R
import com.video.app.api.models.VideoModel
import com.video.app.config.CONSTANT
import com.video.app.ui.theme.AppColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoCard(
    index: Int,
    videoModel: VideoModel,
    onClick: (index: Int, videoModel: VideoModel) -> Unit,
    onLongClick: (videoModel: VideoModel) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .combinedClickable(
            onLongClick = { onLongClick(videoModel) },
            onClick = { onClick(index, videoModel) }
        )) {
        val painterImageVideoError = painterResource(id = R.drawable.video_bg)
        val imageVideoModifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(CONSTANT.UI.ROUNDED_INPUT_BUTTON))
        Box(modifier = imageVideoModifier) {
            AsyncImage(
                model = videoModel.image, contentDescription = null,
                modifier = imageVideoModifier,
                contentScale = ContentScale.Crop,
                placeholder = painterImageVideoError,
                error = painterImageVideoError
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        val painterAvatarError = painterResource(id = R.drawable.user)
        val imageAvatarModifier = Modifier
            .size(35.dp)
            .clip(CircleShape)
        Row {
            Box(modifier = imageAvatarModifier) {
                AsyncImage(
                    model = videoModel?.uploader?.imageURL, contentDescription = null,
                    modifier = imageAvatarModifier,
                    contentScale = ContentScale.Fit,
                    placeholder = painterAvatarError,
                    error = painterAvatarError
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                videoModel.name?.let { name ->
                    Text(
                        text = name,
                        modifier = Modifier.fillMaxWidth(),
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = CONSTANT.UI.TEXT_SIZE.SM,
                            color = AppColor.primary_text,
                        ),
                        minLines = 1,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row {
                    videoModel?.uploader?.fullName?.let { nameUploader ->
                        Text(
                            text = nameUploader,
                            style = TextStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = CONSTANT.UI.TEXT_SIZE.SM_,
                                color = Color.DarkGray
                            ),
                            minLines = 1,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoCardRow(
    index: Int,
    videoModel: VideoModel,
    onClick: (index: Int, videoModel: VideoModel) -> Unit,
    onLongClick: (videoModel: VideoModel) -> Unit = {}
) {
    val imgVideoModifier = Modifier
        .height(100.dp)
        .width(150.dp)
        .clip(RoundedCornerShape(CONSTANT.UI.ROUNDED_INPUT_BUTTON))
    val painterImgVideoError = painterResource(id = R.drawable.video_bg)
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColor.background_container
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onLongClick = { onLongClick(videoModel) },
                ) {
                    onClick(index, videoModel)
                },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            Box(modifier = imgVideoModifier) {
                AsyncImage(
                    model = videoModel.image, contentDescription = null,
                    modifier = imgVideoModifier,
                    contentScale = ContentScale.Crop,
                    placeholder = painterImgVideoError,
                    error = painterImgVideoError
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Heading(
                    text = videoModel.name ?: "Unknown",
                    size = CONSTANT.UI.TEXT_SIZE.SM,
                    maxLines = 3
                )
                Text(
                    text = videoModel?.uploader?.fullName ?: "Unknown",
                    style = TextStyle(
                        color = Color.LightGray
                    )
                )
            }
        }
    }
}